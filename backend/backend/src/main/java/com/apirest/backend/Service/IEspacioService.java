package com.apirest.backend.Service;

import com.apirest.backend.Model.Espacio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IEspacioService {
    List<Espacio> listarEspacios();
    Optional<Espacio> buscarEspacioPorId(String id);
    Espacio guardarEspacio(Espacio espacio);
    void eliminarEspacio(String id);
    Espacio crearEspacio(String idUsuario, String tipoEspacio, String caracteristicas, String direccion, BigDecimal area);
    Optional<Espacio> buscarEspacioPorDireccionYPropietario(String direccion, String idPropietario);
    Espacio editarEspacio(String idEspacio, String tipoEspacio, String caracteristicas, String direccion, BigDecimal area);

}
