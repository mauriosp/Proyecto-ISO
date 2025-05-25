package com.apirest.backend.Dto;

import lombok.Data;

@Data
public class CancelarArrendamientoDTO {
    private String idEspacio;
    private String idArrendatario;
    private String motivoCancelacion;
}