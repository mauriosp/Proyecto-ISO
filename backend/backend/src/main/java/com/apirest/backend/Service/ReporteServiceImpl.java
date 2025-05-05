package com.apirest.backend.Service;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.backend.Model.Reporte;
import com.apirest.backend.Repository.ReporteRepository;

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
    public Reporte obtenerReportePorId(String id) {
        return reporteRepository.findById(id).orElse(null);
    }

    @Override
    public Reporte actualizarEstadoReporte(String id, String nuevoEstado) {
        Reporte reporte = reporteRepository.findById(id).orElse(null);
        if (reporte != null) {
            reporte.setEstado(nuevoEstado);
            return reporteRepository.save(reporte);
        }
        return null;
    }

    @Override
    public void eliminarReporte(String id) {
        reporteRepository.deleteById(id);
    }

    @Override
    public List<Reporte> obtenerReportesPorAviso(String idAviso) {
        return reporteRepository.findByIdAviso(idAviso);
    }

    @Override
    public List<Reporte> obtenerReportesPorUsuario(String idUsuario) {
        return reporteRepository.findByIdUsuario(idUsuario);
    }

    @Override
    public void eliminarReportesPorUsuario(String idUsuario) {
        reporteRepository.deleteByIdUsuario(idUsuario);
    }

    @Override
    public void eliminarReportesPorAviso(String idAviso) {
        reporteRepository.deleteByIdAviso(idAviso);
    }
}