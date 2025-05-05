package com.apirest.backend.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Repository.AvisoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImagenAvisoServiceImpl implements IImagenAvisoService {
    private final AvisoRepository avisoRepository;

    @Value("${app.upload.dir}")
    private String directorioCargas;

    // Tipos de imagen permitidos
    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList(
        "image/jpeg", 
        "image/png", 
        "image/gif", 
        "image/webp"
    );

    // Tamaño máximo de imagen: 5MB
    private static final long TAMANO_MAXIMO = 5 * 1024 * 1024;

    @Override
    public Aviso agregarImagenAAviso(String avisoId, MultipartFile archivo) {
        // Validar tipo de archivo
        validarTipoArchivo(archivo);

        // Validar tamaño de archivo
        validarTamanoArchivo(archivo);

        try {
            // Buscar aviso
            Aviso aviso = avisoRepository.findById(avisoId)
                .orElseThrow(() -> new RuntimeException("Aviso no encontrado"));

            // Generar nombre de archivo único
            String nombreArchivo = generarNombreUnico(archivo);
            
            // Crear ruta de almacenamiento
            Path rutaDestino = prepararRutaAlmacenamiento(nombreArchivo);
            
            // Copiar archivo
            Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            // Actualizar cadena de imágenes
            String imagenesActuales = aviso.getImagenes();
            String nuevasImagenes = imagenesActuales == null || imagenesActuales.isEmpty()
                ? nombreArchivo
                : imagenesActuales + "," + nombreArchivo;
            
            aviso.setImagenes(nuevasImagenes);

            // Guardar aviso actualizado
            return avisoRepository.save(aviso);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen: " + e.getMessage());
        }
    }

    @Override
    public Aviso eliminarImagenDeAviso(String avisoId, String nombreImagen) {
        // Buscar aviso
        Aviso aviso = avisoRepository.findById(avisoId)
            .orElseThrow(() -> new RuntimeException("Aviso no encontrado"));

        // Obtener lista de imágenes
        String imagenesActuales = aviso.getImagenes();
        if (imagenesActuales == null || imagenesActuales.isEmpty()) {
            throw new RuntimeException("No hay imágenes para eliminar");
        }

        try {
            // Eliminar archivo físico
            Path rutaImagen = prepararRutaAlmacenamiento(nombreImagen);
            Files.deleteIfExists(rutaImagen);

            // Eliminar referencia de imagen
            String[] listaImagenes = imagenesActuales.split(",");
            List<String> imagenesFiltradas = Arrays.stream(listaImagenes)
                .filter(img -> !img.equals(nombreImagen))
                .collect(Collectors.toList());
            
            // Actualizar cadena de imágenes
            aviso.setImagenes(String.join(",", imagenesFiltradas));

            // Guardar aviso actualizado
            return avisoRepository.save(aviso);

        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen: " + e.getMessage());
        }
    }

    // Métodos de validación y utilidad
    private void validarTipoArchivo(MultipartFile archivo) {
        if (archivo.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        String tipoContenido = archivo.getContentType();
        if (!TIPOS_PERMITIDOS.contains(tipoContenido)) {
            throw new RuntimeException(
                "Tipo de archivo no permitido. Tipos válidos: " + TIPOS_PERMITIDOS
            );
        }
    }

    private void validarTamanoArchivo(MultipartFile archivo) {
        if (archivo.getSize() > TAMANO_MAXIMO) {
            throw new RuntimeException("El tamaño máximo permitido es de 5MB");
        }
    }

    private String generarNombreUnico(MultipartFile archivo) {
        String extension = obtenerExtension(archivo.getOriginalFilename());
        return "aviso_" + UUID.randomUUID().toString() + "." + extension;
    }

    private String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        return (ultimoPunto == -1) ? "" : nombreArchivo.substring(ultimoPunto + 1);
    }

    private Path prepararRutaAlmacenamiento(String nombreArchivo) throws IOException {
        Path directorioPath = Paths.get(directorioCargas, "avisos").toAbsolutePath().normalize();
        
        // Crear directorio si no existe
        Files.createDirectories(directorioPath);

        return directorioPath.resolve(nombreArchivo).normalize();
    }
}