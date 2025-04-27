package com.apirest.backend.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.apirest.backend.Model.Aviso;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository 
public interface AvisoRepository extends MongoRepository<Aviso, String> {
    // Método para buscar avisos por ID de propietario
    List<Aviso> findByIdPropietario(ObjectId idPropietario);
    
    // Opcional: Método para eliminar avisos de un propietario
    void deleteByIdPropietario(ObjectId idPropietario);

}