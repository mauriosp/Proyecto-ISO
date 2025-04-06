package com.apirest.backend.Repository;

import com.apirest.backend.Model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByVerificacionEmail_Token(String token);
    
    // Agregar este método para buscar usuarios por email
    Optional<Usuario> findByEmail(String email);
}