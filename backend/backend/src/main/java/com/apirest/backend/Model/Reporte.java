package com.apirest.backend.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {

    @Id
    private String id;
    private ObjectId idAviso;
    private ObjectId idUsuario;
    private String motivo;
    private String comentarios;
    private Date fechaReporte;
    private String estado;
    private String decision;
}
