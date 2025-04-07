package com.apirest.backend.Service;

import com.apirest.backend.Model.Reporte;
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
    private INotificacionService notificationService;

    @Override
    public void guardarReporte(Reporte reporte) {
        reporteRepository.save(reporte);
    }

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
        notificationService.enviarNotificacionAdministrador(
                "Nuevo reporte generado",
                "Se ha generado un nuevo reporte para el aviso con ID: " + idAviso + ". Motivo: " + motivo
        );
    }
}
