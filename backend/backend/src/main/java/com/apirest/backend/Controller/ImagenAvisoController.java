package com.apirest.backend.Controller;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Service.IImagenAvisoService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/avisos/{avisoId}/imagenes")
@RequiredArgsConstructor
public class ImagenAvisoController {
    private final IImagenAvisoService imagenAvisoService;

    @PostMapping("/subir")
    public ResponseEntity<?> subirImagen(
        @PathVariable String avisoId,
        @RequestParam("archivo") MultipartFile archivo
    ) {
        try {
            // Convertir avisoId de String a ObjectId
            ObjectId objectId = new ObjectId(avisoId);

            // Subir la imagen al aviso
            Aviso avisoActualizado = imagenAvisoService.agregarImagenAAviso(objectId, archivo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Imagen subida exitosamente");
            respuesta.put("aviso", avisoActualizado);

            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "ID de aviso inválido"));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarImagen(
        @PathVariable String avisoId,
        @RequestParam("nombreImagen") String nombreImagen
    ) {
        try {
            // Convertir avisoId de String a ObjectId
            ObjectId objectId = new ObjectId(avisoId);

            // Eliminar la imagen del aviso
            Aviso avisoActualizado = imagenAvisoService.eliminarImagenDeAviso(objectId, nombreImagen);

            return ResponseEntity.ok(
                Map.of(
                    "mensaje", "Imagen eliminada exitosamente",
                    "aviso", avisoActualizado
                )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "ID de aviso inválido"));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }
}