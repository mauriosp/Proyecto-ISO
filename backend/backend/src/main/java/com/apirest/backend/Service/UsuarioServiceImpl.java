package com.apirest.backend.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public String guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario); 
        return "El Usuario" + usuario.getNombre() + ",fue creado con éxito";
    }

    @Override
    public List <Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
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

    private String guardarImagen(MultipartFile fotoPerfil) throws IOException {
        // Ejemplo: Guardar la imagen en el sistema de archivos local
        String ruta = "uploads/" + fotoPerfil.getOriginalFilename();
        Path path = Paths.get(ruta);
        Files.createDirectories(path.getParent());
        Files.write(path, fotoPerfil.getBytes());
        return ruta;
    }
}