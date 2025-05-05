package com.apirest.backend.Repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apirest.backend.Model.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByVerificacionEmail_Token(String token);

    Optional<Usuario> findByEmail(String email);

    boolean existsById(ObjectId id);
}