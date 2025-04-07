package com.apirest.backend.Service;

import com.apirest.backend.Model.Reporte;

import java.util.List;

public interface IReporteService {
    List<Reporte> listarReportes();
    void reportarAviso(String idAviso, String idUsuario, String motivo, String comentarios);
    void resolverReporte(String idReporte, String decision, String motivo) throws Exception;
}

