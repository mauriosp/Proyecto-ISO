package com.apirest.backend.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailBrevoServiceImpl {

    @Value("${brevo.smtp.username:8ddb16002@smtp-brevo.com}")
    private String brevoUsername;
    
    @Value("${brevo.smtp.password:Y83hCXBgvyfzAcas}")
    private String brevoPassword;

    public boolean enviarEmail(String emailDestino, String asunto, String contenidoHtml) {
        try {
            log.info("Iniciando configuración de Brevo...");
            log.info("Username: {}", brevoUsername);
            log.info("Password: {}***", brevoPassword.substring(0, Math.min(4, brevoPassword.length())));
            
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            
        
            mailSender.setHost("smtp-relay.brevo.com"); 
            mailSender.setPort(587);
            mailSender.setUsername(brevoUsername);
            mailSender.setPassword(brevoPassword);

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "false"); // 
            
            // CONFIGURACIÓN SSL 
            props.put("mail.smtp.ssl.trust", "*"); 
            props.put("mail.smtp.ssl.checkserveridentity", "false"); 
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            props.put("mail.smtp.connectiontimeout", "10000");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.writetimeout", "10000");

            log.info("Conectando a: smtp-relay.brevo.com:587");
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("santorinihills@gmail.com", "Santorini Hills"); 
            helper.setTo(emailDestino);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true);

            log.info("Enviando email a: {}", emailDestino);
            mailSender.send(message);
            log.info("Email enviado exitosamente con Brevo a: {}", emailDestino);
            return true;

        } catch (MessagingException | UnsupportedEncodingException | MailException e) {
            log.error(" Error detallado enviando email con Brevo: {}", e.getMessage());
            log.error("Tipo de excepción: {}", e.getClass().getSimpleName());
            if (e.getCause() != null) {
                log.error(" Causa: {}", e.getCause().getMessage());
            }
            return false;
        }
    }

    
    public String crearEmailVerificacion(String nombreUsuario, String linkVerificacion) {
        String logoUrl = "https://i.ibb.co/Mx6y1Z2J/Logo.png";

        return "<!DOCTYPE html>" +
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
                "        <a href=\"" + linkVerificacion + "\" class=\"button\">Verificar mi cuenta</a>" +
                "      </p>" +
                "      <p>Si el botón no funciona, copia y pega este enlace en tu navegador:</p>" +
                "      <div class=\"link-box\">" +
                linkVerificacion +
                "      </div>" +
                "      <p>Este enlace es válido por 24 horas. Si no solicitaste esta verificación, puedes ignorar este mensaje.</p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "      © 2025 Santorini Hills. Todos los derechos reservados.<br>" +
                "      Este es un correo automático, por favor no respondas a este mensaje." +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    public String crearEmailRecuperacion(String nombreUsuario, String linkRecuperacion) {
        String logoUrl = "https://i.ibb.co/Mx6y1Z2J/Logo.png";

        return "<!DOCTYPE html>" +
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
                "        <a href=\"" + linkRecuperacion + "\" class=\"button\">Restablecer Contraseña</a>" +
                "      </p>" +
                "      <p>Si el botón no funciona, copia y pega este enlace en tu navegador:</p>" +
                "      <div class=\"link-box\">" +
                linkRecuperacion +
                "      </div>" +
                "      <p>Este enlace es válido por 1 hora. Si no solicitaste este cambio, puedes ignorar este mensaje.</p>" +
                "      <p>Si no solicitaste este restablecimiento de contraseña, por favor, contacta con nuestro equipo de soporte inmediatamente.</p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "      © 2025 Santorini Hills. Todos los derechos reservados.<br>" +
                "      Este es un correo automático, por favor no respondas a este mensaje." +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }
}