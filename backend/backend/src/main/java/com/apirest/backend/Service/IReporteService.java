package com.apirest.backend.Service;

import com.apirest.backend.Model.Reporte;
import org.bson.types.ObjectId;

import java.util.List;

public interface IReporteService {
    Reporte crearReporte(Reporte reporte);
    List<Reporte> obtenerTodosReportes();
    Reporte obtenerReportePorId(ObjectId id);
    Reporte actualizarEstadoReporte(ObjectId id, String nuevoEstado);
    void eliminarReporte(ObjectId id);
    List<Reporte> obtenerReportesPorAviso(ObjectId idAviso);
    List<Reporte> obtenerReportesPorUsuario(ObjectId idUsuario);
    void eliminarReportesPorUsuario(ObjectId idUsuario);
    void eliminarReportesPorAviso(ObjectId idAviso);
}