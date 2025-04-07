package com.apirest.backend.Model;

import lombok.*;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("Reporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    private ObjectId id;
    private String descripcion;
    private ObjectId idAviso;
    private ObjectId idUsuario;
    private String motivoEReporte;
    private String comentarioAdicional;
    private Date fechaReporte;
    private String estado;

}