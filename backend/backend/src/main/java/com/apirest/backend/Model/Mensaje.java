package com.apirest.backend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

import org.bson.types.ObjectId;


@Data
@AllArgsConstructor
@NoArgsConstructor


public class Mensaje {
    private ObjectId idUsuario;
    private String contenido;
    private Date fechaAviso;
    private boolean estadoMensaje;
    private RespuestaMensaje respuestaMensaje;
}
