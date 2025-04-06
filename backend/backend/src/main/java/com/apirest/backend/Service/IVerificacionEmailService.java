package com.apirest.backend.Service;

public interface IVerificacionEmailService {
    void enviarCorreoVerificacion(String userId, String email);
    boolean verificarToken(String token);
}