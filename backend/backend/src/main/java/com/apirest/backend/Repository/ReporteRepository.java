package com.apirest.backend.Repository;

import com.apirest.backend.Model.Reporte;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends MongoRepository<Reporte, ObjectId> {
    List<Reporte> findByIdAviso(ObjectId idAviso);
    List<Reporte> findByIdUsuario(ObjectId idUsuario);
    void deleteByIdUsuario(ObjectId idUsuario);
    void deleteByIdAviso(ObjectId idAviso);
}