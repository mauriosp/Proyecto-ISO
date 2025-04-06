package com.apirest.backend.Repository;

import com.apirest.backend.Model.Reporte;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReporteRepository extends MongoRepository<Reporte, String> {
}