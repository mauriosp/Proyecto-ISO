package com.apirest.backend.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Repository.AvisoRepository;
import com.apirest.backend.Repository.EspacioRepository;

@Service
public class AvisoServiceImpl implements IAvisoService {

    @Autowired
    private AvisoRepository avisoRepository;

    @Autowired
    private IMensajeService mensajeService;

    @Autowired
    private IEspacioService espacioService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private EspacioRepository espacioRepository;

    @Override
    public List<Aviso> listarAvisos() {
        return avisoRepository.findAll();
    }

    @Override
    public void crearAviso(String descripcion, double precioMensual, List<String> imagenes, 
                      String titulo, String tipoEspacio, int habitaciones, int baños, 
                      String direccion, BigDecimal area, String idUsuario) throws Exception {
        // Convertir String a ObjectId
        ObjectId idUsuarioObj = new ObjectId(idUsuario);
        
        // Validar que el usuario exista
        if (!usuarioService.existeUsuarioPorId(idUsuarioObj)) {
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

        // Validar las imágenes (ahora son URLs strings)
        if (imagenes != null && !imagenes.isEmpty()) {
            for (String imagenUrl : imagenes) {
                if (imagenUrl == null || imagenUrl.trim().isEmpty()) {
                    throw new IllegalArgumentException("La URL de la imagen no puede estar vacía.");
                }
                // Validar formato básico de URL
                if (!imagenUrl.startsWith("http://") && !imagenUrl.startsWith("https://")) {
                    throw new IllegalArgumentException("La URL de la imagen debe comenzar con http:// o https://");
                }
            }
        }

        // Crear el espacio
        Espacio espacio = espacioService.crearEspacio(idUsuarioObj, tipoEspacio, habitaciones, baños, direccion, area);

        // Validar que el espacio haya sido creado o actualizado correctamente
        if (espacio == null || espacio.getId() == null) {
            throw new IllegalStateException("No se pudo crear o actualizar el espacio. Por favor, intente nuevamente.");
        }

        // Crear el aviso
        Aviso aviso = new Aviso();
        aviso.setIdEspacio(espacio.getId()); // Usar el ID del espacio
        aviso.setDescripcion(descripcion);
        aviso.setPrecio((int) Math.round(precioMensual));
        aviso.setImagenes(imagenes != null ? String.join(",", imagenes) : "");
        aviso.setTitulo(titulo);
        aviso.setEstado("Activo");
        aviso.setFechaPublicacion(new Date());
        aviso.setMensaje(new ArrayList<>());
        aviso.setExtraInfo(new ArrayList<>());

        // Guardar el aviso en la base de datos
        avisoRepository.save(aviso);

        // Notificar al administrador
        mensajeService.enviarMensajeAdministrador("Nuevo aviso creado", "Se ha creado un nuevo aviso con el título: " + titulo);

        // Notificar al propietario
        mensajeService.enviarMensajePropietario("Aviso creado exitosamente", "Tu aviso con el título '" + titulo + "' ha sido creado y está disponible.");
    }

    @Override
    public void editarAviso(String id, String titulo, String descripcion, Double precioMensual, 
                        List<String> imagenes, String estado) throws Exception {
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

        // Validar las imágenes (ahora son URLs strings)
        if (imagenes != null && !imagenes.isEmpty()) {
            for (String imagenUrl : imagenes) {
                if (imagenUrl == null || imagenUrl.trim().isEmpty()) {
                    throw new IllegalArgumentException("La URL de la imagen no puede estar vacía.");
                }
                // Validar formato básico de URL
                if (!imagenUrl.startsWith("http://") && !imagenUrl.startsWith("https://")) {
                    throw new IllegalArgumentException("La URL de la imagen debe comenzar con http:// o https://");
                }
            }
            // Actualizar las imágenes
            aviso.setImagenes(String.join(",", imagenes));
        }

        // Actualizar los campos del aviso
        if (titulo != null) aviso.setTitulo(titulo);
        if (descripcion != null) aviso.setDescripcion(descripcion);
        if (precioMensual != null) {
            aviso.setPrecio((int) Math.round(precioMensual));
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

        // No es necesario actualizar el espacio ya que no existe la relación bidireccional

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

        // Guardar los cambios
        avisoRepository.save(aviso);

        // Notificar al propietario - adaptado para aceptar String en lugar de ObjectId
        if (aviso.getIdEspacio() != null) {
            Espacio espacio = espacioRepository.findById(aviso.getIdEspacio().toHexString())
                .orElse(null);
            if (espacio != null && espacio.getIdPropietario() != null) {
                // Convertir ObjectId a String para la notificación
                String propietarioId = espacio.getIdPropietario().toHexString();
                String avisoId = aviso.getId().toHexString();
                mensajeService.notificarModeracionAviso(propietarioId, avisoId, motivo, "desactivado");
            }
        }
    }

    @Override
    public void reactivarAviso(String id) throws Exception {
        // Buscar el aviso por ID
        Aviso aviso = avisoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));

        // Cambiar el estado del aviso a "Activo"
        aviso.setEstado("Activo");

        // Guardar los cambios
        avisoRepository.save(aviso);

        // Notificar al propietario - adaptado para aceptar String en lugar de ObjectId
        if (aviso.getIdEspacio() != null) {
            Espacio espacio = espacioRepository.findById(aviso.getIdEspacio().toHexString())
                .orElse(null);
            if (espacio != null && espacio.getIdPropietario() != null) {
                // Convertir ObjectId a String para la notificación
                String propietarioId = espacio.getIdPropietario().toHexString();
                String avisoId = aviso.getId().toHexString();
                mensajeService.notificarModeracionAviso(propietarioId, avisoId, "Aviso reactivado", "reactivado");
            }
        }
    }

    
    @Override
    public void eliminarAvisosPorPropietario(String idPropietario) {
        // Convertir String a ObjectId
        ObjectId idPropietarioObj = new ObjectId(idPropietario);
        
        // Obtener todos los espacios y filtrar por propietario
        List<Espacio> espaciosPropietario = espacioRepository.findAll().stream()
            .filter(espacio -> espacio.getIdPropietario() != null && espacio.getIdPropietario().equals(idPropietarioObj))
            .collect(Collectors.toList());
        
        // Luego, buscar y eliminar avisos por cada espacio
        for (Espacio espacio : espaciosPropietario) {
            // Filtrar avisos por idEspacio
            List<Aviso> avisos = avisoRepository.findAll().stream()
                    .filter(aviso -> aviso.getIdEspacio() != null && aviso.getIdEspacio().equals(espacio.getId()))
                    .collect(Collectors.toList());
            
            avisoRepository.deleteAll(avisos);
        }
    }

    @Override
    public List<Aviso> filtrarAvisos(String tipoEspacio, Double precioMin, Double precioMax, String disponibilidad) {
        // Obtener todos los avisos
        List<Aviso> avisos = avisoRepository.findAll();

        // Filtrar avisos por tipo de espacio si se proporciona
        if (tipoEspacio != null && !tipoEspacio.isEmpty()) {
            // Obtener los espacios que coincidan con el tipo de espacio
            List<ObjectId> espaciosFiltrados = espacioRepository.findAll().stream()
                    .filter(espacio -> espacio.getTipoEspacio() != null && espacio.getTipoEspacio().equalsIgnoreCase(tipoEspacio))
                    .map(Espacio::getId)
                    .collect(Collectors.toList());

            // Filtrar los avisos que estén relacionados con los espacios filtrados
            avisos = avisos.stream()
                    .filter(aviso -> aviso.getIdEspacio() != null && espaciosFiltrados.contains(aviso.getIdEspacio()))
                    .collect(Collectors.toList());
        }

        // Filtrar avisos por disponibilidad basada en el estado del espacio si se proporciona
        if (disponibilidad != null && !disponibilidad.isEmpty()) {
            // Obtener los espacios que coincidan con la disponibilidad
            List<ObjectId> espaciosDisponibles = espacioRepository.findAll().stream()
                    .filter(espacio -> espacio.getEstado() != null && espacio.getEstado().equalsIgnoreCase(disponibilidad))
                    .map(Espacio::getId)
                    .collect(Collectors.toList());

            // Filtrar los avisos que estén relacionados con los espacios disponibles
            avisos = avisos.stream()
                    .filter(aviso -> aviso.getIdEspacio() != null && espaciosDisponibles.contains(aviso.getIdEspacio()))
                    .collect(Collectors.toList());
        }

        // Filtrar avisos por rango de precio
        if (precioMin != null) {
            avisos = avisos.stream()
                    .filter(aviso -> aviso.getPrecio() != null && aviso.getPrecio() >= precioMin)
                    .collect(Collectors.toList());
        }

        if (precioMax != null) {
            avisos = avisos.stream()
                    .filter(aviso -> aviso.getPrecio() != null && aviso.getPrecio() <= precioMax)
                    .collect(Collectors.toList());
        }

        return avisos;
    }
}