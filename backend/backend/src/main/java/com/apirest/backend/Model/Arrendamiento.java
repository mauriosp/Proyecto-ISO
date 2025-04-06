package com.apirest.backend.Model;

import org.bson.types.ObjectId;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Arrendamiento {
    private ObjectId id;  
    private ObjectId idUsuario;
    private ObjectId idEspacio; 
    private Date fechaSalida;
    private Date fechaInicio;
    private String estado;
    private String terminos;
    private CalificacionEspacio calificacionEspacio;
}