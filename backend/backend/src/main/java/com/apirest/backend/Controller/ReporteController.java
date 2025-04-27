package com.apirest.backend.Controller;

import com.apirest.backend.Model.Reporte;
import com.apirest.backend.Service.IReporteService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/UAO/api/reportes")
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
        try {
            ObjectId objectId = new ObjectId(id);
            Reporte reporte = reporteService.obtenerReportePorId(objectId);
            if (reporte != null) {
                return new ResponseEntity<>(reporte, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Actualizar estado de un reporte (para administradores)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Reporte> actualizarEstadoReporte(
            @PathVariable String id,
            @RequestParam String estado) {
        try {
            ObjectId objectId = new ObjectId(id);
            Reporte reporte = reporteService.actualizarEstadoReporte(objectId, estado);
            if (reporte != null) {
                return new ResponseEntity<>(reporte, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar un reporte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            reporteService.eliminarReporte(objectId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener reportes por aviso
    @GetMapping("/aviso/{idAviso}")
    public ResponseEntity<List<Reporte>> obtenerReportesPorAviso(@PathVariable String idAviso) {
        try {
            ObjectId objectId = new ObjectId(idAviso);
            return new ResponseEntity<>(reporteService.obtenerReportesPorAviso(objectId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener reportes por usuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Reporte>> obtenerReportesPorUsuario(@PathVariable String idUsuario) {
        try {
            ObjectId objectId = new ObjectId(idUsuario);
            return new ResponseEntity<>(reporteService.obtenerReportesPorUsuario(objectId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar reportes por usuario
    @DeleteMapping("/usuario/{idUsuario}")
    public ResponseEntity<Void> eliminarReportesPorUsuario(@PathVariable String idUsuario) {
        try {
            ObjectId objectId = new ObjectId(idUsuario);
            reporteService.eliminarReportesPorUsuario(objectId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar reportes por aviso
    @DeleteMapping("/aviso/{idAviso}")
    public ResponseEntity<Void> eliminarReportesPorAviso(@PathVariable String idAviso) {
        try {
            ObjectId objectId = new ObjectId(idAviso);
            reporteService.eliminarReportesPorAviso(objectId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}