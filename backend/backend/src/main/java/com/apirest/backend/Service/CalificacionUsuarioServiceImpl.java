package com.apirest.backend.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.backend.Model.CalificacionUsuario;
import com.apirest.backend.Model.Usuario;
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
    private IMensajeService mensajeService;

    @Override
    public void calificarArrendatario(String idPropietario, String idArrendatario, 
                                    int puntuacion, String comentario, String idArrendamiento) {
        
        // Validaciones
        validarParametrosCalificacion(idPropietario, idArrendatario, puntuacion);
        validarComentario(comentario);
        
        // Verificar que los usuarios existan
        Usuario propietario = obtenerUsuario(idPropietario);
        Usuario arrendatario = obtenerUsuario(idArrendatario);
        
        // Validar que sea un propietario (opcional - depende de los requerimientos)
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
        calificacion.setPuntuacion(String.valueOf(puntuacion)); // Usar conversión directa
        calificacion.setFecha(new Date());
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
        String mensajeNotificacion = "Has recibido una nueva calificación de " + puntuacion + " estrellas.";
        if (comentario != null && !comentario.trim().isEmpty()) {
            mensajeNotificacion += " Comentario: \"" + comentario + "\"";
        }
        
        // Necesitaríamos el ID del aviso para la notificación - por ahora usamos un ID dummy
        String avisoId = "dummy_aviso_id"; // En una implementación real, obtener del arrendamiento
        mensajeService.enviarMensaje(idArrendatario, mensajeNotificacion, avisoId);
        
        log.info("Calificación de arrendatario registrada: propietario={}, arrendatario={}, puntuación={}", 
                idPropietario, idArrendatario, puntuacion);
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
        calificacion.setPuntuacion(String.valueOf(puntuacion)); // Usar conversión directa
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
        String mensajeNotificacion = "Has recibido una nueva calificación de " + puntuacion + " estrellas de un arrendatario.";
        if (comentario != null && !comentario.trim().isEmpty()) {
            mensajeNotificacion += " Comentario: \"" + comentario + "\"";
        }
        
        // Necesitaríamos el ID del aviso para la notificación - por ahora usamos un ID dummy
        String avisoId = "dummy_aviso_id"; // En una implementación real, obtener del arrendamiento
        mensajeService.enviarMensaje(idPropietario, mensajeNotificacion, avisoId);
        
        log.info("Calificación de propietario registrada: arrendatario={}, propietario={}, puntuación={}", 
                idArrendatario, idPropietario, puntuacion);
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
                    int puntuacion = calificacion.getPuntuacionAsInt(); // Usar el método helper
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
        // TODO: Implementar validación con el sistema de arrendamientos
        // Por ahora retornamos true, pero en una implementación completa debería:
        // 1. Verificar que existe un arrendamiento con ese ID
        // 2. Verificar que el arrendamiento está en estado "Finalizado"
        // 3. Verificar que los usuarios están relacionados con ese arrendamiento
        // 4. Verificar que el calificador es el propietario o arrendatario correspondiente
        
        log.info("Validando si {} puede calificar a {} para arrendamiento {}", 
                idCalificador, idCalificado, idArrendamiento);
        
        // Usamos el parámetro idArrendamiento en el log para evitar el warning
        return true; // Implementación temporal
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
    
    private boolean yaCalificoEnEsteArrendamiento(Usuario usuarioCalificado, String idCalificador, String idArrendamiento) {
        // TODO: Implementar lógica para verificar si ya calificó en este arrendamiento específico
        // Por ahora retornamos false, pero debería verificar si existe una calificación
        // del calificador para este arrendamiento específico (usando idArrendamiento)
        
        if (usuarioCalificado.getCalificacionUsuario() == null) {
            return false;
        }
        
        // Implementación simple: verificar si ya existe una calificación del mismo calificador
        // En una implementación completa, debería incluir el ID del arrendamiento
        boolean yaCalifico = usuarioCalificado.getCalificacionUsuario().stream()
                .anyMatch(cal -> cal.getIdUsuarioCalifica().toHexString().equals(idCalificador));
                
        log.debug("Verificación de calificación previa para arrendamiento {}: {}", idArrendamiento, yaCalifico);
        return yaCalifico;
    }
}