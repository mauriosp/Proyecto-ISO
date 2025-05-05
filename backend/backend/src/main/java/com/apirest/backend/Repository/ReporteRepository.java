package com.apirest.backend.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apirest.backend.Model.Reporte;

@Repository
public interface ReporteRepository extends MongoRepository<Reporte, String> {
    List<Reporte> findByIdAviso(String idAviso);
    List<Reporte> findByIdUsuario(String idUsuario);
    void deleteByIdUsuario(String idUsuario);
    void deleteByIdAviso(String idAviso);
}