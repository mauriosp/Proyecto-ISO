package com.apirest.backend.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.apirest.backend.Model.Aviso;
import org.springframework.stereotype.Repository;

@Repository
public interface AvisoRepository extends MongoRepository<Aviso, String> {
    void deleteByUsuarioId(String usuarioId);
}

