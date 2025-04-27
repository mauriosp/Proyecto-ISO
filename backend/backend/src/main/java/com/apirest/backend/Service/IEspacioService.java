package com.apirest.backend.Service;

import com.apirest.backend.Model.Espacio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;

public interface IEspacioService {
    List<Espacio> listarEspacios();
    Optional<Espacio> buscarEspacioPorId(ObjectId id);
    Espacio guardarEspacio(Espacio espacio);
    void eliminarEspacio(ObjectId id);
    Espacio crearEspacio(ObjectId idUsuario, String tipoEspacio, String caracteristicas, String direccion, BigDecimal area);
    Optional<Espacio> buscarEspacioPorDireccionYPropietario(String direccion, ObjectId idPropietario);
    Espacio editarEspacio(ObjectId idEspacio, String tipoEspacio, String caracteristicas, String direccion, BigDecimal area);
}
