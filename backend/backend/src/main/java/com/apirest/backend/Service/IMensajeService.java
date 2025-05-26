package com.apirest.backend.Service;

import java.util.List;

import com.apirest.backend.Model.Mensaje;

public interface IMensajeService {
    void enviarMensaje(String usuarioId, String contenido, String avisoId);
    List<Mensaje> obtenerMensajesUsuario(String usuarioId, String avisoId);
    List<Mensaje> obtenerMensajesNoLeidas(String usuarioId, String avisoId);
    void marcarComoLeida(String avisoId, int indiceMensaje);
    void marcarTodasComoLeidas(String usuarioId, String avisoId);
    long contarMensajesNoLeidas(String usuarioId, String avisoId);
    void responderMensaje(String avisoId, int indiceMensaje, String contenidoRespuesta);
    void notificarNuevaCalificacion(String propietarioId, String espacioId, int puntuacion, String avisoId);
    void notificarNuevoComentario(String propietarioId, String comentario, String avisoId);
    void notificarModeracionAviso(String propietarioId, String avisoId, String motivo, String accion);
    
    // Estos métodos son necesarios para AvisoServiceImpl
    void enviarMensajeAdministrador(String titulo, String mensaje);
    void enviarMensajePropietario(String titulo, String mensaje);
    List<Mensaje> obtenerTodosMensajesPorUsuario(String usuarioId);
}