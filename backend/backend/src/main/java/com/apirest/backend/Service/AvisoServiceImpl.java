package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Repository.AvisoRepository;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AvisoServiceImpl implements IAvisoService {

    @Autowired
    private AvisoRepository avisoRepository;

    @Override
    public String guardarAviso(Aviso aviso) {
        // Convertir BigDecimal a Decimal128 si no es nulo
        if (aviso.getPrecio() != null) {
            aviso.setPrecio(new Decimal128(aviso.getPrecio().bigDecimalValue()));
        }

        avisoRepository.save(aviso);

        // Validación para evitar NullPointerException
        String titulo = (aviso.getTitulo() != null) ? aviso.getTitulo() : "sin título";

        return "El aviso '" + titulo + "' fue creado con éxito";
    }

    @Override
    public List<Aviso> listarAvisos() {
        return avisoRepository.findAll();
    }
}
