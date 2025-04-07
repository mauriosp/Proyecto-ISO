package com.apirest.backend.Controller;

import com.apirest.backend.Model.Reporte;
import com.apirest.backend.Service.IReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private IReporteService reporteService;

    // Para usuarios: Crear reporte
    @PostMapping
    public ResponseEntity<Reporte> crearReporte(@RequestBody Reporte reporte) {
        return new ResponseEntity<>(reporteService.crearReporte(reporte), HttpStatus.CREATED);
    }
    // Para administradores: Listar todos los reportes
    @GetMapping
    public ResponseEntity<List<Reporte>> obtenerTodosReportes() {
        return new ResponseEntity<>(reporteService.obtenerTodosReportes(), HttpStatus.OK);
    }
    
    // Obtener un reporte específico
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtenerReportePorId(@PathVariable String id) {
        Reporte reporte = reporteService.obtenerReportePorId(id);
        if (reporte != null) {
            return new ResponseEntity<>(reporte, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // Actualizar estado de un reporte (para administradores)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Reporte> actualizarEstadoReporte(
            @PathVariable String id,
            @RequestParam String estado) {
        Reporte reporte = reporteService.actualizarEstadoReporte(id, estado);
        if (reporte != null) {
            return new ResponseEntity<>(reporte, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // Eliminar un reporte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable String id) {
        reporteService.eliminarReporte(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Obtener reportes por aviso
    @GetMapping("/aviso/{idAviso}")
    public ResponseEntity<List<Reporte>> obtenerReportesPorAviso(@PathVariable String idAviso) {
        return new ResponseEntity<>(reporteService.obtenerReportesPorAviso(idAviso), HttpStatus.OK);
    }
    
    // Obtener reportes por usuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Reporte>> obtenerReportesPorUsuario(@PathVariable String idUsuario) {
        return new ResponseEntity<>(reporteService.obtenerReportesPorUsuario(idUsuario), HttpStatus.OK);
    }
    
    // Eliminar reportes por usuario
    @DeleteMapping("/usuario/{idUsuario}")
    public ResponseEntity<Void> eliminarReportesPorUsuario(@PathVariable String idUsuario) {
        reporteService.eliminarReportesPorUsuario(idUsuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Eliminar reportes por aviso
    @DeleteMapping("/aviso/{idAviso}")
    public ResponseEntity<Void> eliminarReportesPorAviso(@PathVariable String idAviso) {
        reporteService.eliminarReportesPorAviso(idAviso);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}