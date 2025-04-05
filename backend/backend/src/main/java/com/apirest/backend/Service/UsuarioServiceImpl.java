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

    @Override
    public String registrarUsuario(Usuario usuario) {
        // Validar si el correo ya está registrado
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        // Guardar el usuario
        usuarioRepository.save(usuario);
        return "Usuario registrado con éxito: " + usuario.getNombre();
    }
}