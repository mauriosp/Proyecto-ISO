package com.apirest.backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.apirest.backend.Exception.ImagenException;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Service.IFotoPerfilService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/foto-perfil")
@RequiredArgsConstructor
public class FotoPerfilController {
    private final IFotoPerfilService fotoPerfilService;

    @PostMapping("/actualizar")
    public ResponseEntity<?> actualizarFotoPerfil(
        @PathVariable String usuarioId,
        @RequestParam("archivo") MultipartFile archivo
    ) {
        try {
            Usuario usuarioActualizado = fotoPerfilService.actualizarFotoPerfil(usuarioId, archivo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Foto de perfil actualizada exitosamente");
            respuesta.put("usuario", usuarioActualizado);

            return ResponseEntity.ok(respuesta);
        } catch (ImagenException.TipoInvalidoException | ImagenException.TamanoExcedidoException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar la foto de perfil: " + e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarFotoPerfil(
        @PathVariable String usuarioId
    ) {
        try {
            Usuario usuarioActualizado = fotoPerfilService.eliminarFotoPerfil(usuarioId);
            return ResponseEntity.ok(
                Map.of(
                    "mensaje", "Foto de perfil eliminada exitosamente",
                    "usuario", usuarioActualizado
                )
            );
        } catch (ImagenException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }
}