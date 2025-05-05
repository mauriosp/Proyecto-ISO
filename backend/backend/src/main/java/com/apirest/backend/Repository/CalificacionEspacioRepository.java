package com.apirest.backend.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apirest.backend.Model.CalificacionEspacio;

@Repository
public interface CalificacionEspacioRepository extends MongoRepository<CalificacionEspacio, String> {
    // Puedes añadir métodos personalizados si es necesario
}