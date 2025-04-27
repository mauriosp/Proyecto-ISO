package com.apirest.backend.Controller;

import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Service.IEspacioService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/UAO/apirest/Espacio")
public class EspacioController {

    @Autowired
    private IEspacioService espacioService;

    @GetMapping("/listar")
    public ResponseEntity<List<Espacio>> listarEspacios() {
        return ResponseEntity.ok(espacioService.listarEspacios());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Optional<Espacio>> buscarEspacio(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id); // Convertir String a ObjectId
            return ResponseEntity.ok(espacioService.buscarEspacioPorId(objectId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Optional.empty());
        }
    }

    @PostMapping("/insertar")
    public ResponseEntity<Espacio> insertarEspacio(@RequestBody Espacio espacio) {
        return ResponseEntity.ok(espacioService.guardarEspacio(espacio));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarEspacio(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id); // Convertir String a ObjectId
            espacioService.eliminarEspacio(objectId);
            return ResponseEntity.ok("Espacio eliminado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ID inválido");
        }
    }
}

