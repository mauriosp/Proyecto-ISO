package com.apirest.backend.Service;

import com.apirest.backend.Model.Usuario;
import java.util.List;

public interface IUsuarioService {
    // Especificar las operaciones (CRUD)
    public String guardarUsuario(Usuario usuario);
    public List <Usuario> listarUsuarios();
    String loginUsuario(String email, String contraseña);
    
}