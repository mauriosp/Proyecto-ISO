package com.apirest.backend.Service;

import com.apirest.backend.Model.CalificacionEspacio;
import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Repository.EspacioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
        
        // Buscar el espacio
        Espacio espacio = espacioRepository.findById(espacioId)
                .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
        
        // Crear una nueva calificación
        CalificacionEspacio calificacion = new CalificacionEspacio();
        calificacion.setIdUsuarioCalifica(new ObjectId(usuarioId)); // Convertir String a ObjectId
        calificacion.setPuntuacion(puntuacion);
        calificacion.setFecha(new Date());
        calificacion.setComentario(comentario);
        
        // Obtenemos el ID del propietario del espacio
        String propietarioId = espacio.getIdPropietario().toHexString(); // Convertir ObjectId a String
        
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
    
    private String obtenerAvisoIdPorEspacio(String espacioId) {
        // Aquí implementarías la lógica para obtener el ID del aviso
        // Por ahora retorna el mismo espacioId como dummy
        return espacioId; // Cambia esto por la lógica real
    }
}