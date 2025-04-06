package com.apirest.backend.Dto;

import lombok.Data;

@Data
public class CambioContraseñaDTO {
    private String token;
    private String nuevaContraseña;
    private String confirmarContraseña;
}