package com.apirest.backend.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Model.Mensaje;
import com.apirest.backend.Model.RespuestaMensaje;
import com.apirest.backend.Repository.AvisoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MensajeServiceImpl implements IMensajeService {

    private final AvisoRepository avisoRepository;

    @Override
    public void enviarMensaje(String usuarioId, String contenido, String avisoId) {
        // Convertir String a ObjectId para el findById
        Aviso aviso = avisoRepository.findById(avisoId)
            .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado: " + avisoId));
        
        // Crear nuevo mensaje (notificación)
        Mensaje mensaje = new Mensaje();
        mensaje.setIdUsuario(new ObjectId(usuarioId)); // Convertir usuarioId a ObjectId
        mensaje.setContenido(contenido);
        mensaje.setFechaMensaje(new Date());
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
    public void notificarNuevaCalificacion(String propietarioId, String espacioId, 
                                          int puntuacion, String avisoId) {
        String contenido = "Tu espacio ha recibido una nueva calificación de " + puntuacion + " estrellas.";
        enviarMensaje(propietarioId, contenido, avisoId);
    }
    
    @Override
    public void notificarNuevoComentario(String propietarioId, String comentario, String avisoId) {
        String contenido = "Nuevo comentario: \"" + comentario + "\"";
        enviarMensaje(propietarioId, contenido, avisoId);
    }
    
    @Override
    public void notificarModeracionAviso(String propietarioId, String avisoId, 
                                        String motivo, String accion) {
        String contenido = "Tu aviso ha sido " + accion + ". Motivo: " + motivo;
        enviarMensaje(propietarioId, contenido, avisoId);
    }
    
    @Override
    public List<Mensaje> obtenerMensajesUsuario(String usuarioId, String avisoId) {
        Aviso aviso = avisoRepository.findById(avisoId)
            .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));
        
        if (aviso.getMensaje() == null) {
            return new ArrayList<>();
        }
        
        List<Mensaje> notificacionesUsuario = new ArrayList<>();
        
        for (Mensaje mensaje : aviso.getMensaje()) {
            if (mensaje.getIdUsuario().toHexString().equals(usuarioId)) { // Comparar como String
                notificacionesUsuario.add(mensaje);
            }
        }
        
        return notificacionesUsuario;
    }
    
    @Override
    public List<Mensaje> obtenerMensajesNoLeidas(String usuarioId, String avisoId) {
        List<Mensaje> todasNotificaciones = obtenerMensajesUsuario(usuarioId, avisoId);
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
        Aviso aviso = avisoRepository.findById(avisoId)
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
        Aviso aviso = avisoRepository.findById(avisoId)
            .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));
        
        if (aviso.getMensaje() != null) {
            boolean cambios = false;
            
            for (Mensaje mensaje : aviso.getMensaje()) {
                if (mensaje.getIdUsuario().toHexString().equals(usuarioId) && !mensaje.isEstadoMensaje()) {
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
    public long contarMensajesNoLeidas(String usuarioId, String avisoId) {
        return obtenerMensajesNoLeidas(usuarioId, avisoId).size();
    }
    
    @Override
    public void responderMensaje(String avisoId, int indiceMensaje, String contenidoRespuesta) {
        Aviso aviso = avisoRepository.findById(avisoId)
            .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));
        
        if (aviso.getMensaje() != null && indiceMensaje >= 0 && indiceMensaje < aviso.getMensaje().size()) {
            Mensaje mensaje = aviso.getMensaje().get(indiceMensaje);
            
            RespuestaMensaje respuesta = new RespuestaMensaje();
            respuesta.setFechaRespuesta(new Date());
            respuesta.setContenido(contenidoRespuesta);
            
            mensaje.setRespuestaMensaje(respuesta);
            avisoRepository.save(aviso);
            
            log.info("Respuesta añadida a notificación: aviso={}, índice={}", avisoId, indiceMensaje);
        } else {
            throw new IllegalArgumentException("Índice de mensaje inválido");
        }
    }
    
    @Override
    public void enviarMensajeAdministrador(String titulo, String mensaje) {
        log.info("Notificación al administrador - Título: {}, Mensaje: {}", titulo, mensaje);
        // Implementar lógica para enviar notificación al administrador
    }
    
    @Override
    public void enviarMensajePropietario(String titulo, String mensaje) {
        log.info("Notificación al propietario - Título: {}, Mensaje: {}", titulo, mensaje);
        // Implementar lógica para enviar notificación al propietario
    }

    @Override
    public List<Mensaje> obtenerTodosMensajesPorUsuario(String usuarioId) {
        List<Mensaje> mensajesUsuario = new ArrayList<>();
        List<Aviso> avisos = avisoRepository.findAll();
        for (Aviso aviso : avisos) {
            if (aviso.getMensaje() != null) {
                for (Mensaje mensaje : aviso.getMensaje()) {
                    if (mensaje.getIdUsuario().toHexString().equals(usuarioId)) {
                        mensajesUsuario.add(mensaje);
                    }
                }
            }
        }
        return mensajesUsuario;
    }
}