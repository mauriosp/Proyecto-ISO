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

    @GetMapping("/listar")
    public ResponseEntity<List<Reporte>> listarReportes() {
        return ResponseEntity.ok(reporteService.listarReportes());
    }

    @PostMapping("/reportar")
    public ResponseEntity<String> reportarAviso(
            @RequestParam String idAviso,
            @RequestParam String idUsuario,
            @RequestParam String motivo,
            @RequestParam(required = false) String comentarios) {
        try {
            reporteService.reportarAviso(idAviso, idUsuario, motivo, comentarios);
            return new ResponseEntity<>("Reporte enviado correctamente", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al enviar el reporte: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/resolver/{idReporte}")
    public ResponseEntity<String> resolverReporte(
            @PathVariable String idReporte,
            @RequestParam String decision,
            @RequestParam(required = false) String motivo) {
        try {
            reporteService.resolverReporte(idReporte, decision, motivo);
            return new ResponseEntity<>("Reporte resuelto correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al resolver el reporte: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
