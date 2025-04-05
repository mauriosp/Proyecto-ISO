package com.apirest.backend.Controller;

import com.apirest.backend.Model.Reporte;
import com.apirest.backend.Service.IReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/UAO/apirest/Reporte")
public class ReporteController {

    @Autowired
    private IReporteService reporteService;

    @PostMapping("/insertar")
    public ResponseEntity<String> insertarReporte(@RequestBody Reporte reporte) {
        reporteService.guardarReporte(reporte);
        return new ResponseEntity<>("Reporte guardado correctamente", HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Reporte>> listarReportes() {
        return ResponseEntity.ok(reporteService.listarReportes());
    }
}
