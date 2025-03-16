package com.apirest.backend.Repository;
import com.apirest.backend.Model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    
} 