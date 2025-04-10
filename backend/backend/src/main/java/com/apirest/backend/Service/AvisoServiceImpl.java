package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Repository.AvisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Repository.EspacioRepository;
import com.apirest.backend.Service.IEspacioService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import org.bson.types.ObjectId;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvisoServiceImpl implements IAvisoService {

    @Autowired
    private AvisoRepository avisoRepository;

    @Autowired
    private INotificacionService notificacionService;

    @Autowired
    private IEspacioService espacioService;

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    public List<Aviso> listarAvisos() {
        return avisoRepository.findAll();
    }

    @Override
    public void crearAviso(String descripcion, double precioMensual, List<MultipartFile> imagenes, String titulo, String tipoEspacio, String caracteristicas, String direccion, BigDecimal area, ObjectId idUsuario) throws Exception {
        // Validar que el usuario exista
        if (!usuarioService.existeUsuarioPorId(idUsuario)) {
            throw new IllegalArgumentException("El usuario con el ID proporcionado no existe.");
        }

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

        // Verificar si el usuario ya tiene un espacio con la misma dirección
        Optional<Espacio> espacioExistente = espacioService.buscarEspacioPorDireccionYPropietario(direccion, idUsuario);
        if (espacioExistente.isPresent()) {
            // Eliminar el espacio existente
            espacioService.eliminarEspacio(espacioExistente.get().getId().toString());
        }

        // Crear un nuevo espacio
        Espacio nuevoEspacio = espacioService.crearEspacio(idUsuario, tipoEspacio, caracteristicas, direccion, area);

        // Validar que el espacio haya sido creado correctamente
        if (nuevoEspacio == null || nuevoEspacio.getId() == null) {
            throw new IllegalStateException("No se pudo crear el espacio. Por favor, intente nuevamente.");
        }

        // Crear el aviso
        Aviso aviso = new Aviso();
        aviso.setDescripcion(descripcion);
        aviso.setPrecio((int) precioMensual);
        aviso.setImagenes(String.join(",", rutasImagenes));
        aviso.setTitulo(titulo);
        aviso.setEstado("Activo"); // Cambiar a "Activo"
        aviso.setFechaPublicacion(new Date()); // Añadir fecha de publicación
        aviso.setIdPropietario(nuevoEspacio.getIdPropietario());

        // Guardar el aviso en la base de datos
        avisoRepository.save(aviso);

        // Notificar al administrador
        notificacionService.enviarNotificacionAdministrador("Nuevo aviso creado", "Se ha creado un nuevo aviso con el título: " + titulo);

        // Notificar al propietario
        notificacionService.enviarNotificacionPropietario("Aviso creado exitosamente", "Tu aviso con el título '" + titulo + "' ha sido creado y está disponible.");
    }

    @Override
    public void editarAviso(String id, String titulo, String descripcion, Double precioMensual, List<MultipartFile> imagenes, String estado) throws Exception {
        // Buscar el aviso por ID
        Aviso aviso = avisoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));

        // Validar el título
        if (titulo != null && titulo.length() > 100) {
            throw new IllegalArgumentException("El título no puede exceder los 100 caracteres.");
        }

        // Validar la descripción
        if (descripcion != null && descripcion.length() > 500) {
            throw new IllegalArgumentException("La descripción no puede exceder los 500 caracteres.");
        }

        // Validar el precio mensual
        if (precioMensual != null && precioMensual <= 0) {
            throw new IllegalArgumentException("El precio mensual debe ser un valor numérico positivo.");
        }

        // Validar las imágenes
        if (imagenes != null) {
            for (MultipartFile imagen : imagenes) {
                String contentType = imagen.getContentType();
                if (!List.of("image/jpeg", "image/png").contains(contentType)) {
                    throw new IllegalArgumentException("Formato de imagen no permitido. Solo se permiten PNG y JPG.");
                }
                if (imagen.getSize() > 5 * 1024 * 1024) { // 5MB
                    throw new IllegalArgumentException("El tamaño de la imagen no debe exceder los 5MB.");
                }
            }
            // Guardar las nuevas imágenes
            List<String> rutasImagenes = guardarImagenes(imagenes);
            aviso.setImagenes(String.join(",", rutasImagenes));
        }

        // Actualizar los campos del aviso
        if (titulo != null) aviso.setTitulo(titulo);
        if (descripcion != null) aviso.setDescripcion(descripcion);
        if (precioMensual != null) {
            aviso.setPrecio((int) precioMensual.doubleValue());
        }
        if (estado != null) {
            // Asegurarse de que estado sea un valor válido
            if (!List.of("Activo", "Inactivo").contains(estado)) {
                throw new IllegalArgumentException("Estado no válido. Valores permitidos: Activo, Inactivo");
            }
            aviso.setEstado(estado);
        }

        // Guardar los cambios en la base de datos
        avisoRepository.save(aviso);
    }

    @Override
    public void eliminarAviso(String id) throws Exception {
        Aviso aviso = avisoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));

        avisoRepository.delete(aviso);
    }

    @Override
    public List<Aviso> listarAvisosParaModeracion() {
        return avisoRepository.findAll().stream()
                .filter(aviso -> aviso.getEstado().equalsIgnoreCase("Activo"))
                .collect(Collectors.toList());
    }

    @Override
    public void desactivarAviso(String id, String motivo) throws Exception {
        // Buscar el aviso por ID
        Aviso aviso = avisoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));

        // Cambiar el estado del aviso a "Inactivo"
        aviso.setEstado("Inactivo");
        aviso.setMotivoDesactivacion(motivo);

        // Guardar los cambios
        avisoRepository.save(aviso);

    }

    @Override
    public void reactivarAviso(String id) throws Exception {
        // Buscar el aviso por ID
        Aviso aviso = avisoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));

        // Cambiar el estado del aviso a "Activo"
        aviso.setEstado("Activo");  // Cambiar "Publicado" a "Activo"
        aviso.setMotivoDesactivacion(null); // Limpiar el motivo de desactivación

        // Guardar los cambios
        avisoRepository.save(aviso);
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

    @Override
    public void eliminarAvisosPorPropietario(ObjectId idPropietario) {
        List<Aviso> avisos = avisoRepository.findByIdPropietario(idPropietario);
        avisoRepository.deleteAll(avisos);
    }
}