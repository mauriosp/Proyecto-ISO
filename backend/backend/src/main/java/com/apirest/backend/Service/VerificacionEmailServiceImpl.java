package com.apirest.backend.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
            // Intentar enviar correo HTML
            enviarCorreoHTML(email, usuario.getNombre(), link);
            log.info("Correo HTML enviado a {}", email);
        } catch (MessagingException | MailException e) {
            // Manejo de excepciones para errores en el envío de correo HTML
            log.error("Error al enviar correo HTML: {}", e.getMessage());

            // Intentar enviar correo en texto plano si falla el HTML
            try {
                enviarCorreoTextoPlano(email, usuario.getNombre(), link);
                log.info("Correo texto plano enviado a {}", email);
            } catch (MailException me) {
                // Manejo de errores al intentar enviar correo en texto plano
                log.error("Error al enviar correo texto plano: {}", me.getMessage());
                throw new RuntimeException("Error al enviar el correo. Verifica la configuración del servidor SMTP", me);
            }
        }
    }

    @Override
    public boolean verificarToken(String token) {
        // El método de verificación permanece igual
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

        // Buscar el token específico
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
        String patronEmail = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return email != null && email.matches(patronEmail);
    }

    // Método de respaldo para enviar correo en texto plano
    private void enviarCorreoTextoPlano(String destinatario, String nombreUsuario, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Verifica tu cuenta - Santorini Hills");

        message.setText(
                "¡Hola " + (nombreUsuario != null ? nombreUsuario : "Usuario") + "!\n\n" +
                        "Gracias por registrarte en nuestra plataforma de Santorini Hills.\n\n" +
                        "Por favor, haz clic en el siguiente enlace para verificar tu cuenta:\n" +
                        link + "\n\n" +
                        "Este enlace es válido por 24 horas.\n\n" +
                        "Si no solicitaste esta verificación, puedes ignorar este correo.\n\n" +
                        "Saludos,\n" +
                        "El equipo de Santorini Hills"
        );

        mailSender.send(message);
    }

    // Método para enviar correo HTML profesional con la plantilla de Santorini Hills
    private void enviarCorreoHTML(String destinatario, String nombreUsuario, String link) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(destinatario);
        helper.setSubject("Verifica tu cuenta - Santorini Hills");

        // URL de la imagen que subiste a ImgBB
        String logoUrl = "https://i.ibb.co/1YBBPhVC/Logo.png";

        // Plantilla HTML con imagen externa
        String htmlContent =
                "<!DOCTYPE html>" +
                        "<html lang=\"es\">" +
                        "<head>" +
                        "  <meta charset=\"UTF-8\">" +
                        "  <title>Verifica tu cuenta - Santorini Hills</title>" +
                        "  <style>" +
                        "    body {" +
                        "      font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;" +
                        "      background-color: #f0f2f5;" +
                        "      margin: 0;" +
                        "      padding: 0;" +
                        "    }" +
                        "    .container {" +
                        "      max-width: 600px;" +
                        "      margin: 40px auto;" +
                        "      background: #ffffff;" +
                        "      border-radius: 12px;" +
                        "      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);" +
                        "      overflow: hidden;" +
                        "    }" +
                        "    .header {" +
                        "      background-color: #ffffff;" +
                        "      text-align: center;" +
                        "      padding: 24px;" +
                        "    }" +
                        "    .header img {" +
                        "      height: 80px;" +
                        "      margin-bottom: 10px;" +
                        "    }" +
                        "    .header-title {" +
                        "      font-size: 22px;" +
                        "      font-weight: bold;" +
                        "      color: #1a3e5c;" +
                        "      margin-top: 8px;" +
                        "    }" +
                        "    .content {" +
                        "      padding: 32px;" +
                        "      color: #333333;" +
                        "      font-size: 16px;" +
                        "      line-height: 1.6;" +
                        "    }" +
                        "    .button {" +
                        "      display: inline-block;" +
                        "      padding: 14px 28px;" +
                        "      background-color: #1a3e5c;" +
                        "      color: white !important;" +
                        "      text-decoration: none;" +
                        "      border-radius: 8px;" +
                        "      font-weight: bold;" +
                        "      margin: 20px 0;" +
                        "    }" +
                        "    .link-box {" +
                        "      background: #f7f9fc;" +
                        "      padding: 12px;" +
                        "      font-size: 14px;" +
                        "      word-break: break-word;" +
                        "      border-left: 4px solid #1a3e5c;" +
                        "      margin: 16px 0;" +
                        "    }" +
                        "    .footer {" +
                        "      background-color: #f2f2f2;" +
                        "      text-align: center;" +
                        "      font-size: 12px;" +
                        "      color: #888888;" +
                        "      padding: 20px;" +
                        "    }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class=\"container\">" +
                        "    <div class=\"header\">" +
                        "      <img src=\"" + logoUrl + "\" alt=\"Santorini Hills Logo\" style=\"height:80px;\">" +
                        "      <div class=\"header-title\">Santorini Hills</div>" +
                        "    </div>" +
                        "    <div class=\"content\">" +
                        "      <p>Hola <strong>" + (nombreUsuario != null ? nombreUsuario : "Usuario") + "</strong>,</p>" +
                        "      <p>¡Gracias por unirte a <strong>Santorini Hills</strong>! Estamos encantados de tenerte con nosotros.</p>" +
                        "      <p>Para comenzar a publicar, vender o comprar inmuebles en nuestra plataforma, primero necesitas verificar tu correo electrónico.</p>" +
                        "      <p style=\"text-align: center;\">" +
                        "        <a href=\"" + link + "\" class=\"button\">Verificar mi cuenta</a>" +
                        "      </p>" +
                        "      <p>Si el botón no funciona, copia y pega este enlace en tu navegador:</p>" +
                        "      <div class=\"link-box\">" +
                        link +
                        "      </div>" +
                        "      <p>Este enlace es válido por 24 horas. Si no solicitaste esta verificación, puedes ignorar este mensaje.</p>" +
                        "    </div>" +
                        "    <div class=\"footer\">" +
                        "      © " + Calendar.getInstance().get(Calendar.YEAR) + " Santorini Hills. Todos los derechos reservados.<br>" +
                        "      Este es un correo automático, por favor no respondas a este mensaje." +
                        "    </div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";

        helper.setText(htmlContent, true); // true indica que es contenido HTML
        mailSender.send(message);
    }
}
