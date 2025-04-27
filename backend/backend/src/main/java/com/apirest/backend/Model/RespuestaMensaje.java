package com.apirest.backend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaMensaje {
    private String contenido;
    private Date fechaRespuesta;
}