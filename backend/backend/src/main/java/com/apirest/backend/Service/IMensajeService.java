package com.apirest.backend.Service;

import com.apirest.backend.Model.Mensaje;
import org.bson.types.ObjectId;

import java.util.List;

public interface IMensajeService {
    void enviarMensaje(ObjectId usuarioId, String contenido, ObjectId avisoId);
    List<Mensaje> obtenerMensajesUsuario(ObjectId usuarioId, ObjectId avisoId);
    List<Mensaje> obtenerMensajesNoLeidas(ObjectId usuarioId, ObjectId avisoId);
    void marcarComoLeida(ObjectId avisoId, int indiceMensaje);
    void marcarTodasComoLeidas(ObjectId usuarioId, ObjectId avisoId);
    long contarMensajesNoLeidas(ObjectId usuarioId, ObjectId avisoId);
    void responderMensaje(ObjectId avisoId, int indiceMensaje, String contenidoRespuesta);
    void notificarNuevaCalificacion(ObjectId propietarioId, ObjectId espacioId, int puntuacion, ObjectId avisoId);
    void notificarNuevoComentario(ObjectId propietarioId, String comentario, ObjectId avisoId);
    void notificarModeracionAviso(ObjectId propietarioId, ObjectId avisoId, String motivo, String accion);
    
    // Estos métodos son necesarios para AvisoServiceImpl
    void enviarMensajeAdministrador(String titulo, String mensaje);
    void enviarMensajePropietario(String titulo, String mensaje);
}