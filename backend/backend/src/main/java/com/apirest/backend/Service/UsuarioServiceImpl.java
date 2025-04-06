package com.apirest.backend.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;
import com.apirest.backend.Repository.AvisoRepository;
import com.apirest.backend.Repository.ReporteRepository;
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

    @Override
    public String guardarUsuario(Usuario usuario) {
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
            Optional<Usuario> usuarioOpt = usuarioRepository.findByVerificacionEmail_Token(email);

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
            return "Contraseña incorrecta. Intento " + intentos + " de 5.";
        }

        usuario.setIntentosFallidos(0);
        usuarioRepository.save(usuario);
        return "Login exitoso. Bienvenido, " + usuario.getNombre();
    }
    @Override
    public String registrarUsuario(Usuario usuario) {
        // Validar si el correo ya está registrado
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        // Guardar el usuario
        usuarioRepository.save(usuario);
        return "Usuario registrado con éxito: " + usuario.getNombre();
    }

    @Override
    public void actualizarPerfil(String id, String nombre, String telefono, MultipartFile fotoPerfil) throws Exception {
        // Buscar el usuario por ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar y actualizar el nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            usuario.setNombre(nombre);
        }

        // Validar y actualizar el teléfono
        if (telefono != null && !telefono.trim().isEmpty()) {
            if (!telefono.matches("\\d{10}")) { // Ejemplo: validar que el teléfono tenga 10 dígitos
                throw new IllegalArgumentException("El número de teléfono debe tener 10 dígitos");
            }
            usuario.setTelefono(Integer.parseInt(telefono));
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

            // Guardar la imagen (puedes usar un servicio de almacenamiento como AWS S3 o guardarla localmente)
            String rutaImagen = guardarImagen(fotoPerfil);
            usuario.setFotoPerfil(rutaImagen);
        }

        // Guardar los cambios en la base de datos
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

    private void eliminarDatosRelacionados(String usuarioId) {
        // Eliminar avisos relacionados
        avisoRepository.deleteByUsuarioId(usuarioId);

        // Eliminar reportes relacionados
        reporteRepository.deleteByUsuarioId(usuarioId);
    }

    private String guardarImagen(MultipartFile fotoPerfil) throws IOException {
        // Ejemplo: Guardar la imagen en el sistema de archivos local
        String ruta = "uploads/" + fotoPerfil.getOriginalFilename();
        Path path = Paths.get(ruta);
        Files.createDirectories(path.getParent());
        Files.write(path, fotoPerfil.getBytes());
        return ruta;
    }
}