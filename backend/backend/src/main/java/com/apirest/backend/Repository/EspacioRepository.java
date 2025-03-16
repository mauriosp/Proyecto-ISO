package com.apirest.backend.Repository;

import com.apirest.backend.Model.Espacio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspacioRepository extends MongoRepository<Espacio, String> {
}
