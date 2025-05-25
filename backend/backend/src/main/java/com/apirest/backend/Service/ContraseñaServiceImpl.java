package com.apirest.backend.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContraseñaServiceImpl implements IContraseñaService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailBrevoServiceImpl emailBrevoService; // NUEVA DEPENDENCIA

    @Override
    public void enviarCorreoResetContraseña(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            log.warn("Solicitud de reset de contraseña para email no registrado: {}", email);
            return; // No revelar si el email existe o no por seguridad
        }

        Usuario usuario = usuarioOpt.get();
        String token = UUID.randomUUID().toString();
        Date now = new Date();
        Date expiracion = new Date(now.getTime() + 1 * 60 * 60 * 1000); // 1 hora

        VerificacionEmail verificacion = new VerificacionEmail();
        verificacion.setToken(token);
        verificacion.setFechaCreacion(now);
        verificacion.setFechaExpiracion(expiracion);
        verificacion.setVerificado(false);
        verificacion.setTipoVerificacion("RESET_PASSWORD");

        List<VerificacionEmail> verificaciones = new ArrayList<>();
        verificaciones.add(verificacion);
        usuario.setVerificacionEmail(verificaciones);

        usuarioRepository.save(usuario);

        String link = "http://localhost:8080/UAO/apirest/password/formulario?token=" + token;
        String htmlContent = emailBrevoService.crearEmailRecuperacion(usuario.getNombre(), link);

        boolean enviado = emailBrevoService.enviarEmail(
            email,
            "🔑 Recuperar contraseña - Santorini Hills",
            htmlContent
        );

        if (!enviado) {
            log.error("No se pudo enviar el correo de recuperación a {}", email);
            throw new RuntimeException("Error al enviar el correo de recuperación");
        }

        log.info("Correo de recuperación enviado exitosamente a {}", email);
    }

    @Override
    public boolean actualizarContraseña(String token, String nuevaContraseña) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByVerificacionEmail_Token(token);
        if (usuarioOpt.isEmpty()) {
            log.warn("Intento de actualización de contraseña con token inválido: {}", token);
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        List<VerificacionEmail> verificaciones = usuario.getVerificacionEmail();
        VerificacionEmail ve = null;

        for (VerificacionEmail v : verificaciones) {
            if (token.equals(v.getToken())) {
                ve = v;
                break;
            }
        }

        if (ve == null) {
            log.warn("Intento de actualización de contraseña con token inválido para: {}", usuario.getEmail());
            return false;
        }

        if (!"RESET_PASSWORD".equals(ve.getTipoVerificacion())) {
            log.warn("Token incorrecto para recuperación de contraseña: {}", usuario.getEmail());
            return false;
        }

        if (ve.getFechaExpiracion().before(new Date()) || ve.isVerificado()) {
            log.warn("Intento de actualización de contraseña con token expirado o ya utilizado para: {}", usuario.getEmail());
            return false;
        }

        String hashedContraseña = passwordEncoder.encode(nuevaContraseña);
        usuario.setContraseña(hashedContraseña);
        ve.setVerificado(true);

        usuarioRepository.save(usuario);
        log.info(" Contraseña actualizada correctamente para: {}", usuario.getEmail());

        return true;
    }
}