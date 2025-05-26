package com.apirest.backend.Dto;

import java.util.Date;

import lombok.Data;

@Data
public class ModificarArrendamientoDTO {
    private String idEspacio;
    private String idArrendatario; // Para identificar el arrendamiento específico
    private Date fechaInicio;
    private Date fechaSalida;
    private String terminos;
    private Double montoMensual;
    private String motivoModificacion;
}