package com.apirest.backend.Service;

import com.apirest.backend.Model.Usuario;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface IUsuarioService {
    // Especificar las operaciones (CRUD)
    public String guardarUsuario(Usuario usuario);
    public List <Usuario> listarUsuarios();
    public String registrarUsuario(Usuario usuario);
    void actualizarPerfil(String id, String nombre, String telefono, MultipartFile fotoPerfil) throws Exception;
    void eliminarCuenta(String id);
    
}