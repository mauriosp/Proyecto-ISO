package com.apirest.backend.Service;

public interface IContraseñaService {
    void enviarCorreoResetContraseña(String email);
    boolean actualizarContraseña(String token, String nuevaContraseña);
}