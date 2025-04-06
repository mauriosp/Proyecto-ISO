package com.apirest.backend.Service;

import com.apirest.backend.Model.CalificacionEspacio;
import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Repository.CalificacionEspacioRepository;
import com.apirest.backend.Repository.EspacioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalificacionEspacioServiceImpl implements ICalificacionEspacioService {

    private final CalificacionEspacioRepository calificacionRepository;
    private final EspacioRepository espacioRepository;
    private final INotificacionService notificacionService;

    @Override
    public void calificarEspacio(String usuarioId, String espacioId, int puntuacion, String comentario) {
        ObjectId objUsuarioId = new ObjectId(usuarioId);
        ObjectId objEspacioId = new ObjectId(espacioId);
        
        // Guardar calificación
        CalificacionEspacio calificacion = new CalificacionEspacio();
        
        // Verifica los métodos disponibles en la clase CalificacionEspacio
        // Si no existen estos setters, tendrás que ajustar tu clase CalificacionEspacio
        // o establecer los valores de otra manera
        calificacion.setUsuarioId(objUsuarioId);
        calificacion.setEspacioId(objEspacioId);
        calificacion.setPuntuacion(puntuacion);
        calificacion.setComentario(comentario);
        
        calificacionRepository.save(calificacion);
        
        // Obtener espacio y su aviso asociado
        Espacio espacio = espacioRepository.findById(espacioId)
                .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
        
        // Asegúrate de que el método getIdPropietario exista en tu clase Espacio
        // Si no existe, deberás ajustar esto para obtener el ID del propietario
        ObjectId propietarioId = espacio.getIdPropietario();
        
        // Buscar el aviso asociado a este espacio
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
        return new ObjectId();
    }
}