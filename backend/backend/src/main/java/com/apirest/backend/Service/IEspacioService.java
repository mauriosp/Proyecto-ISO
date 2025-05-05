package com.apirest.backend.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.apirest.backend.Model.Espacio;

public interface IEspacioService {
    List<Espacio> listarEspacios();
    Optional<Espacio> buscarEspacioPorId(String id);
    Espacio guardarEspacio(Espacio espacio);
    void eliminarEspacio(String id);
    Espacio crearEspacio(ObjectId idUsuario, String tipoEspacio, String caracteristicas, String direccion, BigDecimal area);
    Optional<Espacio> buscarEspacioPorDireccionYPropietario(String direccion, ObjectId idPropietario);
    Espacio editarEspacio(String idEspacio, String tipoEspacio, String caracteristicas, String direccion, BigDecimal area);

}
