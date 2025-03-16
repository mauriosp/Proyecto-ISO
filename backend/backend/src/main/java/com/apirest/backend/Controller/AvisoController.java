package com.apirest.backend.Controller;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Service.IAvisoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/UAO/apirest/Aviso") // Endpoint

public class AvisoController {
    
    @Autowired
    private IAvisoService avisoService;


    @PostMapping("/insertar")
    public ResponseEntity<String> insertarAviso(@RequestBody Aviso aviso) {
        avisoService.guardarAviso(aviso);
        return new ResponseEntity<>("Aviso guardado correctamente", HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Aviso>> listarAvisos() {
        return ResponseEntity.ok(avisoService.listarAvisos());
    }

}
