package com.apirest.backend.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;


import java.util.Date;
import java.util.List;


@Data
@Document("Aviso")
@AllArgsConstructor
@NoArgsConstructor



public class Aviso {
    @Id
    private ObjectId id;
    private ObjectId idEspacio;
    private String titulo;
    private String descripcion;
    private Integer precio;
    private String estado;
    private Date fechaPublicacion;
    private String imagenes;
    private List <Mensaje> mensaje;
    private List<String> extraInfo;


    // Serializa el campo "id" como String
    @JsonProperty("id")
    public String getIdAsString() {
        return id != null ? id.toHexString() : null;
    }

    @JsonProperty("idEspacio")
    public String getIdEspacioAsString() {
        return idEspacio != null ? idEspacio.toHexString() : null;
    }
}