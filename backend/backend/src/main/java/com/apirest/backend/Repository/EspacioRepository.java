package com.apirest.backend.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apirest.backend.Model.Espacio;

@Repository
public interface EspacioRepository extends MongoRepository<Espacio, String> {
    boolean existsByDireccion(String direccion);
    Optional<Espacio> findByDireccionAndIdPropietario(String direccion, String idPropietario);
}
