package com.apirest.backend.Model;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Arrendamiento {
    private ObjectId idUsuario; 
    private Date fechaSalida;
    private Date fechaInicio;
    private String estado;
    private String terminos;
    private CalificacionEspacio calificacionEspacio;
}