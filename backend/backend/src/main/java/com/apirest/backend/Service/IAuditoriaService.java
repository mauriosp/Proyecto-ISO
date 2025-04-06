package com.apirest.backend.Service;
import com.apirest.backend.Model.Usuario;

public interface IAuditoriaService {
    void registrarCambio(Usuario usuario, String campo, String valorAnterior, 
                         String valorNuevo);
}