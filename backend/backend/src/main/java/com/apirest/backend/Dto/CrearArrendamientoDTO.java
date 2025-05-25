package com.apirest.backend.Dto;

import java.util.Date;

import lombok.Data;

@Data
public class CrearArrendamientoDTO {
    private String idEspacio;
    private String idArrendatario;
    private Date fechaInicio;
    private Date fechaSalida;
    private String terminos;
    private Double montoMensual;
}