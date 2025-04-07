package com.apirest.backend.Controller;

import com.apirest.backend.Model.Mensaje;
import com.apirest.backend.Service.INotificacionService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/UAO/apirest/Notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final INotificacionService notificacionService;

    @GetMapping("/usuario/{usuarioId}/aviso/{avisoId}")
    public ResponseEntity<List<Mensaje>> obtenerNotificacionesUsuario(
            @PathVariable String usuarioId,
            @PathVariable String avisoId) {
        List<Mensaje> notificaciones = notificacionService.obtenerNotificacionesUsuario(
                new ObjectId(usuarioId), new ObjectId(avisoId));
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/usuario/{usuarioId}/aviso/{avisoId}/no-leidas")
    public ResponseEntity<List<Mensaje>> obtenerNotificacionesNoLeidas(
            @PathVariable String usuarioId,
            @PathVariable String avisoId) {
        List<Mensaje> notificaciones = notificacionService.obtenerNotificacionesNoLeidas(
                new ObjectId(usuarioId), new ObjectId(avisoId));
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/usuario/{usuarioId}/aviso/{avisoId}/contador")
    public ResponseEntity<Long> contarNotificacionesNoLeidas(
            @PathVariable String usuarioId,
            @PathVariable String avisoId) {
        long cantidad = notificacionService.contarNotificacionesNoLeidas(
                new ObjectId(usuarioId), new ObjectId(avisoId));
        return ResponseEntity.ok(cantidad);
    }

    @PatchMapping("/aviso/{avisoId}/mensaje/{indiceMensaje}/leer")
    public ResponseEntity<?> marcarComoLeida(
            @PathVariable String avisoId,
            @PathVariable int indiceMensaje) {
        notificacionService.marcarComoLeida(new ObjectId(avisoId), indiceMensaje);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/usuario/{usuarioId}/aviso/{avisoId}/leer-todas")
    public ResponseEntity<?> marcarTodasComoLeidas(
            @PathVariable String usuarioId,
            @PathVariable String avisoId) {
        notificacionService.marcarTodasComoLeidas(
                new ObjectId(usuarioId), new ObjectId(avisoId));
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/aviso/{avisoId}/mensaje/{indiceMensaje}/responder")
    public ResponseEntity<?> responderNotificacion(
            @PathVariable String avisoId,
            @PathVariable int indiceMensaje,
            @RequestBody Map<String, String> payload) {
        String contenidoRespuesta = payload.get("contenido");
        notificacionService.responderNotificacion(
                new ObjectId(avisoId), indiceMensaje, contenidoRespuesta);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/calificacion")
    public ResponseEntity<?> notificarCalificacion(@RequestBody Map<String, Object> payload) {
        String propietarioId = (String) payload.get("propietarioId");
        String espacioId = (String) payload.get("espacioId");
        String avisoId = (String) payload.get("avisoId");
        int puntuacion = Integer.parseInt(payload.get("puntuacion").toString());
        
        notificacionService.notificarNuevaCalificacion(
                new ObjectId(propietarioId), 
                new ObjectId(espacioId), 
                puntuacion, 
                new ObjectId(avisoId));
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/comentario")
    public ResponseEntity<?> notificarComentario(@RequestBody Map<String, String> payload) {
        String propietarioId = payload.get("propietarioId");
        String comentario = payload.get("comentario");
        String avisoId = payload.get("avisoId");
        
        notificacionService.notificarNuevoComentario(
                new ObjectId(propietarioId), 
                comentario, 
                new ObjectId(avisoId));
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/moderacion")
    public ResponseEntity<?> notificarModeracion(@RequestBody Map<String, String> payload) {
        String propietarioId = payload.get("propietarioId");
        String avisoId = payload.get("avisoId");
        String motivo = payload.get("motivo");
        String accion = payload.get("accion");
        
        notificacionService.notificarModeracionAviso(
                new ObjectId(propietarioId), 
                new ObjectId(avisoId), 
                motivo, 
                accion);
        return ResponseEntity.ok().build();
    }
}