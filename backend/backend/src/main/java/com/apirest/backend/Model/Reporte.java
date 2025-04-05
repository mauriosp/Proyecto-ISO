package com.apirest.backend.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {

    @Id
    private String id;

    private String descripcion;
    private String motivo;

    @DBRef
    private Aviso aviso; // Referencia al aviso reportado
}
