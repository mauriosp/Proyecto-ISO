package com.apirest.backend.Service;

import com.apirest.backend.Model.CalificacionEspacio;
import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Repository.EspacioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalificacionEspacioServiceImpl implements ICalificacionEspacioService {

    private final EspacioRepository espacioRepository;
    private final IMensajeService notificacionService;

    @Override
    public void calificarEspacio(String usuarioId, String espacioId, int puntuacion, String comentario) {
        
        // Validaciones
        validarParametrosCalificacion(usuarioId, espacioId, puntuacion);
        validarComentario(comentario);
        
        // Buscar el espacio
        Espacio espacio = espacioRepository.findById(espacioId)
                .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
        
        // Verificar que el usuario puede calificar este espacio
        if (!puedeCalificarEspacio(usuarioId, espacioId)) {
            throw new IllegalArgumentException("No tienes permisos para calificar este espacio");
        }
        
        // Crear una nueva calificación
        CalificacionEspacio calificacion = new CalificacionEspacio();
        calificacion.setPuntuacion(puntuacion);
        calificacion.setFecha(new Date());
        calificacion.setComentario(comentario != null ? comentario : "");
        
        // Actualizar el promedio de calificaciones del espacio
        actualizarPromedioCalificacionesEspacio(espacioId);
        
        // Obtener el ID del propietario del espacio
        String propietarioId = espacio.getIdPropietario().toHexString();
        
        // Buscar el aviso asociado a este espacio o crear un ID si no existe
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
    
    // Métodos auxiliares privados (que antes estaban como @Override)
    
    private void actualizarPromedioCalificacionesEspacio(String espacioId) {
        try {
            Espacio espacio = espacioRepository.findById(espacioId)
                    .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
            
            // TODO: Calcular el promedio real basado en las calificaciones almacenadas
            // Por ahora, incrementamos ligeramente el promedio si es 0, o lo mantenemos
            if (espacio.getPromCalificacion() == 0) {
                espacio.setPromCalificacion(4); // Valor inicial
            }
            
            espacioRepository.save(espacio);
            
            log.info("Promedio de calificaciones actualizado para espacio {}: {}", 
                    espacioId, espacio.getPromCalificacion());
        } catch (Exception e) {
            log.error("Error al actualizar promedio de calificaciones para espacio {}: {}", 
                     espacioId, e.getMessage());
        }
    }
    
    private boolean puedeCalificarEspacio(String usuarioId, String espacioId) {
        // TODO: Implementar validación con el sistema de arrendamientos
        // Debería verificar que:
        // 1. El usuario tuvo un arrendamiento en este espacio
        // 2. El arrendamiento ya finalizó
        // 3. No ha calificado ya este espacio para este arrendamiento
        
        log.info("Validando si usuario {} puede calificar espacio {}", usuarioId, espacioId);
        
        return true; // Implementación temporal
    }
    
    private String obtenerAvisoIdPorEspacio(String espacioId) {
        // TODO: Implementar lógica para obtener el ID del aviso asociado al espacio
        // Por ahora retorna el mismo espacioId como dummy
        return espacioId;
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
}