package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Repository.AvisoRepository;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Repository.EspacioRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import org.bson.types.ObjectId;

@Service
public class AvisoServiceImpl implements IAvisoService {

    @Autowired
    private AvisoRepository avisoRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EspacioRepository espacioRepository;

    @Override
    public List<Aviso> listarAvisos() {
        return avisoRepository.findAll();
    }

    @Override
    public void crearAviso(String descripcion, double precioMensual, List<MultipartFile> imagenes, String titulo, String tipoEspacio, String caracteristicas, String direccion, BigDecimal area, ObjectId idUsuario) throws Exception {
        // Validar el título
        if (titulo.length() > 100) {
            throw new IllegalArgumentException("El título no puede exceder los 100 caracteres.");
        }

        // Validar la descripción
        if (descripcion.length() > 500) {
            throw new IllegalArgumentException("La descripción no puede exceder los 500 caracteres.");
        }

        // Validar el precio mensual
        if (precioMensual <= 0) {
            throw new IllegalArgumentException("El precio mensual debe ser un valor numérico positivo.");
        }

        // Validar las imágenes
        for (MultipartFile imagen : imagenes) {
            String contentType = imagen.getContentType();
            if (!List.of("image/jpeg", "image/png").contains(contentType)) {
                throw new IllegalArgumentException("Formato de imagen no permitido. Solo se permiten PNG y JPG.");
            }
            if (imagen.getSize() > 5 * 1024 * 1024) { // 5MB
                throw new IllegalArgumentException("El tamaño de la imagen no debe exceder los 5MB.");
            }
        }

        // Guardar las imágenes
        List<String> rutasImagenes = guardarImagenes(imagenes);

        // Crear un nuevo espacio
        Espacio nuevoEspacio = new Espacio();
        nuevoEspacio.setIdUsuario(idUsuario);
        nuevoEspacio.setTipoEspacio(tipoEspacio);
        nuevoEspacio.setCaracteristicas(caracteristicas);
        nuevoEspacio.setDireccion(direccion);
        nuevoEspacio.setArea(area);
        nuevoEspacio.setEstado("Disponible");
        Espacio espacio = espacioRepository.save(nuevoEspacio);

        // Crear el aviso
        Aviso aviso = new Aviso();
        aviso.setDescripcion(descripcion);
        aviso.setPrecio(new Decimal128(BigDecimal.valueOf(precioMensual)));
        aviso.setImagenes(String.join(",", rutasImagenes));
        aviso.setTitulo(titulo);
        aviso.setEstado("Disponible");
        aviso.setEspacioId(espacio.getId());

        // Guardar el aviso en la base de datos
        avisoRepository.save(aviso);

        // Notificar al administrador
        notificationService.enviarNotificacionAdministrador("Nuevo aviso creado", "Se ha creado un nuevo aviso con el título: " + titulo);

        // Notificar al propietario
        notificationService.enviarNotificacionPropietario("Aviso creado exitosamente", "Tu aviso con el título '" + titulo + "' ha sido creado y está disponible.");
    }

    private List<String> guardarImagenes(List<MultipartFile> imagenes) throws IOException {
        List<String> rutas = new ArrayList<>();
        for (MultipartFile imagen : imagenes) {
            String ruta = "uploads/" + imagen.getOriginalFilename();
            Path path = Paths.get(ruta);
            Files.createDirectories(path.getParent());
            Files.write(path, imagen.getBytes());
            rutas.add(ruta);
        }
        return rutas;
    }
}
