package com.apirest.backend.Controller.Verificaciones;

import java.util.Calendar;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Service.IVerificacionEmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/UAO/apirest/VerificacionEmail")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VerificacionEmailController {

    private final IVerificacionEmailService verificacionEmailService;

    @GetMapping("/verificar")
    public ResponseEntity<?> verificarCuenta(@RequestParam("token") String token) {
        try {
            boolean verificado = verificacionEmailService.verificarToken(token);
            if (verificado) {
                // Página HTML de éxito
                String htmlResponse =
                        "<!DOCTYPE html>" +
                                "<html lang='es'>" +
                                "<head>" +
                                "    <meta charset='UTF-8'>" +
                                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                                "    <title>Cuenta Verificada - Santorini Hills</title>" +
                                "    <style>" +
                                "        body {" +
                                "            font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;" +
                                "            background-color: #f0f2f5;" +
                                "            margin: 0;" +
                                "            padding: 0;" +
                                "            display: flex;" +
                                "            justify-content: center;" +
                                "            align-items: center;" +
                                "            min-height: 100vh;" +
                                "        }" +
                                "        .container {" +
                                "            max-width: 600px;" +
                                "            width: 90%;" +
                                "            background: #ffffff;" +
                                "            border-radius: 12px;" +
                                "            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);" +
                                "            overflow: hidden;" +
                                "            text-align: center;" +
                                "        }" +
                                "        .header {" +
                                "            background-color: #1a3e5c;" +
                                "            padding: 30px 20px;" +
                                "            color: white;" +
                                "        }" +
                                "        .header img {" +
                                "            height: 80px;" +
                                "            margin-bottom: 15px;" +
                                "        }" +
                                "        .header h1 {" +
                                "            margin: 0;" +
                                "            font-size: 28px;" +
                                "            font-weight: 600;" +
                                "        }" +
                                "        .content {" +
                                "            padding: 40px 30px;" +
                                "        }" +
                                "        .success-icon {" +
                                "            width: 80px;" +
                                "            height: 80px;" +
                                "            background-color: #4BB543;" +
                                "            border-radius: 50%;" +
                                "            display: flex;" +
                                "            justify-content: center;" +
                                "            align-items: center;" +
                                "            margin: 0 auto 20px auto;" +
                                "        }" +
                                "        .success-icon svg {" +
                                "            width: 40px;" +
                                "            height: 40px;" +
                                "            fill: white;" +
                                "        }" +
                                "        h2 {" +
                                "            color: #333;" +
                                "            font-size: 24px;" +
                                "            margin-bottom: 15px;" +
                                "        }" +
                                "        p {" +
                                "            color: #666;" +
                                "            font-size: 16px;" +
                                "            line-height: 1.6;" +
                                "            margin-bottom: 25px;" +
                                "        }" +
                                "        .login-button {" +
                                "            display: inline-block;" +
                                "            background-color: #1a3e5c;" +
                                "            color: white;" +
                                "            text-decoration: none;" +
                                "            padding: 12px 30px;" +
                                "            border-radius: 6px;" +
                                "            font-weight: 600;" +
                                "            transition: background-color 0.3s;" +
                                "        }" +
                                "        .login-button:hover {" +
                                "            background-color: #15334b;" +
                                "        }" +
                                "        .footer {" +
                                "            margin-top: 30px;" +
                                "            color: #999;" +
                                "            font-size: 14px;" +
                                "        }" +
                                "    </style>" +
                                "</head>" +
                                "<body>" +
                                "    <div class='container'>" +
                                "        <div class='header'>" +
                                "            <img src='https://i.ibb.co/1YBBPhVC/Logo.png' alt='Santorini Hills Logo'>" +
                                "            <h1>Santorini Hills</h1>" +
                                "        </div>" +
                                "        <div class='content'>" +
                                "            <div class='success-icon'>" +
                                "                <svg viewBox='0 0 24 24'>" +
                                "                    <path d='M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41L9 16.17z'/>" +
                                "                </svg>" +
                                "            </div>" +
                                "            <h2>¡Cuenta verificada correctamente!</h2>" +
                                "            <p>¡Gracias por verificar tu correo electrónico! Tu cuenta ha sido activada y ahora tienes acceso completo a todos los servicios de Santorini Hills.</p>" +
                                "            <a href='http://localhost:8080/login' class='login-button'>Iniciar sesión</a>" +
                                "            <p class='footer'>© " + Calendar.getInstance().get(Calendar.YEAR) + " Santorini Hills. Todos los derechos reservados.</p>" +
                                "        </div>" +
                                "    </div>" +
                                "</body>" +
                                "</html>";

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                        .body(htmlResponse);
            } else {
                // Página HTML de error para token inválido o expirado
                String htmlError =
                        "<!DOCTYPE html>" +
                                "<html lang='es'>" +
                                "<head>" +
                                "    <meta charset='UTF-8'>" +
                                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                                "    <title>Error de verificación - Santorini Hills</title>" +
                                "    <style>" +
                                "        body {" +
                                "            font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;" +
                                "            background-color: #f0f2f5;" +
                                "            margin: 0;" +
                                "            padding: 0;" +
                                "            display: flex;" +
                                "            justify-content: center;" +
                                "            align-items: center;" +
                                "            min-height: 100vh;" +
                                "        }" +
                                "        .container {" +
                                "            max-width: 600px;" +
                                "            width: 90%;" +
                                "            background: #ffffff;" +
                                "            border-radius: 12px;" +
                                "            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);" +
                                "            overflow: hidden;" +
                                "            text-align: center;" +
                                "        }" +
                                "        .header {" +
                                "            background-color: #1a3e5c;" +
                                "            padding: 30px 20px;" +
                                "            color: white;" +
                                "        }" +
                                "        .header img {" +
                                "            height: 80px;" +
                                "            margin-bottom: 15px;" +
                                "        }" +
                                "        .header h1 {" +
                                "            margin: 0;" +
                                "            font-size: 28px;" +
                                "            font-weight: 600;" +
                                "        }" +
                                "        .content {" +
                                "            padding: 40px 30px;" +
                                "        }" +
                                "        .error-icon {" +
                                "            width: 80px;" +
                                "            height: 80px;" +
                                "            background-color: #ff3b30;" +
                                "            border-radius: 50%;" +
                                "            display: flex;" +
                                "            justify-content: center;" +
                                "            align-items: center;" +
                                "            margin: 0 auto 20px auto;" +
                                "        }" +
                                "        .error-icon svg {" +
                                "            width: 40px;" +
                                "            height: 40px;" +
                                "            fill: white;" +
                                "        }" +
                                "        h2 {" +
                                "            color: #333;" +
                                "            font-size: 24px;" +
                                "            margin-bottom: 15px;" +
                                "        }" +
                                "        p {" +
                                "            color: #666;" +
                                "            font-size: 16px;" +
                                "            line-height: 1.6;" +
                                "            margin-bottom: 25px;" +
                                "        }" +
                                "        .retry-button {" +
                                "            display: inline-block;" +
                                "            background-color: #1a3e5c;" +
                                "            color: white;" +
                                "            text-decoration: none;" +
                                "            padding: 12px 30px;" +
                                "            border-radius: 6px;" +
                                "            font-weight: 600;" +
                                "            transition: background-color 0.3s;" +
                                "        }" +
                                "        .retry-button:hover {" +
                                "            background-color: #15334b;" +
                                "        }" +
                                "        .footer {" +
                                "            margin-top: 30px;" +
                                "            color: #999;" +
                                "            font-size: 14px;" +
                                "        }" +
                                "    </style>" +
                                "</head>" +
                                "<body>" +
                                "    <div class='container'>" +
                                "        <div class='header'>" +
                                "            <img src='https://i.ibb.co/1YBBPhVC/Logo.png' alt='Santorini Hills Logo'>" +
                                "            <h1>Santorini Hills</h1>" +
                                "        </div>" +
                                "        <div class='content'>" +
                                "            <div class='error-icon'>" +
                                "                <svg viewBox='0 0 24 24'>" +
                                "                    <path d='M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z'/>" +
                                "                </svg>" +
                                "            </div>" +
                                "            <h2>Error de verificación</h2>" +
                                "            <p>Lo sentimos, el enlace de verificación es inválido o ha expirado. Los enlaces de verificación son válidos por 24 horas.</p>" +
                                "            <a href='http://localhost:8080/UAO/apirest/VerificacionEmail/reenviar' class='retry-button'>Solicitar nuevo enlace</a>" +
                                "            <p class='footer'>© " + Calendar.getInstance().get(Calendar.YEAR) + " Santorini Hills. Todos los derechos reservados.</p>" +
                                "        </div>" +
                                "    </div>" +
                                "</body>" +
                                "</html>";

                return ResponseEntity.badRequest()
                        .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                        .body(htmlError);
            }
        } catch (Exception e) {
            // Página HTML para errores del servidor
            String htmlServerError =
                    "<!DOCTYPE html>" +
                            "<html lang='es'>" +
                            "<head>" +
                            "    <meta charset='UTF-8'>" +
                            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                            "    <title>Error - Santorini Hills</title>" +
                            "    <style>" +
                            "        body {" +
                            "            font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;" +
                            "            background-color: #f0f2f5;" +
                            "            margin: 0;" +
                            "            padding: 0;" +
                            "            display: flex;" +
                            "            justify-content: center;" +
                            "            align-items: center;" +
                            "            min-height: 100vh;" +
                            "        }" +
                            "        .container {" +
                            "            max-width: 600px;" +
                            "            width: 90%;" +
                            "            background: #ffffff;" +
                            "            border-radius: 12px;" +
                            "            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);" +
                            "            overflow: hidden;" +
                            "            text-align: center;" +
                            "        }" +
                            "        .header {" +
                            "            background-color: #1a3e5c;" +
                            "            padding: 30px 20px;" +
                            "            color: white;" +
                            "        }" +
                            "        .header img {" +
                            "            height: 80px;" +
                            "            margin-bottom: 15px;" +
                            "        }" +
                            "        .header h1 {" +
                            "            margin: 0;" +
                            "            font-size: 28px;" +
                            "            font-weight: 600;" +
                            "        }" +
                            "        .content {" +
                            "            padding: 40px 30px;" +
                            "        }" +
                            "        .error-icon {" +
                            "            width: 80px;" +
                            "            height: 80px;" +
                            "            background-color: #ff9500;" +
                            "            border-radius: 50%;" +
                            "            display: flex;" +
                            "            justify-content: center;" +
                            "            align-items: center;" +
                            "            margin: 0 auto 20px auto;" +
                            "        }" +
                            "        .error-icon svg {" +
                            "            width: 40px;" +
                            "            height: 40px;" +
                            "            fill: white;" +
                            "        }" +
                            "        h2 {" +
                            "            color: #333;" +
                            "            font-size: 24px;" +
                            "            margin-bottom: 15px;" +
                            "        }" +
                            "        p {" +
                            "            color: #666;" +
                            "            font-size: 16px;" +
                            "            line-height: 1.6;" +
                            "            margin-bottom: 25px;" +
                            "        }" +
                            "        .home-button {" +
                            "            display: inline-block;" +
                            "            background-color: #1a3e5c;" +
                            "            color: white;" +
                            "            text-decoration: none;" +
                            "            padding: 12px 30px;" +
                            "            border-radius: 6px;" +
                            "            font-weight: 600;" +
                            "            transition: background-color 0.3s;" +
                            "        }" +
                            "        .home-button:hover {" +
                            "            background-color: #15334b;" +
                            "        }" +
                            "        .footer {" +
                            "            margin-top: 30px;" +
                            "            color: #999;" +
                            "            font-size: 14px;" +
                            "        }" +
                            "    </style>" +
                            "</head>" +
                            "<body>" +
                            "    <div class='container'>" +
                            "        <div class='header'>" +
                            "            <img src='https://i.ibb.co/1YBBPhVC/Logo.png' alt='Santorini Hills Logo'>" +
                            "            <h1>Santorini Hills</h1>" +
                            "        </div>" +
                            "        <div class='content'>" +
                            "            <div class='error-icon'>" +
                            "                <svg viewBox='0 0 24 24'>" +
                            "                    <path d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z'/>" +
                            "                </svg>" +
                            "            </div>" +
                            "            <h2>Ha ocurrido un error</h2>" +
                            "            <p>Lo sentimos, ha ocurrido un error al procesar tu solicitud: " + e.getMessage() + "</p>" +
                            "            <a href='http://localhost:8080' class='home-button'>Ir a la página principal</a>" +
                            "            <p class='footer'>© " + Calendar.getInstance().get(Calendar.YEAR) + " Santorini Hills. Todos los derechos reservados.</p>" +
                            "        </div>" +
                            "    </div>" +
                            "</body>" +
                            "</html>";

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                    .body(htmlServerError);
        }
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarVerificacion(
            @RequestParam("userId") String userId,
            @RequestParam("email") String email) {
        try {
            verificacionEmailService.enviarCorreoVerificacion(userId, email);
            return ResponseEntity.ok("Correo de verificación enviado con éxito. Por favor revisa tu bandeja de entrada.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar correo: " + e.getMessage());
        }
    }
}