package com.apirest.backend.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import java.math.BigDecimal;
import java.util.List;

@Data
@Document ("Espacio")
@AllArgsConstructor
@NoArgsConstructor

public class Espacio {
    @Id
    private ObjectId id;
    private ObjectId idUsuario;
    private String tipo;
    private String direccion;
    private BigDecimal area;
    private String caracteristicas;
    private String tipoEspacio;
    private String estado;
    private List <Arrendamiento> arrendamiento;
    
    }
