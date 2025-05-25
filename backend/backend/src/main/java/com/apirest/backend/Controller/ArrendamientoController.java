package com.apirest.backend.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Dto.CancelarArrendamientoDTO;
import com.apirest.backend.Dto.CrearArrendamientoDTO;
import com.apirest.backend.Dto.ModificarArrendamientoDTO;
import com.apirest.backend.Model.Arrendamiento;
import com.apirest.backend.Service.IArrendamientoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/UAO/apirest/Arrendamiento")
@CrossOrigin(origins = "*")
@Slf4j
public class ArrendamientoController {

    @Autowired
    private IArrendamientoService arrendamientoService;

    /**
     * Registrar Acuerdo de Arrendamiento
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarArrendamiento(@RequestBody CrearArrendamientoDTO crearArrendamientoDTO) {
        try {
            Arrendamiento arrendamiento = arrendamientoService.registrarArrendamiento(crearArrendamientoDTO);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Acuerdo de arrendamiento registrado exitosamente");
            respuesta.put("arrendamiento", arrendamiento);
            respuesta.put("idEspacio", crearArrendamientoDTO.getIdEspacio());
            respuesta.put("idArrendatario", crearArrendamientoDTO.getIdArrendatario());
            
            log.info("Arrendamiento registrado exitosamente: espacio={}, arrendatario={}", 
                    crearArrendamientoDTO.getIdEspacio(), crearArrendamientoDTO.getIdArrendatario());
            
            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al registrar arrendamiento: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(crearRespuestaError("Error de validación", e.getMessage()));
                
        } catch (Exception e) {
            log.error("Error interno al registrar arrendamiento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor", 
                    "Ha ocurrido un error inesperado al registrar el arrendamiento"));
        }
    }

    /**
     * Modificar Acuerdo de Arrendamiento
     */
    @PutMapping("/modificar")
    public ResponseEntity<?> modificarArrendamiento(@RequestBody ModificarArrendamientoDTO modificarArrendamientoDTO) {
        try {
            Arrendamiento arrendamiento = arrendamientoService.modificarArrendamiento(modificarArrendamientoDTO);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Acuerdo de arrendamiento modificado exitosamente");
            respuesta.put("arrendamiento", arrendamiento);
            respuesta.put("auditoriaModificacion", arrendamiento.getAuditoriaArrendamiento());
            
            log.info("Arrendamiento modificado exitosamente: espacio={}, arrendatario={}", 
                    modificarArrendamientoDTO.getIdEspacio(), modificarArrendamientoDTO.getIdArrendatario());
            
            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al modificar arrendamiento: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(crearRespuestaError("Error de validación", e.getMessage()));
                
        } catch (Exception e) {
            log.error("Error interno al modificar arrendamiento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor", 
                    "Ha ocurrido un error inesperado al modificar el arrendamiento"));
        }
    }

    /**
     * Cancelar Acuerdo de Arrendamiento
     */
    @PutMapping("/cancelar")
    public ResponseEntity<?> cancelarArrendamiento(@RequestBody CancelarArrendamientoDTO cancelarArrendamientoDTO) {
        try {
            Arrendamiento arrendamiento = arrendamientoService.cancelarArrendamiento(cancelarArrendamientoDTO);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Acuerdo de arrendamiento cancelado exitosamente");
            respuesta.put("arrendamiento", arrendamiento);
            respuesta.put("motivoCancelacion", arrendamiento.getMotivoCancelacion());
            respuesta.put("fechaCancelacion", arrendamiento.getFechaCancelacion());
            respuesta.put("espacioDisponible", true);
            
            log.info("Arrendamiento cancelado exitosamente: espacio={}, arrendatario={}, motivo={}", 
                    cancelarArrendamientoDTO.getIdEspacio(), 
                    cancelarArrendamientoDTO.getIdArrendatario(),
                    cancelarArrendamientoDTO.getMotivoCancelacion());
            
            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al cancelar arrendamiento: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(crearRespuestaError("Error de validación", e.getMessage()));
                
        } catch (Exception e) {
            log.error("Error interno al cancelar arrendamiento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor", 
                    "Ha ocurrido un error inesperado al cancelar el arrendamiento"));
        }
    }

    /**
     * Obtener arrendamientos activos de un propietario
     */
    @GetMapping("/propietario/{idPropietario}/activos")
    public ResponseEntity<?> obtenerArrendamientosActivosPropietario(@PathVariable String idPropietario) {
        try {
            List<Arrendamiento> arrendamientos = arrendamientoService
                .obtenerArrendamientosActivosPorPropietario(idPropietario);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("arrendamientos", arrendamientos);
            respuesta.put("totalActivos", arrendamientos.size());
            respuesta.put("propietario", idPropietario);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            log.error("Error al obtener arrendamientos activos del propietario {}: {}", 
                    idPropietario, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor", 
                    "Error al obtener los arrendamientos activos"));
        }
    }

    /**
     * Obtener todos los arrendamientos de un espacio específico
     */
    @GetMapping("/espacio/{idEspacio}")
    public ResponseEntity<?> obtenerArrendamientosPorEspacio(@PathVariable String idEspacio) {
        try {
            List<Arrendamiento> arrendamientos = arrendamientoService.obtenerArrendamientosPorEspacio(idEspacio);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("arrendamientos", arrendamientos);
            respuesta.put("totalArrendamientos", arrendamientos.size());
            respuesta.put("espacio", idEspacio);
            
            // Estadísticas adicionales
            long activos = arrendamientos.stream().filter(a -> "Activo".equals(a.getEstado())).count();
            long completados = arrendamientos.stream().filter(a -> "Completado".equals(a.getEstado())).count();
            long cancelados = arrendamientos.stream().filter(a -> "Cancelado".equals(a.getEstado())).count();
            
            Map<String, Long> estadisticas = new HashMap<>();
            estadisticas.put("activos", activos);
            estadisticas.put("completados", completados);
            estadisticas.put("cancelados", cancelados);
            respuesta.put("estadisticas", estadisticas);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            log.error("Error al obtener arrendamientos del espacio {}: {}", idEspacio, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor", 
                    "Error al obtener los arrendamientos del espacio"));
        }
    }

    /**
     * Verificar disponibilidad de un espacio
     */
    @PostMapping("/verificar-disponibilidad")
    public ResponseEntity<?> verificarDisponibilidad(@RequestBody Map<String, Object> request) {
        try {
            String idEspacio = (String) request.get("idEspacio");
            String fechaInicioStr = (String) request.get("fechaInicio");
            String fechaSalidaStr = (String) request.get("fechaSalida");
            
            // Convertir fechas (asumiendo formato ISO)
            java.util.Date fechaInicio = java.sql.Date.valueOf(fechaInicioStr);
            java.util.Date fechaSalida = java.sql.Date.valueOf(fechaSalidaStr);
            
            boolean disponible = arrendamientoService.verificarDisponibilidadEspacio(
                idEspacio, fechaInicio, fechaSalida);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("disponible", disponible);
            respuesta.put("espacio", idEspacio);
            respuesta.put("fechaInicio", fechaInicio);
            respuesta.put("fechaSalida", fechaSalida);
            respuesta.put("mensaje", disponible ? 
                "El espacio está disponible para las fechas seleccionadas" : 
                "El espacio no está disponible para las fechas seleccionadas");
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            log.error("Error al verificar disponibilidad: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(crearRespuestaError("Error de validación", 
                    "Error al verificar la disponibilidad del espacio"));
        }
    }

    /**
     * Buscar arrendamientos que pueden ser modificados por un propietario
     */
    @GetMapping("/propietario/{idPropietario}/modificables")
    public ResponseEntity<?> obtenerArrendamientosModificables(@PathVariable String idPropietario) {
        try {
            List<Arrendamiento> arrendamientos = arrendamientoService
                .obtenerArrendamientosActivosPorPropietario(idPropietario);
            
            // Filtrar solo los que pueden ser modificados
            List<Arrendamiento> modificables = arrendamientos.stream()
                .filter(Arrendamiento::puedeSerModificado)
                .toList();
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("arrendamientos", modificables);
            respuesta.put("totalModificables", modificables.size());
            respuesta.put("propietario", idPropietario);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            log.error("Error al obtener arrendamientos modificables del propietario {}: {}", 
                    idPropietario, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor", 
                    "Error al obtener los arrendamientos modificables"));
        }
    }

    /**
     * Obtener auditoría de un arrendamiento específico
     */
    @GetMapping("/espacio/{idEspacio}/arrendatario/{idArrendatario}/auditoria")
    public ResponseEntity<?> obtenerAuditoriaArrendamiento(
            @PathVariable String idEspacio, 
            @PathVariable String idArrendatario) {
        try {
            List<Arrendamiento> arrendamientos = arrendamientoService.obtenerArrendamientosPorEspacio(idEspacio);
            
            Arrendamiento arrendamiento = arrendamientos.stream()
                .filter(arr -> arr.getIdUsuario().toHexString().equals(idArrendatario))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Arrendamiento no encontrado"));
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("auditoriaArrendamiento", arrendamiento.getAuditoriaArrendamiento());
            respuesta.put("arrendamiento", arrendamiento);
            respuesta.put("tieneAuditoria", arrendamiento.getAuditoriaArrendamiento() != null);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(crearRespuestaError("Arrendamiento no encontrado", e.getMessage()));
                
        } catch (Exception e) {
            log.error("Error al obtener auditoría de arrendamiento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor", 
                    "Error al obtener la auditoría del arrendamiento"));
        }
    }

    // Método auxiliar para crear respuestas de error consistentes
    private Map<String, Object> crearRespuestaError(String tipo, String mensaje) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", tipo);
        error.put("mensaje", mensaje);
        error.put("timestamp", new java.util.Date());
        return error;
    }
}