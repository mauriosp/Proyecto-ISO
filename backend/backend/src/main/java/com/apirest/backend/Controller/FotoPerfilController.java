package com.apirest.backend.Controller;

import com.apirest.backend.Exception.ImagenException;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Service.IFotoPerfilService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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
            // Convertir usuarioId de String a ObjectId
            ObjectId objectId = new ObjectId(usuarioId);

            // Actualizar la foto de perfil
            Usuario usuarioActualizado = fotoPerfilService.actualizarFotoPerfil(objectId, archivo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Foto de perfil actualizada exitosamente");
            respuesta.put("usuario", usuarioActualizado);

            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "ID de usuario inválido"));
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
            // Convertir usuarioId de String a ObjectId
            ObjectId objectId = new ObjectId(usuarioId);

            // Eliminar la foto de perfil
            Usuario usuarioActualizado = fotoPerfilService.eliminarFotoPerfil(objectId);

            return ResponseEntity.ok(
                Map.of(
                    "mensaje", "Foto de perfil eliminada exitosamente",
                    "usuario", usuarioActualizado
                )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "ID de usuario inválido"));
        } catch (ImagenException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }
}