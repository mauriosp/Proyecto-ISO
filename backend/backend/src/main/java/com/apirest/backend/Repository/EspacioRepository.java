package com.apirest.backend.Repository;

import com.apirest.backend.Model.Espacio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;
import java.util.Optional;

@Repository
public interface EspacioRepository extends MongoRepository<Espacio, String> {
    boolean existsByDireccion(String direccion);
    Optional<Espacio> findByDireccionAndIdPropietario(String direccion, ObjectId idPropietario);
}
