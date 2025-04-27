package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Model.Mensaje;
import com.apirest.backend.Model.RespuestaMensaje;
import com.apirest.backend.Repository.AvisoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionServiceImpl implements INotificacionService {

    private final AvisoRepository avisoRepository;

    @Override
    public void enviarNotificacion(String usuarioId, String contenido, String avisoId) {
        // Convertir ObjectId a String para el findById
        Aviso aviso = avisoRepository.findById(avisoId.toString())
            .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado: " + avisoId));
        
        // Crear nuevo mensaje (notificación)
        Mensaje mensaje = new Mensaje();
        mensaje.setIdUsuario(new ObjectId(usuarioId));
        mensaje.setContenido(contenido);
        mensaje.setFechaAviso(new Date());
        mensaje.setEstadoMensaje(false); // No leído
        
        // Añadir el mensaje al aviso
        if (aviso.getMensaje() == null) {
            aviso.setMensaje(new ArrayList<>());
        }
        aviso.getMensaje().add(mensaje);
        
        // Guardar cambios
        avisoRepository.save(aviso);
        
        log.info("Notificación enviada a usuario: {}, aviso: {}", usuarioId, avisoId);
    }
    
    @Override
    public void notificarNuevaCalificacion(String propietarioId, String espacioId, int puntuacion, String avisoId) {
        String contenido = "Tu espacio ha recibido una nueva calificación de " + puntuacion + " estrellas.";
        enviarNotificacion(propietarioId, contenido, avisoId);
    }
    
    @Override
    public void notificarNuevoComentario(String propietarioId, String comentario, String avisoId) {
        String contenido = "Nuevo comentario: \"" + comentario + "\"";
        enviarNotificacion(propietarioId, contenido, avisoId);
    }
    
    @Override
    public void notificarModeracionAviso(String propietarioId, String avisoId, String motivo, String accion) {
        String contenido = "Tu aviso ha sido " + accion + ". Motivo: " + motivo;
        enviarNotificacion(propietarioId, contenido, avisoId);
    }
    
    @Override
    public List<Mensaje> obtenerNotificacionesUsuario(String usuarioId, String avisoId) {
        Aviso aviso = avisoRepository.findById(avisoId.toString())
            .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));
        
        if (aviso.getMensaje() == null) {
            return new ArrayList<>();
        }
        
        List<Mensaje> notificacionesUsuario = new ArrayList<>();
        
        for (Mensaje mensaje : aviso.getMensaje()) {
            if (mensaje.getIdUsuario().equals(usuarioId)) {
                notificacionesUsuario.add(mensaje);
            }
        }
        
        return notificacionesUsuario;
    }
    
    @Override
    public List<Mensaje> obtenerNotificacionesNoLeidas(String usuarioId, String avisoId) {
        List<Mensaje> todasNotificaciones = obtenerNotificacionesUsuario(usuarioId, avisoId);
        List<Mensaje> noLeidas = new ArrayList<>();
        
        for (Mensaje mensaje : todasNotificaciones) {
            if (!mensaje.isEstadoMensaje()) {
                noLeidas.add(mensaje);
            }
        }
        
        return noLeidas;
    }
    
    @Override
    public void marcarComoLeida(String avisoId, int indiceMensaje) {
        Aviso aviso = avisoRepository.findById(avisoId.toString())
            .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));
        
        if (aviso.getMensaje() != null && indiceMensaje >= 0 && indiceMensaje < aviso.getMensaje().size()) {
            Mensaje mensaje = aviso.getMensaje().get(indiceMensaje);
            mensaje.setEstadoMensaje(true);
            avisoRepository.save(aviso);
            
            log.info("Mensaje marcado como leído: aviso={}, índice={}", avisoId, indiceMensaje);
        } else {
            throw new IllegalArgumentException("Índice de mensaje inválido");
        }
    }
    
    @Override
    public void marcarTodasComoLeidas(String usuarioId, String avisoId) {
        Aviso aviso = avisoRepository.findById(avisoId.toString())
            .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));
        
        if (aviso.getMensaje() != null) {
            boolean cambios = false;
            
            for (Mensaje mensaje : aviso.getMensaje()) {
                if (mensaje.getIdUsuario().equals(usuarioId) && !mensaje.isEstadoMensaje()) {
                    mensaje.setEstadoMensaje(true);
                    cambios = true;
                }
            }
            
            if (cambios) {
                avisoRepository.save(aviso);
                log.info("Todas las notificaciones marcadas como leídas: usuario={}, aviso={}", 
                       usuarioId, avisoId);
            }
        }
    }
    
    @Override
    public long contarNotificacionesNoLeidas(String usuarioId, String avisoId) {
        return obtenerNotificacionesNoLeidas(usuarioId, avisoId).size();
    }
    
    @Override
    public void responderNotificacion(String avisoId, int indiceMensaje, String contenidoRespuesta) {
        Aviso aviso = avisoRepository.findById(avisoId.toString())
            .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));
        
        if (aviso.getMensaje() != null && indiceMensaje >= 0 && indiceMensaje < aviso.getMensaje().size()) {
            Mensaje mensaje = aviso.getMensaje().get(indiceMensaje);
            
            RespuestaMensaje respuesta = new RespuestaMensaje();
            respuesta.setFechaRespuesta(new Date());
            // Asegúrate de que el método setContenido existe en RespuestaMensaje
            respuesta.setContenido(contenidoRespuesta);
            
            mensaje.setRespuestaMensaje(respuesta);
            avisoRepository.save(aviso);
            
            log.info("Respuesta añadida a notificación: aviso={}, índice={}", avisoId, indiceMensaje);
        } else {
            throw new IllegalArgumentException("Índice de mensaje inválido");
        }
    }
    
    // Añadir estos métodos que se utilizan en AvisoServiceImpl
    @Override
    public void enviarNotificacionAdministrador(String titulo, String mensaje) {
        log.info("Notificación al administrador - Título: {}, Mensaje: {}", titulo, mensaje);
        // Implementar lógica para enviar notificación al administrador
    }
    
    @Override
    public void enviarNotificacionPropietario(String titulo, String mensaje) {
        log.info("Notificación al propietario - Título: {}, Mensaje: {}", titulo, mensaje);
        // Implementar lógica para enviar notificación al propietario
    }
}