package com.apirest.backend.Controller;

import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Service.IEspacioService;
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
        return ResponseEntity.ok(espacioService.buscarEspacioPorId(id));
    }

    @PostMapping("/insertar")
    public ResponseEntity<Espacio> insertarEspacio(@RequestBody Espacio espacio) {
        return ResponseEntity.ok(espacioService.guardarEspacio(espacio));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarEspacio(@PathVariable String id) {
        espacioService.eliminarEspacio(id);
        return ResponseEntity.ok("Espacio eliminado correctamente");
    }
}

