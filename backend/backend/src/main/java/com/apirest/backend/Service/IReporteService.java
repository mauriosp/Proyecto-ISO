package com.apirest.backend.Service;

import java.util.List;

import com.apirest.backend.Model.Reporte;

public interface IReporteService {
    Reporte crearReporte(Reporte reporte);
    List<Reporte> obtenerTodosReportes();
    Reporte obtenerReportePorId(String id);
    Reporte actualizarEstadoReporte(String id, String nuevoEstado);
    void eliminarReporte(String id);
    List<Reporte> obtenerReportesPorAviso(String idAviso);
    List<Reporte> obtenerReportesPorUsuario(String idUsuario);
    void eliminarReportesPorUsuario(String idUsuario);
    void eliminarReportesPorAviso(String idAviso);
}