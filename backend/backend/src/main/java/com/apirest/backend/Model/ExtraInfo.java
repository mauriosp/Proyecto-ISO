package com.apirest.backend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExtraInfo {
    private Map<String, Object> detalles;  // Permite almacenar datos dinámicos
}
