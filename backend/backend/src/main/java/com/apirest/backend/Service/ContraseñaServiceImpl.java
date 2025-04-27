package com.apirest.backend.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContraseñaServiceImpl implements IContraseñaService {

    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void enviarCorreoResetContraseña(String email) {
        // Buscar usuario por email, no por token
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            log.warn("Solicitud de reset de contraseña para email no registrado: {}", email);
            return;
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
        // Si tu VerificacionEmail tiene el campo tipoVerificacion, descomenta esta línea
        verificacion.setTipoVerificacion("RESET_PASSWORD");
        
        // Si VerificacionEmail es una lista en Usuario
        List<VerificacionEmail> verificaciones = new ArrayList<>();
        verificaciones.add(verificacion);
        usuario.setVerificacionEmail(verificaciones);
        
        usuarioRepository.save(usuario);

        String link = "http://localhost:8080/UAO/apirest/password/formulario?token=" + token;

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Recuperación de Contraseña");
            message.setText("Has solicitado restablecer tu contraseña. Haz clic en el siguiente enlace para crear una nueva contraseña: " + link + 
                        "\n\nEste enlace expirará en 1 hora. Si no solicitaste este cambio, puedes ignorar este correo.");
            mailSender.send(message);
            log.info("Correo de recuperación de contraseña enviado a {}", email);
        } catch (MailSendException e) {
            log.error("Error al enviar correo de recuperación - Problema con el envío: ", e);
        } catch (MailPreparationException e) {
            log.error("Error al preparar el correo de recuperación: ", e);
        } catch (MailException e) {
            log.error("Error general de correo al enviar recuperación de contraseña: ", e);
        }
    }

    @Override
    public boolean actualizarContraseña(String token, String nuevaContraseña) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByVerificacionEmail_Token(token);
        if (usuarioOpt.isEmpty()) {
            log.warn("Intento de actualización de contraseña con token inválido: {}", token);
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        // Si VerificacionEmail es una lista en Usuario
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

        // Si tu VerificacionEmail tiene el campo tipoVerificacion, descomenta estas líneas
        // if (!"RESET_PASSWORD".equals(ve.getTipoVerificacion())) {
        //    log.warn("Token incorrecto para: {}", usuario.getEmail());
        //    return false;
        // }

        if (ve.getFechaExpiracion().before(new Date()) || ve.getVerificado()) {
            log.warn("Intento de actualización de contraseña con token expirado o ya utilizado para: {}", usuario.getEmail());
            return false;
        }

        // Encriptar la nueva contraseña
        String hashedContraseña = passwordEncoder.encode(nuevaContraseña);
        // Usar el método correcto para establecer la contraseña
        usuario.setContraseña(hashedContraseña);
        
        // Marcar el token como utilizado
        ve.setVerificado(true);
        
        usuarioRepository.save(usuario);
        log.info("Contraseña actualizada correctamente para: {}", usuario.getEmail());
        
        return true;
    }
}