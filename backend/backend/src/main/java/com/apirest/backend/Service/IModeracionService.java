package com.apirest.backend.Service;

import org.bson.types.ObjectId;

public interface IModeracionService {
    void moderarAviso(ObjectId avisoId, String accion, String motivo);
}