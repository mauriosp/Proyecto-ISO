package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;

import java.math.BigDecimal;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

public interface IAvisoService {
    // Especificar las operaciones (CRUD)
    public List<Aviso> listarAvisos();
    void crearAviso(String descripcion, double precioMensual, List<MultipartFile> imagenes, String titulo, String tipoEspacio, String caracteristicas, String direccion, BigDecimal area, ObjectId idUsuario) throws Exception;

    // Método para editar un aviso
    void editarAviso(String id, String titulo, String descripcion, Double precioMensual, List<MultipartFile> imagenes, String estado) throws Exception;
}
