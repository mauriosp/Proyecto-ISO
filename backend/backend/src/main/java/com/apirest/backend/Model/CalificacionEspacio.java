package com.apirest.backend.Model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CalificacionEspacio {
    private int puntuacion;
    private Date fecha;
    private String comentario;
}
