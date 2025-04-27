package com.apirest.backend.Service;

import com.apirest.backend.Exception.ImagenException;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FotoPerfilServiceImpl implements IFotoPerfilService {
    private final UsuarioRepository usuarioRepository;

    @Value("${app.upload.dir}")
    private String directorioCargas;

    // Tipos de imagen permitidos para foto de perfil
    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList(
        "image/jpeg", 
        "image/png", 
        "image/gif", 
        "image/webp"
    );

    // Tamaño máximo de imagen de perfil: 2MB
    private static final long TAMANO_MAXIMO = 2 * 1024 * 1024;

    @Override
    public Usuario actualizarFotoPerfil(String usuarioId, MultipartFile archivo) {
        // Validar tipo de archivo
        validarTipoArchivo(archivo);

        // Validar tamaño de archivo
        validarTamanoArchivo(archivo);

        try {
            // Buscar usuario
            Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ImagenException("Usuario no encontrado"));

            // Generar nombre de archivo único
            String nombreArchivo = generarNombreUnico(archivo);
            
            // Crear ruta de almacenamiento
            Path rutaDestino = prepararRutaAlmacenamiento(nombreArchivo);
            
            // Copiar archivo
            Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            // Eliminar foto de perfil anterior si existe
            if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isEmpty()) {
                eliminarFotoPerfilAnterior(usuario.getFotoPerfil());
            }

            // Actualizar foto de perfil
            usuario.setFotoPerfil(nombreArchivo);

            // Guardar usuario actualizado
            return usuarioRepository.save(usuario);

        } catch (IOException e) {
            throw new ImagenException.AlmacenamientoException("Error al guardar la foto de perfil: " + e.getMessage());
        }
    }

    @Override
    public Usuario eliminarFotoPerfil(String usuarioId) {
        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ImagenException("Usuario no encontrado"));

        // Verificar si hay foto de perfil
        if (usuario.getFotoPerfil() == null || usuario.getFotoPerfil().isEmpty()) {
            throw new ImagenException("No hay foto de perfil para eliminar");
        }

        try {
            // Eliminar archivo físico
            eliminarFotoPerfilAnterior(usuario.getFotoPerfil());

            // Limpiar referencia de foto de perfil
            usuario.setFotoPerfil(null);

            // Guardar usuario actualizado
            return usuarioRepository.save(usuario);

        } catch (IOException e) {
            throw new ImagenException.AlmacenamientoException(
                "Error al eliminar la foto de perfil: " + e.getMessage()
            );
        }
    }

    // Métodos de validación
    private void validarTipoArchivo(MultipartFile archivo) {
        if (archivo.isEmpty()) {
            throw new ImagenException.TipoInvalidoException("El archivo está vacío");
        }

        String tipoContenido = archivo.getContentType();
        if (!TIPOS_PERMITIDOS.contains(tipoContenido)) {
            throw new ImagenException.TipoInvalidoException(
                "Tipo de archivo no permitido. Tipos válidos: " + TIPOS_PERMITIDOS
            );
        }
    }

    private void validarTamanoArchivo(MultipartFile archivo) {
        if (archivo.getSize() > TAMANO_MAXIMO) {
            throw new ImagenException.TamanoExcedidoException(
                "El tamaño máximo permitido es de 2MB"
            );
        }
    }

    // Métodos de generación y almacenamiento
    private String generarNombreUnico(MultipartFile archivo) {
        String extension = obtenerExtension(archivo.getOriginalFilename());
        return "perfil_" + UUID.randomUUID().toString() + "." + extension;
    }

    private String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        return (ultimoPunto == -1) ? "" : nombreArchivo.substring(ultimoPunto + 1);
    }

    private Path prepararRutaAlmacenamiento(String nombreArchivo) throws IOException {
        Path directorioPath = Paths.get(directorioCargas, "perfiles").toAbsolutePath().normalize();
        
        // Crear directorio si no existe
        Files.createDirectories(directorioPath);

        return directorioPath.resolve(nombreArchivo).normalize();
    }

    // Método para eliminar foto de perfil anterior
    private void eliminarFotoPerfilAnterior(String nombreArchivo) throws IOException {
        Path rutaImagen = prepararRutaAlmacenamiento(nombreArchivo);
        Files.deleteIfExists(rutaImagen);
    }
}