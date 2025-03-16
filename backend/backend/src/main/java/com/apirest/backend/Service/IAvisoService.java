package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;
import java.util.List;

public interface IAvisoService {
    // Especificar las operaciones (CRUD)
    public String guardarAviso(Aviso aviso);
    public List <Aviso> listarAvisos();
    
} 
