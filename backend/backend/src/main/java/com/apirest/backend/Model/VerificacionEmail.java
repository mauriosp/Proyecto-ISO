package com.apirest.backend.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor

public class VerificacionEmail {
    private String Token;
    private Date fechaCreacion;
    private Date fechaExpiracion;
    private Boolean verificado;
    private String tipoVerificacion; // "Registro" o "Recuperacion"
}
