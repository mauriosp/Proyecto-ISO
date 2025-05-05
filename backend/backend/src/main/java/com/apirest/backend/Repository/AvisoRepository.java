package com.apirest.backend.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apirest.backend.Model.Aviso;

@Repository
public interface AvisoRepository extends MongoRepository<Aviso, String> {
   
    List<Aviso> findByIdEspacio(ObjectId idEspacio);
    void deleteByIdEspacio(ObjectId idEspacio);
}