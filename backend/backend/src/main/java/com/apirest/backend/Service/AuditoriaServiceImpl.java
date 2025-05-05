package com.apirest.backend.Service;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.apirest.backend.Model.AuditoriaPerfil;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditoriaServiceImpl implements IAuditoriaService {

    private final UsuarioRepository usuarioRepository;
    
    @Override
    public void registrarCambio(Usuario usuario, String campo, String valorAnterior, 
                               String valorNuevo) {
        AuditoriaPerfil auditoria = new AuditoriaPerfil();
        auditoria.setFechaModificacion(new Date());
        auditoria.setCampoModificado(campo);
        auditoria.setValorAnterior(valorAnterior);
        auditoria.setValorNuevo(valorNuevo);
        
        if (usuario.getAuditoriaPerfil() == null) {
            usuario.setAuditoriaPerfil(new ArrayList<>());
        }
        
        usuario.getAuditoriaPerfil().add(auditoria);
        usuarioRepository.save(usuario);
        
    }
}