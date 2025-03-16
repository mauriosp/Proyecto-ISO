package com.apirest.backend.Controller;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/UAO/apirest/Usuario") // Endpoint
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/insertar")
    public ResponseEntity<String> insertarUsuario(@RequestBody Usuario usuario) {
        usuarioService.guardarUsuario(usuario);
        return new ResponseEntity<>("Usuario guardado correctamente", HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return new ResponseEntity<>(usuarioService.listarUsuarios(), HttpStatus.OK);
    }
}