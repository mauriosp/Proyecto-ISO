package com.apirest.backend.Service;

import org.bson.types.ObjectId;

public interface IVerificacionEmailService {
    void enviarCorreoVerificacion(ObjectId userId, String email);
    boolean verificarToken(String token);
}