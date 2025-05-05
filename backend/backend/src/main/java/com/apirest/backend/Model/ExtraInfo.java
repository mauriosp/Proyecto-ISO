package com.apirest.backend.Model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExtraInfo {
    private Map<String, Object> detalles;  // Permite almacenar datos dinámicos
}
