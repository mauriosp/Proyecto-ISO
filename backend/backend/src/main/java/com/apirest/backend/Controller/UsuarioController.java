package com.apirest.backend.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Service.IUsuarioService;

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

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarCuenta(@PathVariable String id) {
        try {
            usuarioService.eliminarCuenta(id);
            return new ResponseEntity<>("Cuenta eliminada correctamente", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la cuenta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String contraseña) {
        try {
            // Llamar al servicio para autenticar al usuario
            String mensaje = usuarioService.loginUsuario(email, contraseña);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al iniciar sesión: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}