package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface IAvisoService {
    // Especificar las operaciones (CRUD)
    public String guardarAviso(Aviso aviso);
    public List <Aviso> listarAvisos();
    void crearAviso(String descripcion, double precioMensual, List<MultipartFile> imagenes, String titulo) throws Exception;
    
}
