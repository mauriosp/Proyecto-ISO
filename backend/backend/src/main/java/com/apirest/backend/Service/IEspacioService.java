package com.apirest.backend.Service;

import com.apirest.backend.Model.Espacio;
import java.util.List;
import java.util.Optional;

public interface IEspacioService {
    List<Espacio> listarEspacios();
    Optional<Espacio> buscarEspacioPorId(String id);
    Espacio guardarEspacio(Espacio espacio);
    void eliminarEspacio(String id);
}
