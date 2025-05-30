package com.apirest.backend.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Date;


@Document("Usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class Usuario {
    @Id
    private ObjectId id;
    private String nombre;
    private String email;
    private long telefono;
    private String contraseña;
    private String tipoUsuario;
    private int promCalificacion;
    private int intentosFallidos;
    private boolean estado = false;
    private Date fechaBloqueo;   
    private String fotoPerfil;

    private List <CalificacionUsuario> CalificacionUsuario;
    @JsonProperty("id")
    public String getIdAsString(){
        return id!=null ? id.toHexString():null;
    }
    private List <VerificacionEmail> verificacionEmail;
    private List <AuditoriaPerfil> auditoriaPerfil;
}
