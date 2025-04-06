package com.apirest.backend.Service;

public interface ICalificacionEspacioService {
    void calificarEspacio(String usuarioId, String espacioId, int puntuacion, String comentario);
}