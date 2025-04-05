package com.apirest.backend.Controller;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            String mensaje = usuarioService.registrarUsuario(usuario);
            return new ResponseEntity<>(mensaje, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al registrar el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return new ResponseEntity<>(usuarioService.listarUsuarios(), HttpStatus.OK);
    }

    @PutMapping("/actualizarPerfil/{id}")
    public ResponseEntity<String> actualizarPerfil(
            @PathVariable String id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) MultipartFile fotoPerfil) {
        try {
            usuarioService.actualizarPerfil(id, nombre, telefono, fotoPerfil);
            return new ResponseEntity<>("Perfil actualizado correctamente", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el perfil: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}