package com.apirest.backend.Service;

import com.apirest.backend.Model.Mensaje;

import java.util.List;

public interface INotificacionService {
    void enviarNotificacion(String usuarioId, String contenido, String avisoId);
    List<Mensaje> obtenerNotificacionesUsuario(String usuarioId, String avisoId);
    List<Mensaje> obtenerNotificacionesNoLeidas(String usuarioId, String avisoId);
    void marcarComoLeida(String avisoId, int indiceMensaje);
    void marcarTodasComoLeidas(String usuarioId, String avisoId);
    long contarNotificacionesNoLeidas(String usuarioId, String avisoId);
    void responderNotificacion(String avisoId, int indiceMensaje, String contenidoRespuesta);
    void notificarNuevaCalificacion(String propietarioId, String espacioId, int puntuacion, String avisoId);
    void notificarNuevoComentario(String propietarioId, String comentario, String avisoId);
    void notificarModeracionAviso(String propietarioId, String avisoId, String motivo, String accion);
    
    // Estos métodos son necesarios para AvisoServiceImpl
    void enviarNotificacionAdministrador(String titulo, String mensaje);
    void enviarNotificacionPropietario(String titulo, String mensaje);
}