package com.apirest.backend.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.apirest.backend.Model.Arrendamiento;
import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Model.CalificacionEspacio;
import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Repository.AvisoRepository;
import com.apirest.backend.Repository.EspacioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalificacionEspacioServiceImpl implements ICalificacionEspacioService {

    private final EspacioRepository espacioRepository;
    private final AvisoRepository avisoRepository;
    private final IMensajeService notificacionService;

    @Override
    public void calificarEspacio(String usuarioId, String espacioId, int puntuacion, String comentario) {
        
        // Validaciones
        validarParametrosCalificacion(usuarioId, espacioId, puntuacion);
        validarComentario(comentario);
        
        // Buscar el espacio
        Espacio espacio = espacioRepository.findById(espacioId)
                .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
        
        // VALIDACIÓN DE FINALIZACIÓN Y RESTRICCIÓN TEMPORAL
        if (!puedeCalificarEspacio(usuarioId, espacioId)) {
            throw new IllegalArgumentException("No tienes permisos para calificar este espacio. Solo puedes calificar espacios donde hayas tenido un arrendamiento completado.");
        }
        
        // PREVENCIÓN DE DUPLICADOS - Verificar que no haya calificado ya
        if (yaCalificoEsteEspacio(usuarioId, espacioId)) {
            throw new IllegalArgumentException("Ya has calificado este espacio para tu arrendamiento.");
        }
        
        // Crear una nueva calificación
        CalificacionEspacio calificacion = new CalificacionEspacio();
        calificacion.setPuntuacion(puntuacion);
        calificacion.setFecha(new Date());
        calificacion.setComentario(comentario != null ? comentario : "");
        
        // Buscar el arrendamiento del usuario para agregar la calificación
        Arrendamiento arrendamientoUsuario = encontrarArrendamientoUsuario(espacio, usuarioId);
        if (arrendamientoUsuario != null) {
            arrendamientoUsuario.setCalificacionEspacio(calificacion);
            espacioRepository.save(espacio);
        }
        
        // Actualizar el promedio de calificaciones del espacio
        actualizarPromedioCalificacionesEspacio(espacioId);
        
        // Obtener el ID del propietario del espacio
        String propietarioId = espacio.getIdPropietario().toHexString();
        
        // Buscar el aviso asociado a este espacio
        String avisoId = obtenerAvisoIdPorEspacio(espacioId);
        
        // Enviar notificación de calificación
        notificacionService.notificarNuevaCalificacion(propietarioId, espacioId, puntuacion, avisoId);
        
        // Si hay comentario, enviar notificación específica
        if (comentario != null && !comentario.trim().isEmpty()) {
            notificacionService.notificarNuevoComentario(propietarioId, comentario, avisoId);
        }
        
        log.info("Calificación registrada: espacio={}, usuario={}, puntuación={}", 
                espacioId, usuarioId, puntuacion);
    }
        
    private void actualizarPromedioCalificacionesEspacio(String espacioId) {
        try {
            Espacio espacio = espacioRepository.findById(espacioId)
                    .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
            
            // Calcular el promedio real basado en las calificaciones almacenadas
            if (espacio.getArrendamiento() != null && !espacio.getArrendamiento().isEmpty()) {
                int totalCalificaciones = 0;
                int sumaCalificaciones = 0;
                
                for (Arrendamiento arrendamiento : espacio.getArrendamiento()) {
                    if (arrendamiento.getCalificacionEspacio() != null) {
                        sumaCalificaciones += arrendamiento.getCalificacionEspacio().getPuntuacion();
                        totalCalificaciones++;
                    }
                }
                
                if (totalCalificaciones > 0) {
                    int promedioCalculado = Math.round((float) sumaCalificaciones / totalCalificaciones);
                    espacio.setPromCalificacion(promedioCalculado);
                } else {
                    espacio.setPromCalificacion(0);
                }
                
                espacioRepository.save(espacio);
                
                log.info("Promedio de calificaciones actualizado para espacio {}: {} (basado en {} calificaciones)", 
                        espacioId, espacio.getPromCalificacion(), totalCalificaciones);
            } else {
                log.debug("No hay arrendamientos con calificaciones para el espacio {}", espacioId);
            }
            
        } catch (Exception e) {
            log.error("Error al actualizar promedio de calificaciones para espacio {}: {}", 
                     espacioId, e.getMessage());
        }
    }
    
    /**
     * VALIDACIÓN DE FINALIZACIÓN Y RESTRICCIÓN TEMPORAL MEJORADA
     */
    private boolean puedeCalificarEspacio(String usuarioId, String espacioId) {
        try {
            Optional<Espacio> espacioOpt = espacioRepository.findById(espacioId);
            if (espacioOpt.isEmpty()) {
                return false;
            }
            
            Espacio espacio = espacioOpt.get();
            
            // Verificar que el usuario tuvo un arrendamiento en este espacio
            if (espacio.getArrendamiento() != null) {
                for (Arrendamiento arrendamiento : espacio.getArrendamiento()) {
                    if (arrendamiento.getIdUsuario().toHexString().equals(usuarioId)) {
                        
                        // VALIDACIÓN DE FINALIZACIÓN: Verificar que el arrendamiento está completado
                        if (!"Completado".equals(arrendamiento.getEstado())) {
                            log.debug("Usuario {} no puede calificar espacio {} - arrendamiento no completado (estado: {})", 
                                    usuarioId, espacioId, arrendamiento.getEstado());
                            return false;
                        }
                        
                        // RESTRICCIÓN TEMPORAL: Verificar que la fecha de salida ya pasó
                        Date fechaSalida = arrendamiento.getFechaSalida();
                        Date ahora = new Date();
                        
                        if (ahora.before(fechaSalida)) {
                            log.debug("Usuario {} no puede calificar espacio {} - arrendamiento aún no ha terminado", 
                                    usuarioId, espacioId);
                            return false;
                        }
                        
                        // RESTRICCIÓN TEMPORAL: Verificar ventana de calificación (máximo 30 días)
                        long treintaDiasEnMillis = 30L * 24L * 60L * 60L * 1000L;
                        long tiempoTranscurrido = ahora.getTime() - fechaSalida.getTime();
                        
                        if (tiempoTranscurrido > treintaDiasEnMillis) {
                            log.debug("Usuario {} no puede calificar espacio {} - período de calificación expirado", 
                                    usuarioId, espacioId);
                            return false;
                        }
                        
                        log.debug("Usuario {} puede calificar espacio {} - arrendamiento completado y en ventana de tiempo válida", 
                                usuarioId, espacioId);
                        return true;
                    }
                }
            }
            
            log.debug("Usuario {} no puede calificar espacio {} - no tiene arrendamientos completados", 
                    usuarioId, espacioId);
            return false;
            
        } catch (Exception e) {
            log.error("Error al verificar permisos de calificación: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * PREVENCIÓN DE DUPLICADOS 
     */
    private boolean yaCalificoEsteEspacio(String usuarioId, String espacioId) {
        try {
            Optional<Espacio> espacioOpt = espacioRepository.findById(espacioId);
            if (espacioOpt.isEmpty()) {
                return false;
            }
            
            Espacio espacio = espacioOpt.get();
            
            if (espacio.getArrendamiento() != null) {
                for (Arrendamiento arrendamiento : espacio.getArrendamiento()) {
                    if (arrendamiento.getIdUsuario().toHexString().equals(usuarioId)) {
                        // Verificar si ya tiene calificación para este espacio
                        if (arrendamiento.getCalificacionEspacio() != null) {
                            log.debug("Usuario {} ya calificó el espacio {}", usuarioId, espacioId);
                            return true;
                        }
                    }
                }
            }
            
            return false;
            
        } catch (Exception e) {
            log.error("Error al verificar si ya calificó el espacio: {}", e.getMessage());
            return false;
        }
    }
    
    private String obtenerAvisoIdPorEspacio(String espacioId) {
        try {
            List<Aviso> avisos = avisoRepository.findByIdEspacio(new org.bson.types.ObjectId(espacioId));
            
            if (!avisos.isEmpty()) {
                // Buscar el aviso más reciente o activo
                Optional<Aviso> avisoActivo = avisos.stream()
                        .filter(aviso -> "Activo".equals(aviso.getEstado()))
                        .findFirst();
                
                if (avisoActivo.isPresent()) {
                    String avisoId = avisoActivo.get().getId().toHexString();
                    log.debug("Aviso activo encontrado para espacio {}: {}", espacioId, avisoId);
                    return avisoId;
                } else {
                    // Si no hay avisos activos, usar el primer aviso disponible
                    String avisoId = avisos.get(0).getId().toHexString();
                    log.debug("Usando primer aviso disponible para espacio {}: {}", espacioId, avisoId);
                    return avisoId;
                }
            } else {
                log.warn("No se encontraron avisos para el espacio {}, usando ID del espacio como fallback", espacioId);
                return espacioId; // Fallback al ID del espacio
            }
            
        } catch (Exception e) {
            log.error("Error al obtener aviso para espacio {}: {}", espacioId, e.getMessage());
            return espacioId; // Fallback en caso de error
        }
    }
    
    private Arrendamiento encontrarArrendamientoUsuario(Espacio espacio, String usuarioId) {
        if (espacio.getArrendamiento() != null) {
            return espacio.getArrendamiento().stream()
                    .filter(arr -> arr.getIdUsuario().toHexString().equals(usuarioId))
                    .filter(arr -> "Completado".equals(arr.getEstado()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
    
    private void validarParametrosCalificacion(String usuarioId, String espacioId, int puntuacion) {
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede estar vacío");
        }
        
        if (espacioId == null || espacioId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del espacio no puede estar vacío");
        }
        
        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }
    }
    
    private void validarComentario(String comentario) {
        if (comentario != null && comentario.length() > 300) {
            throw new IllegalArgumentException("El comentario no puede exceder los 300 caracteres");
        }
    }
        
    /**
     * Obtiene todas las calificaciones de un espacio
     */
    public List<CalificacionEspacio> obtenerCalificacionesEspacio(String espacioId) {
        Optional<Espacio> espacioOpt = espacioRepository.findById(espacioId);
        if (espacioOpt.isPresent() && espacioOpt.get().getArrendamiento() != null) {
            return espacioOpt.get().getArrendamiento().stream()
                    .filter(arr -> arr.getCalificacionEspacio() != null)
                    .map(Arrendamiento::getCalificacionEspacio)
                    .toList();
        }
        return List.of();
    }
    
    /**
     * Verifica si un usuario puede calificar un espacio específico
     */
    public boolean verificarPermisosCalificacion(String usuarioId, String espacioId) {
        return puedeCalificarEspacio(usuarioId, espacioId);
    }
    
    /**
     * Obtiene el promedio de calificaciones de un espacio
     */
    public double obtenerPromedioCalificaciones(String espacioId) {
        List<CalificacionEspacio> calificaciones = obtenerCalificacionesEspacio(espacioId);
        if (calificaciones.isEmpty()) {
            return 0.0;
        }
        
        double suma = calificaciones.stream()
                .mapToInt(CalificacionEspacio::getPuntuacion)
                .sum();
        
        return suma / calificaciones.size();
    }
}