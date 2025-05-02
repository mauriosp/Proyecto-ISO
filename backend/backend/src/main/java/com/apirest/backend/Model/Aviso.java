package com.apirest.backend.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.annotation.JsonProperty; // Importar esta anotación
import java.util.Date;
import java.util.List;

@Data
@Document("Aviso")
@AllArgsConstructor
@NoArgsConstructor
public class Aviso {
    @Id
    private ObjectId id;
    
    private ObjectId idPropietario;
    private String titulo;
    private String descripcion;
    private Integer precio;
    private String estado;
    private Date fechaPublicacion;
    private String imagenes;
    private String motivoDesactivacion;
    private List<Mensaje> mensaje;
    private ObjectId idEspacio;

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