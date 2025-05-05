package com.apirest.backend.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CalificacionEspacio {

    private int puntuacion;
    private Date fecha;
    private String comentario;
}
