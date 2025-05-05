package com.apirest.backend.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Service.IEspacioService;

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

