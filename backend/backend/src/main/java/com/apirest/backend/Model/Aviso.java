package com.apirest.backend.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private ObjectId idPropietario;
    private String titulo;
    private String descripcion;
    private Integer precio;
    private String estado;
    private Date fechaPublicacion;
    private String imagenes;
    private String motivoDesactivacion;
    private List <Mensaje> mensaje;
}