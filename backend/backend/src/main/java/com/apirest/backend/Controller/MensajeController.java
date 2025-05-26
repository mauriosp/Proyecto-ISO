package com.apirest.backend.Controller;

import com.apirest.backend.Model.Mensaje;
import com.apirest.backend.Service.IMensajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/UAO/apirest/Notificaciones")
@RequiredArgsConstructor
public class MensajeController {

    private final IMensajeService notificacionService;

    @GetMapping("/usuario/{usuarioId}/aviso/{avisoId}")
    public ResponseEntity<List<Mensaje>> obtenerNotificacionesUsuario(
            @PathVariable String usuarioId,
            @PathVariable String avisoId) {
        List<Mensaje> notificaciones = notificacionService.obtenerMensajesUsuario(usuarioId, avisoId);
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/usuario/{usuarioId}/aviso/{avisoId}/no-leidas")
    public ResponseEntity<List<Mensaje>> obtenerNotificacionesNoLeidas(
            @PathVariable String usuarioId,
            @PathVariable String avisoId) {
        List<Mensaje> notificaciones = notificacionService.obtenerMensajesNoLeidas(usuarioId, avisoId);
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/usuario/{usuarioId}/aviso/{avisoId}/contador")
    public ResponseEntity<Long> contarNotificacionesNoLeidas(
            @PathVariable String usuarioId,
            @PathVariable String avisoId) {
        long cantidad = notificacionService.contarMensajesNoLeidas(usuarioId, avisoId);
        return ResponseEntity.ok(cantidad);
    }

    @PatchMapping("/aviso/{avisoId}/mensaje/{indiceMensaje}/leer")
    public ResponseEntity<?> marcarComoLeida(
            @PathVariable String avisoId,
            @PathVariable int indiceMensaje) {
        notificacionService.marcarComoLeida(avisoId, indiceMensaje);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/usuario/{usuarioId}/aviso/{avisoId}/leer-todas")
    public ResponseEntity<?> marcarTodasComoLeidas(
            @PathVariable String usuarioId,
            @PathVariable String avisoId) {
        notificacionService.marcarTodasComoLeidas(usuarioId, avisoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/aviso/{avisoId}/mensaje/{indiceMensaje}/responder")
    public ResponseEntity<?> responderNotificacion(
            @PathVariable String avisoId,
            @PathVariable int indiceMensaje,
            @RequestBody Map<String, String> payload) {
        String contenidoRespuesta = payload.get("contenido");
        notificacionService.responderMensaje(avisoId, indiceMensaje, contenidoRespuesta);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calificacion")
    public ResponseEntity<?> notificarCalificacion(@RequestBody Map<String, Object> payload) {
        String propietarioId = (String) payload.get("propietarioId");
        String espacioId = (String) payload.get("espacioId");
        String avisoId = (String) payload.get("avisoId");
        int puntuacion = Integer.parseInt(payload.get("puntuacion").toString());

        notificacionService.notificarNuevaCalificacion(propietarioId, espacioId, puntuacion, avisoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comentario")
    public ResponseEntity<?> notificarComentario(@RequestBody Map<String, String> payload) {
        String propietarioId = payload.get("propietarioId");
        String comentario = payload.get("comentario");
        String avisoId = payload.get("avisoId");

        notificacionService.notificarNuevoComentario(propietarioId, comentario, avisoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/moderacion")
    public ResponseEntity<?> notificarModeracion(@RequestBody Map<String, String> payload) {
        String propietarioId = payload.get("propietarioId");
        String avisoId = payload.get("avisoId");
        String motivo = payload.get("motivo");
        String accion = payload.get("accion");

        notificacionService.notificarModeracionAviso(propietarioId, avisoId, motivo, accion);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/enviar")
    public ResponseEntity<?> enviarMensaje(
            @RequestParam String usuarioId,
            @RequestParam String avisoId,
            @RequestParam String contenido) {
        try {
            notificacionService.enviarMensaje(usuarioId, contenido, avisoId);
            return ResponseEntity.ok("Mensaje enviado correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}/todos")
    public ResponseEntity<List<Mensaje>> obtenerTodosMensajesUsuario(
            @PathVariable String usuarioId) {
        List<Mensaje> mensajes = notificacionService.obtenerTodosMensajesPorUsuario(usuarioId);
        return ResponseEntity.ok(mensajes);
    }
}