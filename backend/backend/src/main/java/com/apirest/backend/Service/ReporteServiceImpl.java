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

    @Override
    public Reporte crearReporte(Reporte reporte) {
        reporte.setFechaReporte(new Date());
        reporte.setEstado("pendiente");
        return reporteRepository.save(reporte);
    }

    @Override
    public List<Reporte> obtenerTodosReportes() {
        return reporteRepository.findAll();
    }

    @Override
    public Reporte obtenerReportePorId(ObjectId id) {
        return reporteRepository.findById(id).orElse(null);
    }

    @Override
    public Reporte actualizarEstadoReporte(ObjectId id, String nuevoEstado) {
        Reporte reporte = reporteRepository.findById(id).orElse(null);
        if (reporte != null) {
            reporte.setEstado(nuevoEstado);
            return reporteRepository.save(reporte);
        }
        return null;
    }

    @Override
    public void eliminarReporte(ObjectId id) {
        reporteRepository.deleteById(id);
    }

    @Override
    public List<Reporte> obtenerReportesPorAviso(ObjectId idAviso) {
        return reporteRepository.findByIdAviso(idAviso);
    }

    @Override
    public List<Reporte> obtenerReportesPorUsuario(ObjectId idUsuario) {
        return reporteRepository.findByIdUsuario(idUsuario);
    }

    @Override
    public void eliminarReportesPorUsuario(ObjectId idUsuario) {
        reporteRepository.deleteByIdUsuario(idUsuario);
    }

    @Override
    public void eliminarReportesPorAviso(ObjectId idAviso) {
        reporteRepository.deleteByIdAviso(idAviso);
    }
}