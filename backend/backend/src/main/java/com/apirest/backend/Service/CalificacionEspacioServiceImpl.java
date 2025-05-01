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
        ObjectId objUsuarioId = new ObjectId(usuarioId);
        ObjectId objEspacioId = new ObjectId(espacioId);
        
        // Buscar el espacio
        Espacio espacio = espacioRepository.findById(espacioId)
                .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
        
        // Crear una nueva calificación
        CalificacionEspacio calificacion = new CalificacionEspacio();
        calificacion.setIdUsuarioCalifica(objUsuarioId);
        calificacion.setPuntuacion(puntuacion);
        calificacion.setFecha(new Date());
        calificacion.setComentario(comentario);
        
        // Aquí necesitarías agregar esta calificación al arrendamiento correspondiente
        // Esto dependerá de cómo manejas las relaciones entre Espacio, Arrendamiento y CalificacionEspacio
        // Por ejemplo, podrías buscar el arrendamiento por usuario y espacio, y luego añadir la calificación
        
        // Obtenemos el ID del propietario del espacio
        ObjectId propietarioId = espacio.getIdPropietario();
        
        // Buscar el aviso asociado a este espacio o crear un ID si no existe
        ObjectId avisoId = obtenerAvisoIdPorEspacio(objEspacioId);
        
        // Enviar notificación de calificación
        notificacionService.notificarNuevaCalificacion(propietarioId, objEspacioId, puntuacion, avisoId);
        
        // Si hay comentario, enviar notificación específica
        if (comentario != null && !comentario.trim().isEmpty()) {
            notificacionService.notificarNuevoComentario(propietarioId, comentario, avisoId);
        }
        
        log.info("Calificación registrada: espacio={}, usuario={}, puntuación={}", 
                espacioId, usuarioId, puntuacion);
    }
    
    private ObjectId obtenerAvisoIdPorEspacio(ObjectId espacioId) {
        // Aquí implementarías la lógica para obtener el ID del aviso
        // Por ahora retorna un ID dummy
        return espacioId; // Cambia esto por la lógica real
    }
}