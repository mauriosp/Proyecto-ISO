package com.apirest.backend.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Service.IAvisoService;

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
            @RequestParam int baños,
            @RequestParam int habitaciones,
            @RequestParam List<String> imagenes, // Cambiado de MultipartFile a String
            @RequestParam String titulo,
            @RequestParam String direccion,
            @RequestParam BigDecimal area,
            @RequestParam String idUsuario,
            @RequestParam List<String> extraInfo) {
        try {
            // Validaciones preliminares
            if (titulo.length() > 100) {
                return new ResponseEntity<>("El título no puede exceder los 100 caracteres.", HttpStatus.BAD_REQUEST);
            }
            if (descripcion.length() > 500) {
                return new ResponseEntity<>("La descripción no puede exceder los 500 caracteres.", HttpStatus.BAD_REQUEST);
            }
            if (precioMensual <= 0) {
                return new ResponseEntity<>("El precio mensual debe ser un valor numérico positivo.", HttpStatus.BAD_REQUEST);
            }

            avisoService.crearAviso(descripcion, precioMensual, imagenes, titulo, tipoEspacio, habitaciones, baños, direccion, area, idUsuario, extraInfo);
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
            @RequestParam String tipoEspacio,
            @RequestParam String descripcion,
            @RequestParam double precioMensual,
            @RequestParam int baños,
            @RequestParam int habitaciones,
            @RequestParam List<String> imagenes, // Cambiado de MultipartFile a String
            @RequestParam String titulo,
            @RequestParam String direccion,
            @RequestParam BigDecimal area,
            @RequestParam List<String> extraInfo) {
        try {
            // Validaciones preliminares
            if (titulo != null && titulo.length() > 100) {
                return new ResponseEntity<>("El título no puede exceder los 100 caracteres.", HttpStatus.BAD_REQUEST);
            }
            if (descripcion != null && descripcion.length() > 500) {
                return new ResponseEntity<>("La descripción no puede exceder los 500 caracteres.", HttpStatus.BAD_REQUEST);
            }
            if (precioMensual <= 0) {
                return new ResponseEntity<>("El precio mensual debe ser un valor numérico positivo.", HttpStatus.BAD_REQUEST);
            }
            if (direccion == null || direccion.trim().isEmpty()) {
                return new ResponseEntity<>("La dirección no puede estar vacía.", HttpStatus.BAD_REQUEST);
            }
            if (area == null || area.doubleValue() <= 0) {
                return new ResponseEntity<>("El área debe ser mayor a 0.", HttpStatus.BAD_REQUEST);
            }

            // Llamar al servicio para editar el aviso
            avisoService.editarAviso(id, titulo, descripcion, precioMensual, imagenes, tipoEspacio, direccion, area, habitaciones, baños, extraInfo);
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

    @GetMapping("/moderacion/listar")
    public ResponseEntity<List<Aviso>> listarAvisosParaModeracion() {
        try {
            List<Aviso> avisos = avisoService.listarAvisosParaModeracion();
            return new ResponseEntity<>(avisos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/moderacion/desactivar/{id}")
    public ResponseEntity<String> desactivarAviso(
            @PathVariable String id,
            @RequestParam String motivo) {
        try {
            if (motivo == null || motivo.trim().isEmpty()) {
                return new ResponseEntity<>("El motivo de desactivación no puede estar vacío", HttpStatus.BAD_REQUEST);
            }
            avisoService.desactivarAviso(id, motivo);
            return new ResponseEntity<>("Aviso desactivado correctamente", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al desactivar el aviso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/moderacion/reactivar/{id}")
    public ResponseEntity<String> reactivarAviso(@PathVariable String id) {
        try {
            avisoService.reactivarAviso(id);
            return new ResponseEntity<>("Aviso reactivado correctamente", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al reactivar el aviso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminar-por-propietario/{idPropietario}")
    public ResponseEntity<String> eliminarAvisosPorPropietario(@PathVariable String idPropietario) {
        try {
            avisoService.eliminarAvisosPorPropietario(idPropietario);
            return new ResponseEntity<>("Avisos eliminados correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar avisos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Aviso>> filtrarAvisos(
            @RequestParam(required = false) String tipoEspacio,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) String disponibilidad) {
        List<Aviso> avisosFiltrados = avisoService.filtrarAvisos(tipoEspacio, precioMin, precioMax, disponibilidad);
        return ResponseEntity.ok(avisosFiltrados);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Aviso> buscarAvisoPorId(@PathVariable String id) {
        try {
            Aviso aviso = avisoService.buscarAvisoPorId(id);
            if (aviso == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(aviso, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}