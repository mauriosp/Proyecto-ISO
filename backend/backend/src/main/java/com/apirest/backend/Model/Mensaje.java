package com.apirest.backend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import lombok.NoArgsConstructor;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor





public class Mensaje {
    private ObjectId idAviso;
    private String contenido;
    private Date fechaAviso;
    private boolean estadoMensaje;

    
}
