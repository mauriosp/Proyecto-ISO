package com.apirest.backend.Repository;

import com.apirest.backend.Model.Usuario;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByVerificacionEmail_Token(String token);

    Optional<Usuario> findByEmail(String email);

    boolean existsById(String id);
}