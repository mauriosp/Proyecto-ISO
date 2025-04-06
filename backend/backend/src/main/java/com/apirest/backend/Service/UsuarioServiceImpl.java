package com.apirest.backend.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public String guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
        return "El Usuario " + usuario.getNombre() + ", fue creado con éxito";
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public String loginUsuario(String email, String contraseña) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();

        if (usuarioOpt.isEmpty()) return "Usuario no encontrado";

        Usuario usuario = usuarioOpt.get();

        List<VerificacionEmail> verificaciones = usuario.getVerificacionEmail();
        if (verificaciones == null || verificaciones.isEmpty() || 
            !verificaciones.get(verificaciones.size() - 1).getVerificado()) {
            return "Correo no verificado. Por favor verifica tu cuenta.";
        }

        if (usuario.isEstado()) {
            if (usuario.getFechaBloqueo() != null) {
                long minutos = (new Date().getTime() - usuario.getFechaBloqueo().getTime()) / (60 * 1000);
                if (minutos < 15) {
                    return "Cuenta bloqueada. Intenta en " + (15 - minutos) + " minutos.";
                } else {
                    usuario.setEstado(false);
                    usuario.setIntentosFallidos(0);
                    usuario.setFechaBloqueo(null);
                }
            }
        }

        if (!usuario.getContraseña().equals(contraseña)) {
            int intentos = usuario.getIntentosFallidos() + 1;
            usuario.setIntentosFallidos(intentos);
            if (intentos >= 5) {
                usuario.setEstado(true);
                usuario.setFechaBloqueo(new Date());
                usuarioRepository.save(usuario);
                return "Cuenta bloqueada por múltiples intentos fallidos.";
            }
            usuarioRepository.save(usuario);
            return "Contraseña incorrecta. Intento " + intentos + " de 5.";
        }

        usuario.setIntentosFallidos(0);
        usuarioRepository.save(usuario);
        return "Login exitoso. Bienvenido, " + usuario.getNombre();
    }
}