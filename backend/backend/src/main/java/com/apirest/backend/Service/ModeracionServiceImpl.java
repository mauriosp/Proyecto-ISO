package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Repository.AvisoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModeracionServiceImpl implements IModeracionService {

    private final AvisoRepository avisoRepository;
    private final INotificacionService notificacionService;

    @Override
    public void moderarAviso(String avisoId, String accion, String motivo) {
        ObjectId objAvisoId = new ObjectId(avisoId);
        
        Aviso aviso = avisoRepository.findById(avisoId)
                .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));
        
        // Aplicar la acción de moderación
        if ("suspender".equals(accion)) {
            aviso.setEstado("Suspendido");
        } else if ("aprobar".equals(accion)) {
            aviso.setEstado("Aprobado");
        } else if ("rechazar".equals(accion)) {
            aviso.setEstado("Rechazado");
        } else {
            throw new IllegalArgumentException("Acción de moderación no válida");
        }
        
        // Guardar el aviso actualizado
        avisoRepository.save(aviso);
        
        // Notificar al propietario sobre la moderación
        ObjectId propietarioId = aviso.getIdUsuario();
        notificacionService.notificarModeracionAviso(propietarioId, objAvisoId, motivo, accion);
        
        log.info("Aviso moderado: id={}, acción={}, motivo={}", avisoId, accion, motivo);
    }
}