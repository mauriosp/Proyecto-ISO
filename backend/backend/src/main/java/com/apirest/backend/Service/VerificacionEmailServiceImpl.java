package com.apirest.backend.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificacionEmailServiceImpl implements IVerificacionEmailService {

    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;

    @Override
    public void enviarCorreoVerificacion(ObjectId userId, String email) {
        // Buscar el usuario por ID
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        if (usuarioOpt.isEmpty()) {
            log.warn("Usuario con ID {} no encontrado", userId);
            return;
        }

        Usuario usuario = usuarioOpt.get();

        // Generar token y fechas
        String token = UUID.randomUUID().toString();
        Date now = new Date();
        Date expiracion = new Date(now.getTime() + 24 * 60 * 60 * 1000); // +24 horas

        // Crear objeto de verificación
        VerificacionEmail verificacion = new VerificacionEmail();
        verificacion.setToken(token);
        verificacion.setFechaCreacion(now);
        verificacion.setFechaExpiracion(expiracion);
        verificacion.setVerificado(false);

        // Actualizar la lista de verificaciones del usuario
        List<VerificacionEmail> verificaciones = usuario.getVerificacionEmail();
        if (verificaciones == null) {
            verificaciones = new ArrayList<>();
        }
        verificaciones.add(verificacion);
        usuario.setVerificacionEmail(verificaciones);

        // Guardar el usuario con la nueva verificación
        usuarioRepository.save(usuario);

        // Crear el enlace de verificación
        String link = "http://localhost:8080/UAO/apirest/VerificacionEmail/verificar?token=" + token;

        // Enviar el correo de verificación
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Verifica tu cuenta");
            message.setText("Haz clic aquí para verificar tu cuenta: " + link);
            mailSender.send(message);
            log.info("Correo enviado a {}", email);
        } catch (MailSendException e) {
            log.error("Error al enviar el correo de verificación - Problema con el envío: ", e);
        } catch (MailPreparationException e) {
            log.error("Error al preparar el correo de verificación: ", e);
        } catch (MailException e) {
            log.error("Error general de correo al enviar verificación: ", e);
        }
    }

    @Override
    public boolean verificarToken(String token) {
        // Buscar el usuario por el token de verificación
        Optional<Usuario> usuarioOpt = usuarioRepository.findByVerificacionEmail_Token(token);
        if (usuarioOpt.isEmpty()) {
            log.warn("Token de verificación no encontrado: {}", token);
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        List<VerificacionEmail> lista = usuario.getVerificacionEmail();

        if (lista == null || lista.isEmpty()) {
            log.warn("No se encontraron verificaciones para el usuario con ID {}", usuario.getId());
            return false;
        }

        // Obtener la última verificación
        VerificacionEmail verificacion = lista.get(lista.size() - 1);

        // Validar el token
        if (verificacion.getToken().equals(token)
                && verificacion.getFechaExpiracion().after(new Date())
                && !verificacion.getVerificado()) {

            // Marcar como verificado
            verificacion.setVerificado(true);
            usuarioRepository.save(usuario);
            log.info("Token verificado correctamente para el usuario con ID {}", usuario.getId());
            return true;
        }

        log.warn("El token no es válido o ya fue utilizado: {}", token);
        return false;
    }
}