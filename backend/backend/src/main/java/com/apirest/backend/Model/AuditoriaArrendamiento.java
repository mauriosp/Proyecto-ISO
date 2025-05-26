package com.apirest.backend.Model;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class AuditoriaArrendamiento {
    private Date fechaModificacion;
    private String tipoOperacion; // "CREACION", "MODIFICACION", "CANCELACION"
    private String campoModificado; // "fechaInicio", "fechaSalida", "terminos", "montoMensual", "estado"
    private String valorAnterior;
    private String valorNuevo;
    private String motivoModificacion;
    private ObjectId usuarioModificador; // ID del usuario que hizo la modificación
}