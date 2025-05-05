package com.apirest.backend.Service;

import org.springframework.stereotype.Service;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Repository.AvisoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModeracionServiceImpl implements IModeracionService {

    private final AvisoRepository avisoRepository;
    private final IMensajeService notificacionService;

    @Override
    public void moderarAviso(String avisoId, String accion, String motivo) {
        
        Aviso aviso = avisoRepository.findById(avisoId)
                .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));
        
        // Aplicar la acción de moderación
        switch (accion) {
            case "suspender" -> aviso.setEstado("Suspendido");
            case "aprobar" -> aviso.setEstado("Aprobado");
            case "rechazar" -> aviso.setEstado("Rechazado");
            default -> throw new IllegalArgumentException("Acción de moderación no válida");
        }
        
        // Guardar el aviso actualizado
        avisoRepository.save(aviso);
        
        // Notificar al propietario sobre la moderación
        
        String espacioId = aviso.getIdEspacio().toString();
        notificacionService.notificarModeracionAviso(espacioId, avisoId, motivo, accion);
        
        log.info("Aviso moderado: id={}, acción={}, motivo={}", avisoId, accion, motivo);
    }
}