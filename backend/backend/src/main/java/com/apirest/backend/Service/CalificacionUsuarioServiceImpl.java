package com.apirest.backend.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.backend.Model.Arrendamiento;
import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Model.CalificacionUsuario;
import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Repository.AvisoRepository;
import com.apirest.backend.Repository.EspacioRepository;
import com.apirest.backend.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalificacionUsuarioServiceImpl implements ICalificacionUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EspacioRepository espacioRepository;
    
    @Autowired
    private IMensajeService mensajeService;

    @Autowired
    private AvisoRepository avisoRepository;

    @Override
    public void calificarArrendatario(String idPropietario, String idArrendatario, 
                                    int puntuacion, String comentario, String idArrendamiento) {
        
        // Validaciones
        validarParametrosCalificacion(idPropietario, idArrendatario, puntuacion);
        validarComentario(comentario);
        
        // Verificar que los usuarios existan
        Usuario propietario = obtenerUsuario(idPropietario);
        Usuario arrendatario = obtenerUsuario(idArrendatario);
        
        // Validar que sea un propietario
        if (!"Propietario".equals(propietario.getTipoUsuario())) {
            throw new IllegalArgumentException("Solo los propietarios pueden calificar arrendatarios");
        }
        
        // Verificar que el propietario puede calificar a este arrendatario
        if (!puedeCalificar(idPropietario, idArrendatario, idArrendamiento)) {
            throw new IllegalArgumentException("Solo puede calificar después de que el arrendamiento haya finalizado");
        }
        
        // Verificar que no haya calificado ya a este arrendatario para este arrendamiento
        if (yaCalificoEnEsteArrendamiento(arrendatario, idPropietario, idArrendamiento)) {
            throw new IllegalArgumentException("Ya has calificado a este arrendatario para este arrendamiento");
        }
        
        // Crear la calificación
        CalificacionUsuario calificacion = new CalificacionUsuario();
        calificacion.setIdUsuarioCalifica(new ObjectId(idPropietario));
        calificacion.setPuntuacion(String.valueOf(puntuacion));
        calificacion.setFecha(new Date());
        calificacion.setComentario(comentario != null ? comentario : "");
        
        calificacion.setComentario(comentario != null ? comentario : "");
        
        // Agregar la calificación al arrendatario
        if (arrendatario.getCalificacionUsuario() == null) {
            arrendatario.setCalificacionUsuario(new ArrayList<>());
        }
        arrendatario.getCalificacionUsuario().add(calificacion);
        
        // Actualizar el promedio de calificaciones
        actualizarPromedioCalificaciones(idArrendatario);
        
        // Guardar el usuario actualizado
        usuarioRepository.save(arrendatario);
        
        // Enviar notificación al arrendatario
        enviarNotificacionCalificacion(idArrendatario, puntuacion, comentario, "arrendatario");
        
        log.info("Calificación de arrendatario registrada: propietario={}, arrendatario={}, puntuación={}, arrendamiento={}", 
                idPropietario, idArrendatario, puntuacion, idArrendamiento);
    }

    @Override
    public void calificarPropietario(String idArrendatario, String idPropietario, 
                                   int puntuacion, String comentario, String idArrendamiento) {
        
        // Validaciones
        validarParametrosCalificacion(idArrendatario, idPropietario, puntuacion);
        validarComentario(comentario);
        
        // Verificar que los usuarios existan
        Usuario arrendatario = obtenerUsuario(idArrendatario);
        Usuario propietario = obtenerUsuario(idPropietario);
        
        // Validar que sea un interesado (arrendatario)
        if (!"Interesado".equals(arrendatario.getTipoUsuario())) {
            throw new IllegalArgumentException("Solo los interesados pueden calificar propietarios");
        }
        
        // Verificar que el arrendatario puede calificar a este propietario
        if (!puedeCalificar(idArrendatario, idPropietario, idArrendamiento)) {
            throw new IllegalArgumentException("Solo puede calificar después del período de arrendamiento");
        }
        
        // Verificar que no haya calificado ya a este propietario para este arrendamiento
        if (yaCalificoEnEsteArrendamiento(propietario, idArrendatario, idArrendamiento)) {
            throw new IllegalArgumentException("Ya has calificado a este propietario para este arrendamiento");
        }
        
        // Crear la calificación
        CalificacionUsuario calificacion = new CalificacionUsuario();
        calificacion.setIdUsuarioCalifica(new ObjectId(idArrendatario));
        calificacion.setPuntuacion(String.valueOf(puntuacion));
        calificacion.setFecha(new Date());
        calificacion.setComentario(comentario != null ? comentario : "");
        
        // Agregar la calificación al propietario
        if (propietario.getCalificacionUsuario() == null) {
            propietario.setCalificacionUsuario(new ArrayList<>());
        }
        propietario.getCalificacionUsuario().add(calificacion);
        
        // Actualizar el promedio de calificaciones
        actualizarPromedioCalificaciones(idPropietario);
        
        // Guardar el usuario actualizado
        usuarioRepository.save(propietario);
        
        // Enviar notificación al propietario
        enviarNotificacionCalificacion(idPropietario, puntuacion, comentario, "propietario");
        
        log.info("Calificación de propietario registrada: arrendatario={}, propietario={}, puntuación={}, arrendamiento={}", 
                idArrendatario, idPropietario, puntuacion, idArrendamiento);
    }

    @Override
    public List<CalificacionUsuario> obtenerCalificacionesUsuario(String idUsuario) {
        Usuario usuario = obtenerUsuario(idUsuario);
        return usuario.getCalificacionUsuario() != null ? 
               usuario.getCalificacionUsuario() : new ArrayList<>();
    }

    @Override
    public void actualizarPromedioCalificaciones(String idUsuario) {
        Usuario usuario = obtenerUsuario(idUsuario);
        
        if (usuario.getCalificacionUsuario() == null || usuario.getCalificacionUsuario().isEmpty()) {
            usuario.setPromCalificacion(0);
        } else {
            double suma = 0;
            int contador = 0;
            
            for (CalificacionUsuario calificacion : usuario.getCalificacionUsuario()) {
                try {
                    int puntuacion = calificacion.getPuntuacionAsInt();
                    suma += puntuacion;
                    contador++;
                } catch (Exception e) {
                    log.warn("Puntuación inválida encontrada para usuario {}: {}", 
                            idUsuario, calificacion.getPuntuacion());
                }
            }
            
            if (contador > 0) {
                int promedio = (int) Math.round(suma / contador);
                usuario.setPromCalificacion(promedio);
            } else {
                usuario.setPromCalificacion(0);
            }
        }
        
        usuarioRepository.save(usuario);
        log.info("Promedio de calificaciones actualizado para usuario {}: {}", 
                idUsuario, usuario.getPromCalificacion());
    }

    @Override
    public boolean puedeCalificar(String idCalificador, String idCalificado, String idArrendamiento) {
        try {
            // 1. VALIDACIÓN DE FINALIZACIÓN: Buscar el arrendamiento específico
            Arrendamiento arrendamiento = buscarArrendamientoPorUsuarios(idCalificador, idCalificado);
            
            if (arrendamiento == null) {
                log.warn("No se encontró arrendamiento entre usuarios {} y {}", idCalificador, idCalificado);
                return false;
            }
            
            // 2. VALIDACIÓN DE FINALIZACIÓN: Verificar que el arrendamiento está "Completado"
            if (!"Completado".equals(arrendamiento.getEstado())) {
                log.warn("El arrendamiento no está completado. Estado actual: {}", arrendamiento.getEstado());
                return false;
            }
            
            // 3. RESTRICCIÓN TEMPORAL: Verificar que la fecha de salida ya pasó
            Date fechaSalida = arrendamiento.getFechaSalida();
            Date ahora = new Date();
            
            if (ahora.before(fechaSalida)) {
                log.warn("No se puede calificar antes de que termine el arrendamiento. Fecha salida: {}, Fecha actual: {}", 
                        fechaSalida, ahora);
                return false;
            }
            
            // 4. RESTRICCIÓN TEMPORAL: Verificar ventana de calificación (máximo 30 días después de finalizar)
            long treintaDiasEnMillis = 30L * 24L * 60L * 60L * 1000L;
            long tiempoTranscurrido = ahora.getTime() - fechaSalida.getTime();
            
            if (tiempoTranscurrido > treintaDiasEnMillis) {
                log.warn("El período para calificar ha expirado. Arrendamiento terminó hace {} días", 
                        tiempoTranscurrido / (24L * 60L * 60L * 1000L));
                return false;
            }
            
            log.info("Usuario {} puede calificar a {} - arrendamiento completado y en ventana de tiempo válida", 
                    idCalificador, idCalificado);
            return true;
            
        } catch (Exception e) {
            log.error("Error al verificar permisos de calificación: {}", e.getMessage());
            return false;
        }
    }

    private boolean yaCalificoEnEsteArrendamiento(Usuario usuarioCalificado, String idCalificador, String idArrendamiento) {
        if (usuarioCalificado.getCalificacionUsuario() == null || usuarioCalificado.getCalificacionUsuario().isEmpty()) {
            return false;
        }
        
        // PREVENCIÓN DE DUPLICADOS MEJORADA: 
        // Buscar el arrendamiento específico para obtener sus fechas únicas
        Arrendamiento arrendamiento = buscarArrendamientoPorUsuarios(idCalificador, 
            usuarioCalificado.getId().toHexString());
            
        if (arrendamiento == null) {
            log.debug("No se encontró arrendamiento para validar duplicados");
            return false;
        }
        
        // Verificar si ya existe una calificación del mismo calificador para este arrendamiento específico
        // Usamos las fechas del arrendamiento como identificador único
        boolean yaCalifico = usuarioCalificado.getCalificacionUsuario().stream()
                .anyMatch(cal -> {
                    // Verificar que es del mismo calificador
                    boolean mismoCalificador = cal.getIdUsuarioCalifica().toHexString().equals(idCalificador);
                    
                    // Verificar que la fecha de la calificación coincide con el período del arrendamiento
                    // (la calificación debe estar dentro de los 30 días posteriores a la fecha de salida)
                    boolean dentroDelPeriodo = false;
                    if (cal.getFecha() != null && arrendamiento.getFechaSalida() != null) {
                        long diferencia = cal.getFecha().getTime() - arrendamiento.getFechaSalida().getTime();
                        long treintaDias = 30L * 24L * 60L * 60L * 1000L;
                        dentroDelPeriodo = diferencia >= 0 && diferencia <= treintaDias;
                    }
                    
                    return mismoCalificador && dentroDelPeriodo;
                });
                
        log.debug("Verificación de calificación previa - Calificador: {}, Arrendamiento: {}, Ya calificó: {}", 
                idCalificador, idArrendamiento, yaCalifico);
        
        return yaCalifico;
    }
    
    // Métodos auxiliares privados
    
    private void validarParametrosCalificacion(String idCalificador, String idCalificado, int puntuacion) {
        if (idCalificador == null || idCalificador.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del calificador no puede estar vacío");
        }
        
        if (idCalificado == null || idCalificado.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario a calificar no puede estar vacío");
        }
        
        if (idCalificador.equals(idCalificado)) {
            throw new IllegalArgumentException("Un usuario no puede calificarse a sí mismo");
        }
        
        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }
    }
    
    private void validarComentario(String comentario) {
        if (comentario != null && comentario.length() > 300) {
            throw new IllegalArgumentException("El comentario no puede exceder los 300 caracteres");
        }
    }
    
    private Usuario obtenerUsuario(String idUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado: " + idUsuario);
        }
        return usuarioOpt.get();
    }
    
    private void enviarNotificacionCalificacion(String idUsuario, int puntuacion, String comentario, String tipoUsuario) {
        try {
            String mensajeNotificacion = "Has recibido una nueva calificación de " + puntuacion + " estrellas";
            
            if ("arrendatario".equals(tipoUsuario)) {
                mensajeNotificacion += " de tu propietario.";
            } else {
                mensajeNotificacion += " de un arrendatario.";
            }
            
            if (comentario != null && !comentario.trim().isEmpty()) {
                mensajeNotificacion += " Comentario: \"" + comentario + "\"";
            }
            
            // Obtener un aviso real del usuario para las notificaciones
            String avisoId = obtenerAvisoDelUsuario(idUsuario);
            mensajeService.enviarMensaje(idUsuario, mensajeNotificacion, avisoId);
            
        } catch (Exception e) {
            log.error("Error enviando notificación de calificación a usuario {}: {}", idUsuario, e.getMessage());
        }
    }
    
    /**
     * Obtiene un aviso real asociado al usuario para las notificaciones
     */
    private String obtenerAvisoDelUsuario(String idUsuario) {
        try {
            // Buscar espacios del usuario
            List<Espacio> espaciosUsuario = espacioRepository.findByIdPropietario(new ObjectId(idUsuario));
            
            if (!espaciosUsuario.isEmpty()) {
                // Buscar avisos del primer espacio del usuario
                List<Aviso> avisos = avisoRepository.findByIdEspacio(espaciosUsuario.get(0).getId());
                
                if (!avisos.isEmpty()) {
                    return avisos.get(0).getId().toHexString();
                }
            }
            
            // Si no encuentra avisos específicos, usar el ID del usuario como fallback
            log.debug("No se encontraron avisos para usuario {}, usando ID de usuario como fallback", idUsuario);
            return idUsuario;
            
        } catch (Exception e) {
            log.error("Error al obtener aviso del usuario {}: {}", idUsuario, e.getMessage());
            return idUsuario; // Fallback seguro
        }
    }
    
    /**
     * MÉTODO AUXILIAR: Busca un arrendamiento completado entre dos usuarios
     * Esto resuelve la VALIDACIÓN DE FINALIZACIÓN y RESTRICCIÓN TEMPORAL
     */
    private Arrendamiento buscarArrendamientoPorUsuarios(String idUsuario1, String idUsuario2) {
        try {
            List<Espacio> espacios = espacioRepository.findAll();
            
            for (Espacio espacio : espacios) {
                if (espacio.getArrendamiento() != null) {
                    for (Arrendamiento arrendamiento : espacio.getArrendamiento()) {
                        String idPropietario = espacio.getIdPropietario().toHexString();
                        String idArrendatario = arrendamiento.getIdUsuario().toHexString();
                        
                        // Verificar si los usuarios coinciden con la relación propietario-arrendatario
                        boolean relacionValida = 
                            (idUsuario1.equals(idPropietario) && idUsuario2.equals(idArrendatario)) ||
                            (idUsuario1.equals(idArrendatario) && idUsuario2.equals(idPropietario));
                            
                        if (relacionValida) {
                            return arrendamiento;
                        }
                    }
                }
            }
            
            return null;
            
        } catch (Exception e) {
            log.error("Error al buscar arrendamiento entre usuarios {} y {}: {}", 
                     idUsuario1, idUsuario2, e.getMessage());
            return null;
        }
    }
}