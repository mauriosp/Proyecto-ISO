package com.apirest.backend.Service;

import com.apirest.backend.Model.AuditoriaPerfil;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;
import com.apirest.backend.Repository.AvisoRepository;
import com.apirest.backend.Repository.ReporteRepository;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    private static final int MAX_INTENTOS = 5;
    private static final int TIEMPO_BLOQUEO = 15; // minutos

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AvisoRepository avisoRepository;

    @Autowired
    private ReporteRepository reporteRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String guardarUsuario(Usuario usuario) {
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        usuarioRepository.save(usuario);
        return "El Usuario " + usuario.getNombre() + ", fue creado con éxito";
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public String loginUsuario(String email, String contraseña) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

            if (usuarioOpt.isEmpty()) {
                logger.warn("Intento de login con email no registrado: {}", email);
                return "Usuario no encontrado";
            }

            Usuario usuario = usuarioOpt.get();

            // Verificación de email
            List<VerificacionEmail> verificaciones = usuario.getVerificacionEmail();
            if (verificaciones == null || verificaciones.isEmpty() || 
                !verificaciones.get(verificaciones.size() - 1).getVerificado()) {
                logger.warn("Intento de login con correo no verificado: {}", email);
                return "Correo no verificado. Por favor verifica tu cuenta.";
            }

            // Verificación de bloqueo de cuenta
            if (usuario.isEstado()) {
                if (usuario.getFechaBloqueo() != null) {
                    long minutos = (new Date().getTime() - usuario.getFechaBloqueo().getTime()) / (60 * 1000);
                    if (minutos < TIEMPO_BLOQUEO) {
                        logger.warn("Intento de login en cuenta bloqueada: {}", email);
                        return "Cuenta bloqueada. Intenta en " + (TIEMPO_BLOQUEO - minutos) + " minutos.";
                    } else {
                        usuario.setEstado(false);
                        usuario.setIntentosFallidos(0);
                        usuario.setFechaBloqueo(null);
                    }
                }
            }

            // Validación de contraseña
            if (!passwordEncoder.matches(contraseña, usuario.getContraseña())) {
                int intentos = usuario.getIntentosFallidos() + 1;
                usuario.setIntentosFallidos(intentos);

                if (intentos >= MAX_INTENTOS) {
                    usuario.setEstado(true);
                    usuario.setFechaBloqueo(new Date());
                    usuarioRepository.save(usuario);
                    
                    logger.error("Cuenta bloqueada por múltiples intentos fallidos: {}", email);
                    return "Cuenta bloqueada por múltiples intentos fallidos.";
                }

                usuarioRepository.save(usuario);
                logger.warn("Contraseña incorrecta para usuario: {}. Intento {} de {}", email, intentos, MAX_INTENTOS);
                return "Contraseña incorrecta. Intento " + intentos + " de " + MAX_INTENTOS + ".";
            }

            // Login exitoso
            usuario.setIntentosFallidos(0);
            usuarioRepository.save(usuario);
            return "Login exitoso. Bienvenido, " + usuario.getNombre();
        } catch (Exception e) {
            logger.error("Error en el proceso de login: ", e);
            return "Error en el proceso de login: " + e.getMessage();
        }
    }
    
    @Override
    public String registrarUsuario(Usuario usuario) {
        // Validar si el correo ya está registrado
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        // Validar el formato del correo electrónico
        if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
        }

        // Validar la contraseña
        if (usuario.getContraseña() == null || usuario.getContraseña().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        if (!usuario.getContraseña().matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula.");
        }
        if (!usuario.getContraseña().matches(".*[a-z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra minúscula.");
        }
        if (!usuario.getContraseña().matches(".*\\d.*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un número.");
        }
        if (!usuario.getContraseña().matches(".*[@#$%^&+=].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un carácter especial (@#$%^&+=).");
        }

        // Validar el nombre
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (!usuario.getNombre().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios.");
        }

        String telefono = String.valueOf(usuario.getTelefono());
        if (!telefono.matches("\\d{10}")) {
            throw new IllegalArgumentException("El número de teléfono debe tener 10 dígitos.");
        }

        // Guardar el usuario
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        usuarioRepository.save(usuario);
        return "Usuario registrado con éxito: " + usuario.getNombre();
    }

    @Override
    public void actualizarPerfil(String id, String nombre, String telefono, MultipartFile fotoPerfil) throws Exception {
        // Buscar el usuario por ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Inicializar la lista de auditoría si es null
        if (usuario.getAuditoriaPerfil() == null) {
            usuario.setAuditoriaPerfil(new ArrayList<>());
        }

        // Validar y actualizar el nombre
        if (nombre != null && !nombre.trim().isEmpty() && !nombre.equals(usuario.getNombre())) {
            String nombreAnterior = usuario.getNombre();
            usuario.setNombre(nombre);
            
            // Registro de auditoría para el cambio de nombre
            registrarAuditoria(usuario, "nombre", nombreAnterior, nombre);
        }

       // Validar y actualizar el teléfono
        if (telefono != null && !telefono.trim().isEmpty()) {
        if (!telefono.matches("\\d{10}")) {
            throw new IllegalArgumentException("El número de teléfono debe tener 10 dígitos");
        }
        
        // Guardar el valor anterior para auditoría
        String telefonoAnterior = "";
        Long telefonoActual = usuario.getTelefono();
        if (telefonoActual != null) {
            telefonoAnterior = telefonoActual.toString();
        }
        
        // Convertir y asignar el nuevo valor
        usuario.setTelefono(Long.parseLong(telefono));
        
        // Registro de auditoría para el cambio de teléfono
        registrarAuditoria(usuario, "telefono", telefonoAnterior, telefono);
        }

        // Validar y actualizar la foto de perfil
        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String contentType = fotoPerfil.getContentType();
            if (!List.of("image/jpeg", "image/png", "image/jpg").contains(contentType)) {
                throw new IllegalArgumentException("Formato de imagen no permitido. Solo se permiten JPG, JPEG y PNG.");
            }
            if (fotoPerfil.getSize() > 5 * 1024 * 1024) { // 5MB
                throw new IllegalArgumentException("El tamaño de la imagen no debe exceder los 5MB.");
            }

            String rutaAnterior = usuario.getFotoPerfil() != null ? usuario.getFotoPerfil() : "";
            String rutaImagen = guardarImagen(fotoPerfil);
            usuario.setFotoPerfil(rutaImagen);
            
            // Registro de auditoría para el cambio de foto de perfil
            registrarAuditoria(usuario, "fotoPerfil", rutaAnterior, rutaImagen);
        }

        // Guardar los cambios en la base de datos
        usuarioRepository.save(usuario);
    }
    
    // Método para actualizar la contraseña con auditoría
    @Override
    public void actualizarContraseña(String id, String nuevaContraseña) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Encriptar la nueva contraseña
        String contraseñaEncriptada = passwordEncoder.encode(nuevaContraseña);
        usuario.setContraseña(contraseñaEncriptada);
        
        // Registrar en auditoría (sin mostrar las contraseñas reales)
        registrarAuditoria(usuario, "contraseña", "********", "********");
        
        usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarCuenta(String id) {
        // Buscar el usuario por ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Eliminar datos relacionados
        eliminarDatosRelacionados(id);

        // Eliminar el usuario
        usuarioRepository.delete(usuario);
    }

    @Override
    public boolean existeUsuarioPorId(ObjectId idUsuario) {
        return usuarioRepository.existsById(idUsuario);
    }

    private void eliminarDatosRelacionados(String usuarioId) {
        ObjectId propietarioId = new ObjectId(usuarioId);
        avisoRepository.deleteByIdPropietario(propietarioId);

        // Eliminar reportes relacionados
        reporteRepository.deleteByIdUsuario(usuarioId);
    }

    private String guardarImagen(MultipartFile fotoPerfil) throws IOException {
        // Ejemplo: Guardar la imagen en el sistema de archivos local
        String ruta = "uploads/" + fotoPerfil.getOriginalFilename();
        Path path = Paths.get(ruta);
        Files.createDirectories(path.getParent());
        Files.write(path, fotoPerfil.getBytes());
        return ruta;
    }
    
    // Método privado para registrar cambios en la auditoría (sin el parámetro ejecutadoPor)
    private void registrarAuditoria(Usuario usuario, String campo, String valorAnterior, 
                                    String valorNuevo) {
        AuditoriaPerfil auditoria = new AuditoriaPerfil();
        auditoria.setFechaModificacion(new Date());
        auditoria.setCampoModificado(campo);
        auditoria.setValorAnterior(valorAnterior);
        auditoria.setValorNuevo(valorNuevo);
        
        usuario.getAuditoriaPerfil().add(auditoria);
        
        logger.info("Registro de auditoría creado: usuario={}, campo={}", 
                  usuario.getId(), campo);
    }
}