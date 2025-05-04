package com.apirest.backend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor


public class Mensaje {
    private ObjectId idUsuario;
    private String contenido;
    private Date fechaMensaje;
    private boolean estadoMensaje;
    private RespuestaMensaje respuestaMensaje;

    @JsonProperty("idUsuario")
    public String getIdUsuarioAsString() {
        return idUsuario != null ? idUsuario.toHexString() : null;
    }
}
