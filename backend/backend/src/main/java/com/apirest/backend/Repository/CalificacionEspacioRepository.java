package com.apirest.backend.Repository;

import com.apirest.backend.Model.CalificacionEspacio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalificacionEspacioRepository extends MongoRepository<CalificacionEspacio, String> {
    // Puedes añadir métodos personalizados si es necesario
}