package com.apirest.backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Service.ICalificacionEspacioService;
import com.apirest.backend.Service.ICalificacionUsuarioService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/UAO/apirest/Calificaciones")
@CrossOrigin(origins = "*")
@Slf4j

public class CalificacionController {
    @Autowired
    private ICalificacionEspacioService calificacionEspacioService;
    @Autowired
    private ICalificacionUsuarioService calificacionUsuarioService;
    
    @PostMapping("/calificar-arrendatario")
    public ResponseEntity<?> calificarArrendatario(
        @RequestParam String idPropietario,
        @RequestParam String idArrendatario,
        @RequestParam int puntuacion,
        @RequestParam (required = false) String comentario,
        @RequestParam String idArrendamiento) {

        try {
            // Validar puntuación
            if (puntuacion < 1 || puntuacion > 5) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("La puntuación debe estar entre 1 y 5 estrellas"));
            }
            
            // Validar longitud del comentario
            if (comentario != null && comentario.length() > 300) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("El comentario no puede exceder los 300 caracteres"));
            }
            
            // Calificar al arrendatario
            calificacionUsuarioService.calificarArrendatario(
                idPropietario, idArrendatario, puntuacion, comentario, idArrendamiento);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Calificación del arrendatario registrada exitosamente");
            respuesta.put("puntuacion", puntuacion);
            respuesta.put("arrendatario", idArrendatario);
            
            log.info("Calificación de arrendatario registrada: propietario={}, arrendatario={}, puntuación={}", 
                    idPropietario, idArrendatario, puntuacion);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al calificar arrendatario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al calificar arrendatario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }

    @PostMapping("/calificar-propietario")
    public ResponseEntity<?> calificarPropietario(
            @RequestParam String idArrendatario,
            @RequestParam String idPropietario,
            @RequestParam String idEspacio,
            @RequestParam int puntuacion,
            @RequestParam(required = false) String comentario,
            @RequestParam String idArrendamiento) {
        
        try {
            // Validar puntuación
            if (puntuacion < 1 || puntuacion > 5) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("La puntuación debe estar entre 1 y 5 estrellas"));
            }
            
            // Validar longitud del comentario
            if (comentario != null && comentario.length() > 300) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("El comentario no puede exceder los 300 caracteres"));
            }
            
            // Calificar al propietario
            calificacionUsuarioService.calificarPropietario(
                idArrendatario, idPropietario, puntuacion, comentario, idArrendamiento);
            
            // Si hay comentario específico sobre el espacio, también calificar el espacio
            if (comentario != null && !comentario.trim().isEmpty()) {
                calificacionEspacioService.calificarEspacio(idArrendatario, idEspacio, puntuacion, comentario);
            }
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Calificación del propietario registrada exitosamente");
            respuesta.put("puntuacion", puntuacion);
            respuesta.put("propietario", idPropietario);
            if (comentario != null && !comentario.trim().isEmpty()) {
                respuesta.put("espacioCalificado", true);
            }
            
            log.info("Calificación de propietario registrada: arrendatario={}, propietario={}, puntuación={}", 
                    idArrendatario, idPropietario, puntuacion);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al calificar propietario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al calificar propietario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }


    @PostMapping("/calificar-espacio")
    public ResponseEntity<?> calificarEspacio(
            @RequestParam String idUsuario,
            @RequestParam String idEspacio,
            @RequestParam int puntuacion,
            @RequestParam(required = false) String comentario) {
        
        try {
            // Validar puntuación
            if (puntuacion < 1 || puntuacion > 5) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("La puntuación debe estar entre 1 y 5 estrellas"));
            }
            
            // Validar longitud del comentario
            if (comentario != null && comentario.length() > 300) {
                return ResponseEntity.badRequest()
                    .body(crearRespuestaError("El comentario no puede exceder los 300 caracteres"));
            }
            
            // Calificar el espacio
            calificacionEspacioService.calificarEspacio(idUsuario, idEspacio, puntuacion, comentario);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Calificación del espacio registrada exitosamente");
            respuesta.put("puntuacion", puntuacion);
            respuesta.put("espacio", idEspacio);
            
            log.info("Calificación de espacio registrada: usuario={}, espacio={}, puntuación={}", 
                    idUsuario, idEspacio, puntuacion);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al calificar espacio: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al calificar espacio: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }

    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", mensaje);
        error.put("timestamp", new java.util.Date());
        return error;
    }
}
