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

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Service.IImagenAvisoService;

import lombok.RequiredArgsConstructor;

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
            Aviso avisoActualizado = imagenAvisoService.agregarImagenAAviso(avisoId, archivo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Imagen subida exitosamente");
            respuesta.put("aviso", avisoActualizado);

            return ResponseEntity.ok(respuesta);
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
            Aviso avisoActualizado = imagenAvisoService.eliminarImagenDeAviso(avisoId, nombreImagen);
            return ResponseEntity.ok(
                Map.of(
                    "mensaje", "Imagen eliminada exitosamente",
                    "aviso", avisoActualizado
                )
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }
}