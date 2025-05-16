package com.apirest.backend.Service;

import java.math.BigDecimal;
import java.util.List;

import com.apirest.backend.Model.Aviso;

public interface IAvisoService {
    // Especificar las operaciones (CRUD)
    public List<Aviso> listarAvisos();

    void crearAviso(String descripcion, double precioMensual, List<String> imagenes, String titulo, String tipoEspacio, int habitaciones, int baños, String direccion, BigDecimal area, String idUsuario, List<String> extraInfos) throws Exception;

    void editarAviso(String id, String titulo, String descripcion, Double precioMensual, List<String> imagenes, String estado, String tipoEspacio, String direccion, BigDecimal area, int habitaciones, int baños) throws Exception;

    void eliminarAviso(String id) throws Exception;

    List<Aviso> listarAvisosParaModeracion();

    void desactivarAviso(String id, String motivo) throws Exception;

    void reactivarAviso(String id) throws Exception;

    void eliminarAvisosPorPropietario(String idPropietario) throws Exception;

    List<Aviso> filtrarAvisos(String tipoEspacio, Double precioMin, Double precioMax, String disponibilidad);
}
