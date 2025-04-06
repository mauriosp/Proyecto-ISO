package com.apirest.backend.Service;

public interface IModeracionService {
    void moderarAviso(String avisoId, String accion, String motivo);
}