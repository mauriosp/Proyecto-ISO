package com.apirest.backend.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.backend.Dto.CancelarArrendamientoDTO;
import com.apirest.backend.Dto.CrearArrendamientoDTO;
import com.apirest.backend.Dto.ModificarArrendamientoDTO;
import com.apirest.backend.Model.Arrendamiento;
import com.apirest.backend.Model.AuditoriaArrendamiento;
import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Repository.AvisoRepository;
import com.apirest.backend.Repository.EspacioRepository;
import com.apirest.backend.Repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArrendamientoServiceImpl implements IArrendamientoService {

    @Autowired
    private EspacioRepository espacioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AvisoRepository avisoRepository;
    
    @Autowired
    private IMensajeService mensajeService;
    
    @Autowired
    private EmailBrevoServiceImpl emailService;

    @Override
    public Arrendamiento registrarArrendamiento(CrearArrendamientoDTO dto) {
        // 1. Validaciones iniciales
        validarDatosCreacion(dto);
        
        // 2. Verificar que el espacio existe y pertenece al propietario
        Espacio espacio = espacioRepository.findById(dto.getIdEspacio())
            .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
        
        // 3. Verificar que el arrendatario existe
        Usuario arrendatario = usuarioRepository.findById(dto.getIdArrendatario())
            .orElseThrow(() -> new IllegalArgumentException("Arrendatario no encontrado"));
        
        if (!"Interesado".equals(arrendatario.getTipoUsuario())) {
            throw new IllegalArgumentException("El usuario debe ser de tipo 'Interesado' para ser arrendatario");
        }
        
        // 4. Verificar disponibilidad del espacio
        if (!verificarDisponibilidadEspacio(dto.getIdEspacio(), dto.getFechaInicio(), dto.getFechaSalida())) {
            throw new IllegalArgumentException("El espacio no está disponible para las fechas seleccionadas");
        }
        
        // 5. Crear el arrendamiento
        Arrendamiento arrendamiento = new Arrendamiento();
        arrendamiento.setId(new ObjectId());
        arrendamiento.setIdUsuario(new ObjectId(dto.getIdArrendatario()));
        arrendamiento.setFechaInicio(dto.getFechaInicio());
        arrendamiento.setFechaSalida(dto.getFechaSalida());
        arrendamiento.setTerminos(dto.getTerminos());
        arrendamiento.setMontoMensual(dto.getMontoMensual());
        arrendamiento.setEstado("Activo");
        arrendamiento.setFechaRegistro(new Date());
        
        // 6. Crear auditoría de creación (objeto único según tu esquema)
        AuditoriaArrendamiento auditoria = new AuditoriaArrendamiento();
        auditoria.setFechaModificacion(new Date());
        auditoria.setTipoOperacion("Creacion");
        auditoria.setCampoModificado("fechaInicio"); 
        auditoria.setValorAnterior("");
        auditoria.setValorNuevo("Arrendamiento creado");
        auditoria.setMotivoModificacion("Registro inicial del acuerdo de arrendamiento");
        auditoria.setUsuarioModificador(espacio.getIdPropietario());
        arrendamiento.setAuditoriaArrendamiento(auditoria);
        
        // 7. Actualizar el espacio
        if (espacio.getArrendamiento() == null) {
            espacio.setArrendamiento(new ArrayList<>());
        }
        espacio.getArrendamiento().add(arrendamiento);
        espacio.setEstado("Ocupado");
        
        // 8. Guardar cambios
        espacioRepository.save(espacio);
        
        // 9. Inactivar avisos relacionados
        inactivarAvisosDelEspacio(dto.getIdEspacio());
        
        // 10. Enviar notificaciones
        enviarNotificacionesRegistro(espacio, arrendatario, arrendamiento);
        
        log.info("Arrendamiento registrado exitosamente: espacio={}, arrendatario={}", 
                dto.getIdEspacio(), dto.getIdArrendatario());
        
        return arrendamiento;
    }

    @Override
    public Arrendamiento modificarArrendamiento(ModificarArrendamientoDTO dto) {
        // 1. Validaciones iniciales
        validarDatosModificacion(dto);
        
        // 2. Buscar el espacio
        Espacio espacio = espacioRepository.findById(dto.getIdEspacio())
            .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
        
        // 3. Buscar el arrendamiento específico
        Arrendamiento arrendamiento = encontrarArrendamientoEnEspacio(espacio, dto.getIdArrendatario());
        
        // 4. Verificar que puede ser modificado
        if (!arrendamiento.puedeSerModificado()) {
            throw new IllegalArgumentException("No se puede modificar un arrendamiento en estado: " + arrendamiento.getEstado());
        }
        
        // 5. Determinar qué campo se está modificando principalmente
        String campoModificado = determinarCampoModificado(arrendamiento, dto);
        String valorAnterior = obtenerValorAnterior(arrendamiento, campoModificado);
        String valorNuevo = obtenerValorNuevo(dto, campoModificado);
        
        // 6. Aplicar las modificaciones
        if (dto.getFechaInicio() != null) {
            arrendamiento.setFechaInicio(dto.getFechaInicio());
        }
        
        if (dto.getFechaSalida() != null) {
            arrendamiento.setFechaSalida(dto.getFechaSalida());
        }
        
        if (dto.getTerminos() != null) {
            arrendamiento.setTerminos(dto.getTerminos());
        }
        
        if (dto.getMontoMensual() != null) {
            arrendamiento.setMontoMensual(dto.getMontoMensual());
        }
        
        // 7. Actualizar auditoría (sobrescribir la anterior según tu esquema)
        AuditoriaArrendamiento auditoria = new AuditoriaArrendamiento();
        auditoria.setFechaModificacion(new Date());
        auditoria.setTipoOperacion("Modificacion");
        auditoria.setCampoModificado(campoModificado);
        auditoria.setValorAnterior(valorAnterior);
        auditoria.setValorNuevo(valorNuevo);
        auditoria.setMotivoModificacion(dto.getMotivoModificacion());
        auditoria.setUsuarioModificador(espacio.getIdPropietario());
        arrendamiento.setAuditoriaArrendamiento(auditoria);
        
        // 8. Guardar cambios
        espacioRepository.save(espacio);
        
        // 9. Enviar notificaciones
        enviarNotificacionesModificacion(espacio, arrendamiento);
        
        log.info("Arrendamiento modificado exitosamente: espacio={}, arrendatario={}", 
                dto.getIdEspacio(), dto.getIdArrendatario());
        
        return arrendamiento;
    }

    @Override
    public Arrendamiento cancelarArrendamiento(CancelarArrendamientoDTO dto) {
        // 1. Validaciones iniciales
        if (dto.getMotivoCancelacion() == null || dto.getMotivoCancelacion().trim().length() < 20) {
            throw new IllegalArgumentException("El motivo de cancelación debe tener al menos 20 caracteres");
        }
        
        // 2. Buscar el espacio
        Espacio espacio = espacioRepository.findById(dto.getIdEspacio())
            .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
        
        // 3. Buscar el arrendamiento específico
        Arrendamiento arrendamiento = encontrarArrendamientoEnEspacio(espacio, dto.getIdArrendatario());
        
        // 4. Verificar que puede ser cancelado
        if (!arrendamiento.puedeSerCancelado()) {
            throw new IllegalArgumentException("No se puede cancelar un arrendamiento en estado: " + arrendamiento.getEstado());
        }
        
        // 5. Actualizar el arrendamiento
        String estadoAnterior = arrendamiento.getEstado();
        arrendamiento.setEstado("Cancelado");
        arrendamiento.setMotivoCancelacion(dto.getMotivoCancelacion());
        arrendamiento.setFechaCancelacion(new Date());
        
        // 6. Registrar en auditoría
        AuditoriaArrendamiento auditoria = new AuditoriaArrendamiento();
        auditoria.setFechaModificacion(new Date());
        auditoria.setTipoOperacion("Cancelacion");
        auditoria.setCampoModificado("estado");
        auditoria.setValorAnterior(estadoAnterior);
        auditoria.setValorNuevo("Cancelado");
        auditoria.setMotivoModificacion(dto.getMotivoCancelacion());
        auditoria.setUsuarioModificador(espacio.getIdPropietario());
        arrendamiento.setAuditoriaArrendamiento(auditoria);
        
        // 7. Actualizar estado del espacio
        espacio.setEstado("Disponible");
        
        // 8. Guardar cambios
        espacioRepository.save(espacio);
        
        // 9. Republicar avisos del espacio
        republicarAvisosDelEspacio(dto.getIdEspacio());
        
        // 10. Enviar notificaciones
        enviarNotificacionesCancelacion(espacio, arrendamiento, dto.getMotivoCancelacion());
        
        log.info("Arrendamiento cancelado exitosamente: espacio={}, arrendatario={}, motivo={}", 
                dto.getIdEspacio(), dto.getIdArrendatario(), dto.getMotivoCancelacion());
        
        return arrendamiento;
    }

    @Override
    public List<Arrendamiento> obtenerArrendamientosActivosPorPropietario(String idPropietario) {
        // Usar query optimizada 
        List<Espacio> espacios = espacioRepository.findByIdPropietario(new ObjectId(idPropietario));
        
        List<Arrendamiento> arrendamientosActivos = new ArrayList<>();
        
        for (Espacio espacio : espacios) {
            if (espacio.getArrendamiento() != null) {
                List<Arrendamiento> activos = espacio.getArrendamiento().stream()
                    .filter(arrendamiento -> "Activo".equals(arrendamiento.getEstado()))
                    .collect(Collectors.toList());
                arrendamientosActivos.addAll(activos);
            }
        }
        
        return arrendamientosActivos;
    }

    @Override
    public List<Arrendamiento> obtenerArrendamientosPorEspacio(String idEspacio) {
        Optional<Espacio> espacioOpt = espacioRepository.findById(idEspacio);
        if (espacioOpt.isPresent() && espacioOpt.get().getArrendamiento() != null) {
            return espacioOpt.get().getArrendamiento();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean verificarDisponibilidadEspacio(String idEspacio, Date fechaInicio, Date fechaFin) {
        Optional<Espacio> espacioOpt = espacioRepository.findById(idEspacio);
        
        if (espacioOpt.isEmpty()) {
            return false;
        }
        
        Espacio espacio = espacioOpt.get();
        
        // Verificar que no esté en mantenimiento
        if ("Mantenimiento".equals(espacio.getEstado())) {
            return false;
        }
        
        // Verificar que no haya arrendamientos activos que se superpongan
        if (espacio.getArrendamiento() != null) {
            for (Arrendamiento arr : espacio.getArrendamiento()) {
                if ("Activo".equals(arr.getEstado()) || "Pendiente".equals(arr.getEstado())) {
                    // Verificar superposición de fechas
                    if (fechasSeSuperponen(fechaInicio, fechaFin, arr.getFechaInicio(), arr.getFechaSalida())) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    
    private String determinarCampoModificado(Arrendamiento arrendamiento, ModificarArrendamientoDTO dto) {
        if (dto.getFechaInicio() != null && !dto.getFechaInicio().equals(arrendamiento.getFechaInicio())) {
            return "fechaInicio";
        }
        if (dto.getFechaSalida() != null && !dto.getFechaSalida().equals(arrendamiento.getFechaSalida())) {
            return "fechaSalida";
        }
        if (dto.getTerminos() != null && !dto.getTerminos().equals(arrendamiento.getTerminos())) {
            return "terminos";
        }
        if (dto.getMontoMensual() != null && !dto.getMontoMensual().equals(arrendamiento.getMontoMensual())) {
            return "montoMensual";
        }
        return "terminos"; // Por defecto
    }
    
    private String obtenerValorAnterior(Arrendamiento arrendamiento, String campo) {
        return switch (campo) {
            case "fechaInicio" -> arrendamiento.getFechaInicio() != null ? arrendamiento.getFechaInicio().toString() : "";
            case "fechaSalida" -> arrendamiento.getFechaSalida() != null ? arrendamiento.getFechaSalida().toString() : "";
            case "terminos" -> arrendamiento.getTerminos() != null ? arrendamiento.getTerminos() : "";
            case "montoMensual" -> arrendamiento.getMontoMensual() != null ? arrendamiento.getMontoMensual().toString() : "";
            default -> "";
        };
    }
    
    private String obtenerValorNuevo(ModificarArrendamientoDTO dto, String campo) {
        return switch (campo) {
            case "fechaInicio" -> dto.getFechaInicio() != null ? dto.getFechaInicio().toString() : "";
            case "fechaSalida" -> dto.getFechaSalida() != null ? dto.getFechaSalida().toString() : "";
            case "terminos" -> dto.getTerminos() != null ? dto.getTerminos() : "";
            case "montoMensual" -> dto.getMontoMensual() != null ? dto.getMontoMensual().toString() : "";
            default -> "";
        };
    }
    
    private void validarDatosCreacion(CrearArrendamientoDTO dto) {
        if (dto.getIdEspacio() == null || dto.getIdEspacio().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del espacio es obligatorio");
        }
        
        if (dto.getIdArrendatario() == null || dto.getIdArrendatario().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del arrendatario es obligatorio");
        }
        
        if (dto.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }
        
        if (dto.getFechaSalida() == null) {
            throw new IllegalArgumentException("La fecha de salida es obligatoria");
        }
        
        if (dto.getFechaInicio().after(dto.getFechaSalida())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de salida");
        }
        
        if (dto.getFechaInicio().before(new Date())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a la fecha actual");
        }
        
        if (dto.getMontoMensual() == null || dto.getMontoMensual() <= 0) {
            throw new IllegalArgumentException("El monto mensual debe ser mayor a 0");
        }
        
        if (dto.getTerminos() == null || dto.getTerminos().trim().isEmpty()) {
            throw new IllegalArgumentException("Los términos del arrendamiento son obligatorios");
        }
    }
    
    private void validarDatosModificacion(ModificarArrendamientoDTO dto) {
        if (dto.getIdEspacio() == null || dto.getIdEspacio().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del espacio es obligatorio");
        }
        
        if (dto.getIdArrendatario() == null || dto.getIdArrendatario().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del arrendatario es obligatorio");
        }
        
        if (dto.getFechaInicio() != null && dto.getFechaSalida() != null) {
            if (dto.getFechaInicio().after(dto.getFechaSalida())) {
                throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de salida");
            }
        }
        
        if (dto.getMontoMensual() != null && dto.getMontoMensual() <= 0) {
            throw new IllegalArgumentException("El monto mensual debe ser mayor a 0");
        }
    }
    
    private Arrendamiento encontrarArrendamientoEnEspacio(Espacio espacio, String idArrendatario) {
        if (espacio.getArrendamiento() == null || espacio.getArrendamiento().isEmpty()) {
            throw new IllegalArgumentException("No se encontraron arrendamientos en este espacio");
        }
        
        return espacio.getArrendamiento().stream()
            .filter(arr -> arr.getIdUsuario().toHexString().equals(idArrendatario))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No se encontró un arrendamiento activo para este arrendatario"));
    }
    
    private boolean fechasSeSuperponen(Date inicio1, Date fin1, Date inicio2, Date fin2) {
        return inicio1.before(fin2) && fin1.after(inicio2);
    }
    
    private void inactivarAvisosDelEspacio(String idEspacio) {
        List<Aviso> avisos = avisoRepository.findByIdEspacio(new ObjectId(idEspacio));
        for (Aviso aviso : avisos) {
            aviso.setEstado("Inactivo");
            avisoRepository.save(aviso);
        }
    }
    
    private void republicarAvisosDelEspacio(String idEspacio) {
        List<Aviso> avisos = avisoRepository.findByIdEspacio(new ObjectId(idEspacio));
        for (Aviso aviso : avisos) {
            aviso.setEstado("Activo");
            avisoRepository.save(aviso);
        }
    }
    
    private void enviarNotificacionesRegistro(Espacio espacio, Usuario arrendatario, Arrendamiento arrendamiento) {
        try {
            String mensajeArrendatario = String.format(
                "Su acuerdo de arrendamiento ha sido registrado exitosamente. " +
                "Espacio: %s, Fecha inicio: %s, Fecha fin: %s, Monto: $%.2f",
                espacio.getDireccion(), 
                new SimpleDateFormat("dd/MM/yyyy").format(arrendamiento.getFechaInicio()),
                new SimpleDateFormat("dd/MM/yyyy").format(arrendamiento.getFechaSalida()),
                arrendamiento.getMontoMensual()
            );
            
            List<Aviso> avisos = avisoRepository.findByIdEspacio(espacio.getId());
            String avisoId = avisos.isEmpty() ? "N/A" : avisos.get(0).getId().toHexString();
            
            mensajeService.enviarMensaje(arrendatario.getId().toHexString(), mensajeArrendatario, avisoId);
            
            String htmlEmail = emailService.crearEmailVerificacion(
                arrendatario.getNombre(),
                "Su acuerdo de arrendamiento ha sido confirmado para la propiedad en " + espacio.getDireccion()
            );
            emailService.enviarEmail(arrendatario.getEmail(), 
                "Confirmación de Arrendamiento - Santorini Hills", htmlEmail);
            
        } catch (Exception e) {
            log.error("Error enviando notificaciones de registro: {}", e.getMessage());
        }
    }
    
    private void enviarNotificacionesModificacion(Espacio espacio, Arrendamiento arrendamiento) {
        try {
            String mensaje = String.format(
                "Su acuerdo de arrendamiento ha sido modificado. " +
                "Espacio: %s. Por favor revise los nuevos términos.",
                espacio.getDireccion()
            );
            
            List<Aviso> avisos = avisoRepository.findByIdEspacio(espacio.getId());
            String avisoId = avisos.isEmpty() ? "N/A" : avisos.get(0).getId().toHexString();
            
            mensajeService.enviarMensaje(arrendamiento.getIdUsuario().toHexString(), mensaje, avisoId);
            
        } catch (Exception e) {
            log.error("Error enviando notificaciones de modificación: {}", e.getMessage());
        }
    }
    
    private void enviarNotificacionesCancelacion(Espacio espacio, Arrendamiento arrendamiento, String motivo) {
        try {
            String mensaje = String.format(
                "Su acuerdo de arrendamiento ha sido cancelado. " +
                "Espacio: %s. Motivo: %s",
                espacio.getDireccion(), motivo
            );
            
            List<Aviso> avisos = avisoRepository.findByIdEspacio(espacio.getId());
            String avisoId = avisos.isEmpty() ? "N/A" : avisos.get(0).getId().toHexString();
            
            mensajeService.enviarMensaje(arrendamiento.getIdUsuario().toHexString(), mensaje, avisoId);
            
        } catch (Exception e) {
            log.error("Error enviando notificaciones de cancelación: {}", e.getMessage());
        }
    }
}