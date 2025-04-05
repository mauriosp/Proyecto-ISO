package com.apirest.backend.Service;

import com.apirest.backend.Model.Reporte;
import com.apirest.backend.Repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteServiceImpl implements IReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Override
    public void guardarReporte(Reporte reporte) {
        reporteRepository.save(reporte);
    }

    @Override
    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }
}
