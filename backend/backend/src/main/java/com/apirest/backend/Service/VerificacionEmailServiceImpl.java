package com.apirest.backend.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
@Slf4j
public class VerificacionEmailServiceImpl implements IVerificacionEmailService {

    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;

    @Override
    public void enviarCorreoVerificacion(String userId, String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        if (usuarioOpt.isEmpty()) return;

        Usuario usuario = usuarioOpt.get();
        String token = UUID.randomUUID().toString();
        Date now = new Date();
        Date expiracion = new Date(now.getTime() + 24 * 60 * 60 * 1000); // +24h

        VerificacionEmail verificacion = new VerificacionEmail();
        verificacion.setToken(token);
        verificacion.setFechaCreacion(now);
        verificacion.setFechaExpiracion(expiracion);
        verificacion.setVerificado(false);

        // Crear una lista y agregar la verificación
        List<VerificacionEmail> verificaciones = new ArrayList<>();
        verificaciones.add(verificacion);
        usuario.setVerificacionEmail(verificaciones);
        
        usuarioRepository.save(usuario);

        String link = "http://localhost:8080/UAO/apirest/verificar?token=" + token;

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Verifica tu cuenta");
            message.setText("Haz clic aquí para verificar tu cuenta: " + link);
            mailSender.send(message);
            log.info("Correo enviado a {}", email);
        } catch (Exception e) {
            log.error("Error al enviar correo: ", e);
        }
    }

    @Override
    public boolean verificarToken(String token) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByVerificacionEmail_Token(token);
        if (usuarioOpt.isEmpty()) return false;

        Usuario usuario = usuarioOpt.get();
        
        // Buscar la verificación por token
        List<VerificacionEmail> verificaciones = usuario.getVerificacionEmail();
        VerificacionEmail ve = null;
        
        for (VerificacionEmail v : verificaciones) {
            if (token.equals(v.getToken())) {
                ve = v;
                break;
            }
        }
        
        if (ve == null) return false;

        if (ve.getFechaExpiracion().after(new Date()) && !ve.getVerificado()) {
            ve.setVerificado(true);
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }
}