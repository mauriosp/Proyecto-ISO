package com.apirest.backend.Service;

import java.util.List;

import com.apirest.backend.Model.CalificacionUsuario;

public interface ICalificacionUsuarioService {
    

    void calificarArrendatario(String idPropietario, String idArrendatario, int puntuacion, String comentario, String idArrendamiento);

    void calificarPropietario(String idArrendatario, String idPropietario, int puntuacion, String comentario, String idArrendamiento);
    
    List<CalificacionUsuario> obtenerCalificacionesUsuario(String idUsuario);
    
    void actualizarPromedioCalificaciones(String idUsuario);

    boolean puedeCalificar(String idCalificador, String idCalificado, String idArrendamiento);
}