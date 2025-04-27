package com.apirest.backend.Repository;

import com.apirest.backend.Model.Reporte;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends MongoRepository<Reporte, String> {
    List<Reporte> findByIdAviso(String idAviso);
    List<Reporte> findByIdUsuario(String idUsuario);
    void deleteByIdUsuario(String idUsuario);
    void deleteByIdAviso(String idAviso);
}