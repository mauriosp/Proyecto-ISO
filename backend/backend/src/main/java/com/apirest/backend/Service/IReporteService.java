package com.apirest.backend.Service;

import com.apirest.backend.Model.Reporte;

import java.util.List;

public interface IReporteService {
    void guardarReporte(Reporte reporte);
    List<Reporte> listarReportes();
}
