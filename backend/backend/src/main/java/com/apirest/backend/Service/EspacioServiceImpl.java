package com.apirest.backend.Service;

import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Repository.EspacioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspacioServiceImpl implements IEspacioService {

    @Autowired
    private EspacioRepository espacioRepository;

    @Override
    public List<Espacio> listarEspacios() {
        return espacioRepository.findAll();
    }

    @Override
    public Optional<Espacio> buscarEspacioPorId(String id) {
        return espacioRepository.findById(id);
    }

    @Override
    public Espacio guardarEspacio(Espacio espacio) {
        return espacioRepository.save(espacio);
    }

    @Override
    public void eliminarEspacio(String id) {
        espacioRepository.deleteById(id);
    }
}
