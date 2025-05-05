package com.apirest.backend.Model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document ("Espacio")
@AllArgsConstructor
@NoArgsConstructor

public class Espacio {
    @Id
    private ObjectId id;
    private ObjectId idPropietario;
    private String direccion;
    private Double area;
    private String caracteristicas;
    private String tipoEspacio;
    private String estado;
    private int promCalificacion;

    private List <Arrendamiento> arrendamiento;

    // Serializa el campo "id" como String
    @JsonProperty("id")
    public String getIdAsString() {
        return id != null ? id.toHexString() : null;
    }

    // Serializa el campo "idPropietario" como String (opcional)
    @JsonProperty("idPropietario")
    public String getIdPropietarioAsString() {
        return idPropietario != null ? idPropietario.toHexString() : null;
    }
}
