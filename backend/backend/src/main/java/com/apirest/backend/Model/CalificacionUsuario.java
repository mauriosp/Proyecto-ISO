package com.apirest.backend.Model;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CalificacionUsuario {
    private ObjectId idUsuarioCalifica;
    private String puntuacion;
    private Date fecha;
    private String comentario;


 public int getPuntuacionAsInt() {
        try {
            return Integer.parseInt(puntuacion);
        } catch (NumberFormatException e) {
            return 0; // Valor por defecto en caso de error
        }
    }

public void setPuntuacionFromInt(String puntuacionInt) {
    this.puntuacion = String.valueOf(puntuacionInt);
    }
}