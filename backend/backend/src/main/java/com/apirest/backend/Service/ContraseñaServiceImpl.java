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
import org.springframework.security.crypto.password.PasswordEncoder;
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
        verificacion.setTipoVerificacion("RESET_PASSWORD");

        // Si VerificacionEmail es una lista en Usuario
        List<VerificacionEmail> verificaciones = new ArrayList<>();
        verificaciones.add(verificacion);
        usuario.setVerificacionEmail(verificaciones);

        usuarioRepository.save(usuario);

        String link = "http://localhost:8080/UAO/apirest/password/formulario?token=" + token;

        try {
            // Intentar enviar correo HTML
            enviarCorreoHTML(email, usuario.getNombre(), link);
            log.info("Correo HTML de recuperación de contraseña enviado a {}", email);
        } catch (Exception e) {
            log.error("Error al enviar correo HTML de recuperación: {}", e.getMessage());
            try {
                // Si falla el HTML, intentar con texto plano como respaldo
                enviarCorreoTextoPlano(email, usuario.getNombre(), link);
                log.info("Correo texto plano de recuperación enviado a {}", email);
            } catch (MailException me) {
                log.error("Error al enviar correo texto plano de recuperación: {}", me.getMessage());
                throw new RuntimeException("Error al enviar el correo de recuperación. Verifica la configuración del servidor SMTP", me);
            }
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

        // Verificar tipo de verificación
        if (!"RESET_PASSWORD".equals(ve.getTipoVerificacion())) {
            log.warn("Token incorrecto para recuperación de contraseña: {}", usuario.getEmail());
            return false;
        }

        if (ve.getFechaExpiracion().before(new Date()) || ve.isVerificado()) {
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

    // Método de respaldo para enviar correo en texto plano
    private void enviarCorreoTextoPlano(String destinatario, String nombreUsuario, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Recuperación de Contraseña - Santorini Hills");

        message.setText(
                "¡Hola " + (nombreUsuario != null ? nombreUsuario : "Usuario") + "!\n\n" +
                        "Has solicitado restablecer tu contraseña en Santorini Hills.\n\n" +
                        "Por favor, haz clic en el siguiente enlace para crear una nueva contraseña:\n" +
                        link + "\n\n" +
                        "Este enlace es válido por 1 hora.\n\n" +
                        "Si no solicitaste este cambio, puedes ignorar este correo.\n\n" +
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
        helper.setSubject("Recuperación de Contraseña - Santorini Hills");

        // URL de la imagen que subiste a ImgBB (usando la misma que en VerificacionEmailServiceImpl)
        String logoUrl = "https://i.ibb.co/1YBBPhVC/Logo.png";

        // Plantilla HTML con imagen externa
        String htmlContent =
                "<!DOCTYPE html>" +
                        "<html lang=\"es\">" +
                        "<head>" +
                        "  <meta charset=\"UTF-8\">" +
                        "  <title>Recuperación de Contraseña - Santorini Hills</title>" +
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
                        "      <p>Has solicitado restablecer tu contraseña en <strong>Santorini Hills</strong>.</p>" +
                        "      <p>Por favor, haz clic en el siguiente botón para crear una nueva contraseña:</p>" +
                        "      <p style=\"text-align: center;\">" +
                        "        <a href=\"" + link + "\" class=\"button\">Restablecer Contraseña</a>" +
                        "      </p>" +
                        "      <p>Si el botón no funciona, copia y pega este enlace en tu navegador:</p>" +
                        "      <div class=\"link-box\">" +
                        link +
                        "      </div>" +
                        "      <p>Este enlace es válido por 1 hora. Si no solicitaste este cambio, puedes ignorar este mensaje.</p>" +
                        "      <p>Si no solicitaste este restablecimiento de contraseña, por favor, contacta con nuestro equipo de soporte inmediatamente.</p>" +
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