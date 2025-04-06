package com.apirest.backend.Controller;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Service.IAvisoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.math.BigDecimal;
import org.bson.types.ObjectId;

@RestController
@RequestMapping("/UAO/apirest/Aviso") // Endpoint

public class AvisoController {
    
    @Autowired
    private IAvisoService avisoService;

    @PostMapping("/crear")
    public ResponseEntity<String> crearAviso(
            @RequestParam String tipoEspacio,
            @RequestParam String descripcion,
            @RequestParam double precioMensual,
            @RequestParam(required = false) String condicionesAdicionales,
            @RequestParam List<MultipartFile> imagenes,
            @RequestParam String titulo,
            @RequestParam String direccion,
            @RequestParam BigDecimal area,
            @RequestParam ObjectId idUsuario) {
        try {
            avisoService.crearAviso(descripcion, precioMensual, imagenes, titulo, tipoEspacio, condicionesAdicionales, direccion, area, idUsuario);
            return new ResponseEntity<>("Aviso creado exitosamente", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el aviso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editarAviso(
            @PathVariable String id,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) Double precioMensual,
            @RequestParam(required = false) List<MultipartFile> imagenes,
            @RequestParam(required = false) String estado) {
        try {
            avisoService.editarAviso(id, titulo, descripcion, precioMensual, imagenes, estado);
            return new ResponseEntity<>("Aviso actualizado correctamente", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el aviso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarAviso(@PathVariable String id) {
        try {
            avisoService.eliminarAviso(id);
            return new ResponseEntity<>("Aviso eliminado correctamente", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el aviso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Aviso>> listarAvisos() {
        return ResponseEntity.ok(avisoService.listarAvisos());
    }

}
