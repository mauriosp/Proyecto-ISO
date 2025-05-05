package com.apirest.backend.Model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor

public class VerificacionEmail {
    private String token;
    private Date fechaCreacion;
    private Date fechaExpiracion;
    private boolean verificado;
    private String tipoVerificacion; // "Registro" o "Recuperacion"
}
