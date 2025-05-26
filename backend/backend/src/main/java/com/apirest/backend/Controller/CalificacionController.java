package com.apirest.backend.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Model.CalificacionUsuario;
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
    
    /**
     * Calificar un arrendatario 
     */
    @PostMapping("/calificar-arrendatario")
    public ResponseEntity<?> calificarArrendatario(
        @RequestParam String idPropietario,
        @RequestParam String idArrendatario,
        @RequestParam int puntuacion,
        @RequestParam(required = false) String comentario,
        @RequestParam String idArrendamiento) {

        try {
            // Validaciones de entrada
            validarParametrosBasicos(idPropietario, "idPropietario");
            validarParametrosBasicos(idArrendatario, "idArrendatario");
            validarParametrosBasicos(idArrendamiento, "idArrendamiento");
            validarPuntuacion(puntuacion);
            validarComentario(comentario);
            
            // Calificar al arrendatario
            calificacionUsuarioService.calificarArrendatario(
                idPropietario, idArrendatario, puntuacion, comentario, idArrendamiento);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Calificación del arrendatario registrada exitosamente");
            respuesta.put("puntuacion", puntuacion);
            respuesta.put("arrendatario", idArrendatario);
            respuesta.put("arrendamiento", idArrendamiento);
            
            log.info("Calificación de arrendatario registrada: propietario={}, arrendatario={}, puntuación={}, arrendamiento={}", 
                    idPropietario, idArrendatario, puntuacion, idArrendamiento);
            
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

    /**
     * Calificar un propietario
     */
    @PostMapping("/calificar-propietario")
    public ResponseEntity<?> calificarPropietario(
            @RequestParam String idArrendatario,
            @RequestParam String idPropietario,
            @RequestParam int puntuacion,
            @RequestParam(required = false) String comentario,
            @RequestParam String idArrendamiento) {
        
        try {
            // Validaciones de entrada
            validarParametrosBasicos(idArrendatario, "idArrendatario");
            validarParametrosBasicos(idPropietario, "idPropietario");
            validarParametrosBasicos(idArrendamiento, "idArrendamiento");
            validarPuntuacion(puntuacion);
            validarComentario(comentario);
            
            // Calificar SOLO al propietario (responsabilidad única)
            calificacionUsuarioService.calificarPropietario(
                idArrendatario, idPropietario, puntuacion, comentario, idArrendamiento);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Calificación del propietario registrada exitosamente");
            respuesta.put("puntuacion", puntuacion);
            respuesta.put("propietario", idPropietario);
            respuesta.put("arrendamiento", idArrendamiento);
            
            log.info("Calificación de propietario registrada: arrendatario={}, propietario={}, puntuación={}, arrendamiento={}", 
                    idArrendatario, idPropietario, puntuacion, idArrendamiento);
            
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

    /**
     * Calificar un espacio 
     */
    @PostMapping("/calificar-espacio")
    public ResponseEntity<?> calificarEspacio(
            @RequestParam String idUsuario,
            @RequestParam String idEspacio,
            @RequestParam int puntuacion,
            @RequestParam(required = false) String comentario) {
        
        try {
            // Validaciones de entrada
            validarParametrosBasicos(idUsuario, "idUsuario");
            validarParametrosBasicos(idEspacio, "idEspacio");
            validarPuntuacion(puntuacion);
            validarComentario(comentario);
            
            // Calificar el espacio
            calificacionEspacioService.calificarEspacio(idUsuario, idEspacio, puntuacion, comentario);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Calificación del espacio registrada exitosamente");
            respuesta.put("puntuacion", puntuacion);
            respuesta.put("espacio", idEspacio);
            respuesta.put("usuario", idUsuario);
            
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

    /**
     * Verificar si un usuario puede calificar a otro
     */
    @GetMapping("/verificar-permisos/{idCalificador}/{idCalificado}/{idArrendamiento}")
    public ResponseEntity<?> verificarPermisos(
            @PathVariable String idCalificador,
            @PathVariable String idCalificado,
            @PathVariable String idArrendamiento) {
        
        try {
            boolean puedeCalificar = calificacionUsuarioService.puedeCalificar(
                idCalificador, idCalificado, idArrendamiento);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("puedeCalificar", puedeCalificar);
            respuesta.put("calificador", idCalificador);
            respuesta.put("calificado", idCalificado);
            respuesta.put("arrendamiento", idArrendamiento);
            
            if (!puedeCalificar) {
                respuesta.put("razon", "El arrendamiento debe estar completado y dentro de la ventana de calificación (30 días)");
            }
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            log.error("Error al verificar permisos de calificación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al verificar permisos"));
        }
    }

    /**
     * Obtener calificaciones de un usuario
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerCalificacionesUsuario(@PathVariable String idUsuario) {
        
        try {
            validarParametrosBasicos(idUsuario, "idUsuario");
            
            List<CalificacionUsuario> calificaciones = 
                calificacionUsuarioService.obtenerCalificacionesUsuario(idUsuario);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("usuario", idUsuario);
            respuesta.put("calificaciones", calificaciones);
            respuesta.put("totalCalificaciones", calificaciones.size());
            
            // Calcular estadísticas
            if (!calificaciones.isEmpty()) {
                double promedioCalculado = calificaciones.stream()
                    .mapToInt(cal -> cal.getPuntuacionAsInt())
                    .average()
                    .orElse(0.0);
                respuesta.put("promedioCalculado", Math.round(promedioCalculado * 100.0) / 100.0);
            }
            
            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener calificaciones del usuario {}: {}", idUsuario, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al obtener calificaciones"));
        }
    }

    /**
     * Actualizar promedio de calificaciones de un usuario
     */
    @PostMapping("/actualizar-promedio/{idUsuario}")
    public ResponseEntity<?> actualizarPromedioCalificaciones(@PathVariable String idUsuario) {
        
        try {
            validarParametrosBasicos(idUsuario, "idUsuario");
            
            calificacionUsuarioService.actualizarPromedioCalificaciones(idUsuario);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Promedio de calificaciones actualizado exitosamente");
            respuesta.put("usuario", idUsuario);
            
            log.info("Promedio de calificaciones actualizado para usuario: {}", idUsuario);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            log.error("Error al actualizar promedio de usuario {}: {}", idUsuario, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al actualizar promedio"));
        }
    }


    private void validarParametrosBasicos(String parametro, String nombreParametro) {
        if (parametro == null || parametro.trim().isEmpty()) {
            throw new IllegalArgumentException("El parámetro " + nombreParametro + " no puede estar vacío");
        }
    }

    private void validarPuntuacion(int puntuacion) {
        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5 estrellas");
        }
    }

    private void validarComentario(String comentario) {
        if (comentario != null && comentario.length() > 300) {
            throw new IllegalArgumentException("El comentario no puede exceder los 300 caracteres");
        }
    }

    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", mensaje);
        error.put("timestamp", new java.util.Date());
        error.put("success", false);
        return error;
    }
}