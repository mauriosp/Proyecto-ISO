package com.apirest.backend.Model;

import lombok.Data;
import java.util.Date;

@Data
public class AuditoriaPerfil {
    private Date fechaModificacion;
    private String campoModificado;
    private String valorAnterior;
    private String valorNuevo;
}