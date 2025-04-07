package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Model.Reporte;
import com.apirest.backend.Repository.AvisoRepository;
import com.apirest.backend.Repository.ReporteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReporteServiceImpl implements IReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private AvisoRepository avisoRepository;

    @Autowired
    private INotificacionService notificacionService;

    @Override
    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }

    @Override
    public void reportarAviso(String idAviso, String idUsuario, String motivo, String comentarios) {
        // Crear un nuevo reporte
        Reporte reporte = new Reporte();
        reporte.setIdAviso(new ObjectId(idAviso));
        reporte.setIdUsuario(new ObjectId(idUsuario));
        reporte.setMotivo(motivo);
        reporte.setComentarios(comentarios);
        reporte.setFechaReporte(new Date());

        // Guardar el reporte en la base de datos
        reporteRepository.save(reporte);

        // Notificar al administrador
        notificacionService.enviarNotificacionAdministrador(
                "Nuevo reporte generado",
                "Se ha generado un nuevo reporte para el aviso con ID: " + idAviso + ". Motivo: " + motivo
        );
    }

    @Override
    public void resolverReporte(String idReporte, String decision, String motivo) throws Exception {
        // Buscar el reporte
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new IllegalArgumentException("Reporte no encontrado"));

        // Actualizar el estado y la decisión del reporte
        reporte.setEstado("Resuelto");
        reporte.setDecision(decision);
        reporteRepository.save(reporte);

        // Si la decisión es excluir la publicación
        if ("Excluir".equalsIgnoreCase(decision)) {
            Aviso aviso = avisoRepository.findById(reporte.getIdAviso().toHexString())
                    .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));
            aviso.setEstado("Inactivo");
            aviso.setMotivoDesactivacion(motivo);
            avisoRepository.save(aviso);

            // Notificar al propietario del aviso
            notificacionService.enviarNotificacion(
                    aviso.getIdPropietario(),
                    "Tu publicación con título '" + aviso.getTitulo() + "' ha sido eliminada. Motivo: " + motivo,
                    aviso.getId()
            );
        }

        // Notificar al usuario que reportó
        notificacionService.enviarNotificacion(
                reporte.getIdUsuario(),
                "Resolución de tu reporte",
                new ObjectId(avisoRepository.findById(reporte.getIdAviso().toHexString()).get().getTitulo())
        );
    }
}
