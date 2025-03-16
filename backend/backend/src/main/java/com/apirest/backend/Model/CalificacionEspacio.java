package com.apirest.backend.Model;

import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CalificacionEspacio {
    private ObjectId idEspacio;
    private String puntacion;
    private Date fecha;
    private String comentario;
}
