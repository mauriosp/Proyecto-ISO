package com.apirest.backend.Service;

import org.bson.types.ObjectId;

public interface ICalificacionEspacioService {
    void calificarEspacio(ObjectId usuarioId, ObjectId espacioId, int puntuacion, String comentario);
}