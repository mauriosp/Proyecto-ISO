package com.apirest.backend.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    private static final int MAX_INTENTOS = 5;
    private static final int TIEMPO_BLOQUEO = 15; // minutos

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByVerificacionEmail_Token(email);

            if (usuarioOpt.isEmpty()) {
                logger.warn("Intento de login con email no registrado: {}", email);
                return "Usuario no encontrado";
            }

            Usuario usuario = usuarioOpt.get();

            // Verificación de email
            List<VerificacionEmail> verificaciones = usuario.getVerificacionEmail();
            if (verificaciones == null || verificaciones.isEmpty() || 
                !verificaciones.get(verificaciones.size() - 1).getVerificado()) {
                logger.warn("Intento de login con correo no verificado: {}", email);
                return "Correo no verificado. Por favor verifica tu cuenta.";
            }

            // Verificación de bloqueo de cuenta
            if (usuario.isEstado()) {
                if (usuario.getFechaBloqueo() != null) {
                    long minutos = (new Date().getTime() - usuario.getFechaBloqueo().getTime()) / (60 * 1000);
                    if (minutos < TIEMPO_BLOQUEO) {
                        logger.warn("Intento de login en cuenta bloqueada: {}", email);
                        return "Cuenta bloqueada. Intenta en " + (TIEMPO_BLOQUEO - minutos) + " minutos.";
                    } else {
                        usuario.setEstado(false);
                        usuario.setIntentosFallidos(0);
                        usuario.setFechaBloqueo(null);
                    }
                }
            }

            // Validación de contraseña
            if (!passwordEncoder.matches(contraseña, usuario.getContraseña())) {
                int intentos = usuario.getIntentosFallidos() + 1;
                usuario.setIntentosFallidos(intentos);

                if (intentos >= MAX_INTENTOS) {
                    usuario.setEstado(true);
                    usuario.setFechaBloqueo(new Date());
                    usuarioRepository.save(usuario);
                    
                    logger.error("Cuenta bloqueada por múltiples intentos fallidos: {}", email);
                    return "Cuenta bloqueada por múltiples intentos fallidos.";
                }

                usuarioRepository.save(usuario);
                logger.warn("Contraseña incorrecta para usuario: {}. Intento {} de {}", email, intentos, MAX_INTENTOS);
                return "Contraseña incorrecta. Intento " + intentos + " de " + MAX_INTENTOS + ".";
            }

            // Login exitoso
            usuario.setIntentosFallidos(0);
            usuarioRepository.save(usuario);
            
            logger.info("Login exitoso para usuario: {}", email);
            return "Login exitoso. Bienvenido, " + usuario.getNombre();

        } catch (Exception e) {
            logger.error("Error en el proceso de login para email: {}", email, e);
            return "Error en el proceso de login";
        }
    }
}