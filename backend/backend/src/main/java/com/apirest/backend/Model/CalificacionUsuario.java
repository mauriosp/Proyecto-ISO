package com.apirest.backend.Model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CalificacionUsuario {
    private String idUsuarioCalifiaca;
    private String puntuacion;
    private Date fecha;
    private String comentario;
}
