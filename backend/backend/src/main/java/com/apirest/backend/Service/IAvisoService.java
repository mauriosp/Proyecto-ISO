package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IAvisoService {
    // Especificar las operaciones (CRUD)
    public List<Aviso> listarAvisos();

    void crearAviso(String descripcion, double precioMensual, List<MultipartFile> imagenes, String titulo, String tipoEspacio, String caracteristicas, String direccion, BigDecimal area, String idUsuario) throws Exception;

    void editarAviso(String id, String titulo, String descripcion, Double precioMensual, List<MultipartFile> imagenes, String estado) throws Exception;

    void eliminarAviso(String id) throws Exception;

    List<Aviso> listarAvisosParaModeracion();

    void desactivarAviso(String id, String motivo) throws Exception;

    void reactivarAviso(String id) throws Exception;

    void eliminarAvisosPorPropietario(String idPropietario) throws Exception;

    List<Aviso> filtrarAvisos(String tipoEspacio, Double precioMin, Double precioMax, String disponibilidad);
}
