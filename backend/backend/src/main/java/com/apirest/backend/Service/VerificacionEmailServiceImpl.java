package com.apirest.backend.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificacionEmailServiceImpl implements IVerificacionEmailService {

    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;

    @Override
    public void enviarCorreoVerificacion(String userId, String email) {
        // Validar formato de email
        if (!validarFormatoEmail(email)) {
            log.error("Formato de email inválido: {}", email);
            throw new IllegalArgumentException("El formato del correo electrónico no es válido");
        }
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        if (usuarioOpt.isEmpty()) {
            log.error("Usuario no encontrado con ID: {}", userId);
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        String token = UUID.randomUUID().toString();
        Date now = new Date();
        Date expiracion = new Date(now.getTime() + 24 * 60 * 60 * 1000); // +24h

        VerificacionEmail verificacion = new VerificacionEmail();
        verificacion.setToken(token);
        verificacion.setFechaCreacion(now);
        verificacion.setFechaExpiracion(expiracion);
        verificacion.setVerificado(false);
        verificacion.setTipoVerificacion("Registro");

        List<VerificacionEmail> verificaciones = new ArrayList<>();
        verificaciones.add(verificacion);
        usuario.setVerificacionEmail(verificaciones); // reemplaza la lista

        try {
            usuarioRepository.save(usuario);
            log.info("Token de verificación generado para usuario: {}, email: {}", userId, email);
        } catch (Exception e) {
            log.error("Error al guardar token de verificación: {}", e.getMessage());
            throw new RuntimeException("Error al generar el token de verificación", e);
        }

        String link = "http://localhost:8080/UAO/apirest/VerificacionEmail/verificar?token=" + token;

        try {
            // Usamos el método más simple que envía correo en texto plano
            enviarCorreoTextoPlano(email, usuario.getNombre(), link);
            log.info("Correo enviado a {}", email);
        } catch (MailException e) {
            log.error("Error al enviar el correo de verificación: {}", e.getMessage());
            throw new RuntimeException("Error al enviar el correo. Verifica la configuración del servidor SMTP", e);
        }
    }

    @Override
    public boolean verificarToken(String token) {
        if (token == null || token.isEmpty()) {
            log.warn("Intento de verificación con token vacío");
            return false;
        }
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByVerificacionEmail_Token(token);
        if (usuarioOpt.isEmpty()) {
            log.warn("Token no encontrado en la base de datos: {}", token);
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        List<VerificacionEmail> lista = usuario.getVerificacionEmail();

        if (lista == null || lista.isEmpty()) {
            log.warn("Usuario sin verificaciones: {}", usuario.getId());
            return false;
        }

        // Buscar el token específico en lugar de solo usar el último token
        VerificacionEmail ve = null;
        for (VerificacionEmail v : lista) {
            if (token.equals(v.getToken())) {
                ve = v;
                break;
            }
        }
        
        if (ve == null) {
            log.warn("Token no encontrado en las verificaciones del usuario: {}", usuario.getId());
            return false;
        }

        if (ve.isVerificado()) {
            log.warn("Token ya utilizado: {}", token);
            return false;
        }
        
        if (ve.getFechaExpiracion().before(new Date())) {
            log.warn("Token expirado: {}", token);
            return false;
        }

        // Verificamos el token y activamos la cuenta
        ve.setVerificado(true);
        usuario.setEstado(true); // Activar la cuenta del usuario
        
        try {
            usuarioRepository.save(usuario);
            log.info("Cuenta verificada exitosamente para el usuario: {}", usuario.getId());
            return true;
        } catch (Exception e) {
            log.error("Error al guardar verificación: {}", e.getMessage());
            return false;
        }
    }
    
    // Método auxiliar para validar el formato del correo
    private boolean validarFormatoEmail(String email) {
        // Patrón RFC 5322 para validación de email
        String patronEmail = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return email != null && email.matches(patronEmail);
    }
    
    // Método simplificado para enviar correo en formato texto plano
    private void enviarCorreoTextoPlano(String destinatario, String nombreUsuario, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Verifica tu cuenta - Reserva de Espacios UAO");
        message.setText(
            "¡Hola " + (nombreUsuario != null ? nombreUsuario : "Usuario") + "!\n\n" +
            "Gracias por registrarte en nuestra plataforma de Reserva de Espacios UAO.\n\n" +
            "Por favor, haz clic en el siguiente enlace para verificar tu cuenta:\n" +
            link + "\n\n" +
            "Este enlace es válido por 24 horas.\n\n" +
            "Si no solicitaste esta verificación, puedes ignorar este correo.\n\n" +
            "Saludos,\n" +
            "El equipo de Reserva de Espacios UAO"
        );
        mailSender.send(message);
    }
}
