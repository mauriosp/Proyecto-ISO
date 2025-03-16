package com.apirest.backend.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public String guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario); 
        return "El Usuario" + usuario.getNombre() + ",fue creado con éxito";
    }

    @Override
    public List <Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }


}