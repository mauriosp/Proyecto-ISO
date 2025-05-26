package com.apirest.backend.Controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.backend.Dto.CambioContraseñaDTO;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;
import com.apirest.backend.Service.IContraseñaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/UAO/apirest/password")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class ContraseñaController {

    private final IContraseñaService contraseñaService;
    private final UsuarioRepository usuarioRepository;

    // ENDPOINT PARA JSON (APIs como Postman)
    @PostMapping(value = "/reset", 
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> actualizarContraseñaJSON(@RequestBody CambioContraseñaDTO cambioContraseñaDTO) {
        return procesarCambioContraseña(
            cambioContraseñaDTO.getToken(),
            cambioContraseñaDTO.getNuevaContraseña(),
            cambioContraseñaDTO.getConfirmarContraseña()
        );
    }

    // ENDPOINT PARA FORM DATA (Formularios web)
    @PostMapping(value = "/reset", 
                 consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                 produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> actualizarContraseñaForm(
            @RequestParam String token,
            @RequestParam String nuevaContraseña,
            @RequestParam String confirmarContraseña) {
        return procesarCambioContraseña(token, nuevaContraseña, confirmarContraseña);
    }

    // MÉTODO AUXILIAR: Procesa el cambio de contraseña
    private ResponseEntity<?> procesarCambioContraseña(String token, String nuevaContraseña, String confirmarContraseña) {
        // Validar que las contraseñas coincidan
        if (!nuevaContraseña.equals(confirmarContraseña)) {
            return crearRespuestaHTML(
                "Error de validación",
                "Las contraseñas no coinciden",
                "Por favor, asegúrate de que ambas contraseñas sean idénticas.",
                "error",
                false
            );
        }

        // Validar complejidad de la contraseña
        if (!validarComplejidadContraseña(nuevaContraseña)) {
            return crearRespuestaHTML(
                "Contraseña no válida",
                "La contraseña no cumple con los requisitos de seguridad",
                "La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas, números y caracteres especiales.",
                "error",
                false
            );
        }

        // Intentar actualizar la contraseña
        boolean actualizado = contraseñaService.actualizarContraseña(token, nuevaContraseña);

        if (actualizado) {
            return crearRespuestaHTML(
                "¡Contraseña actualizada!",
                "Tu contraseña ha sido actualizada correctamente",
                "Ya puedes iniciar sesión con tu nueva contraseña.",
                "success",
                true
            );
        } else {
            return crearRespuestaHTML(
                "Error al actualizar contraseña",
                "No se pudo actualizar la contraseña",
                "El enlace puede ser inválido o haber expirado. Por favor, solicita un nuevo enlace de recuperación.",
                "error",
                false
            );
        }
    }

    @PostMapping("/solicitar-reset")
    public ResponseEntity<?> solicitarResetContraseña(@RequestParam String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            // Por seguridad, mostramos el mismo mensaje aunque el email no exista
            return crearRespuestaHTMLSimple(
                "Solicitud procesada",
                "Si el email está registrado, recibirás instrucciones para restablecer tu contraseña",
                "Por favor, revisa tu bandeja de entrada y carpeta de spam.",
                "info"
            );
        }
        
        try {
            contraseñaService.enviarCorreoResetContraseña(email);
            return crearRespuestaHTMLSimple(
                "Solicitud procesada",
                "Si el email está registrado, recibirás instrucciones para restablecer tu contraseña",
                "Por favor, revisa tu bandeja de entrada y carpeta de spam.",
                "info"
            );
        } catch (Exception e) {
            return crearRespuestaHTMLSimple(
                "Error del sistema",
                "Hubo un problema al procesar tu solicitud",
                "Por favor, intenta nuevamente más tarde o contacta al soporte.",
                "error"
            );
        }
    }

    @GetMapping("/formulario")
    public ResponseEntity<?> mostrarFormularioReset(@RequestParam String token) {
        // Buscar el usuario que tiene el token en su verificación de correo
        Optional<Usuario> usuarioOpt = usuarioRepository.findByVerificacionEmail_Token(token);
        
        if (usuarioOpt.isEmpty()) {
            return crearRespuestaHTML(
                "Token inválido",
                "El enlace no es válido",
                "El enlace de recuperación que utilizaste no es válido o no existe.",
                "error",
                false
            );
        }

        Usuario usuario = usuarioOpt.get();
        // Buscar la verificación de correo electrónico con ese token
        VerificacionEmail ve = usuario.getVerificacionEmail().stream()
                                      .filter(v -> v.getToken().equals(token))
                                      .findFirst()
                                      .orElse(null);
        
        if (ve == null) {
            return crearRespuestaHTML(
                "Token inválido",
                "El enlace no es válido",
                "El enlace de recuperación que utilizaste no es válido o no existe.",
                "error",
                false
            );
        }

        // Verificar si el enlace ha expirado o si ya ha sido utilizado
        if (ve.getFechaExpiracion().before(new Date()) || ve.isVerificado()) {
            return crearRespuestaHTML(
                "Enlace expirado",
                "El enlace ha expirado o ya ha sido utilizado",
                "Por favor, solicita un nuevo enlace de recuperación de contraseña.",
                "warning",
                false
            );
        }

        // Si el token es válido, mostrar el formulario
        return crearFormularioResetHTML(token);
    }

    private boolean validarComplejidadContraseña(String contraseña) {
        if (contraseña.length() < 8) return false;
        if (!contraseña.matches(".*[A-Z].*")) return false;
        if (!contraseña.matches(".*[a-z].*")) return false;
        if (!contraseña.matches(".*\\d.*")) return false;
        return contraseña.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
    }

    private ResponseEntity<?> crearRespuestaHTML(String titulo, String mensaje, String descripcion, String tipo, boolean mostrarBotonLogin) {
        String tipoClase = tipo.equals("success") ? "success" : tipo.equals("error") ? "error" : tipo.equals("warning") ? "warning" : "info";
        String iconoSVG = obtenerIconoSVG(tipo);
        
        String htmlContent = 
            "<!DOCTYPE html>" +
            "<html lang='es'>" +
            "<head>" +
            "    <meta charset='UTF-8'>" +
            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "    <title>" + titulo + " - Santorini Hills</title>" +
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
            "        .icon {" +
            "            width: 80px;" +
            "            height: 80px;" +
            "            border-radius: 50%;" +
            "            display: flex;" +
            "            justify-content: center;" +
            "            align-items: center;" +
            "            margin: 0 auto 20px auto;" +
            "        }" +
            "        .icon-success { background-color: #4BB543; }" +
            "        .icon-error { background-color: #ff3b30; }" +
            "        .icon-warning { background-color: #ff9500; }" +
            "        .icon-info { background-color: #007aff; }" +
            "        .icon svg {" +
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
            "        .button {" +
            "            display: inline-block;" +
            "            background-color: #1a3e5c;" +
            "            color: white;" +
            "            text-decoration: none;" +
            "            padding: 12px 30px;" +
            "            border-radius: 6px;" +
            "            font-weight: 600;" +
            "            transition: background-color 0.3s;" +
            "        }" +
            "        .button:hover {" +
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
            "            <div class='icon icon-" + tipoClase + "'>" +
            "                " + iconoSVG +
            "            </div>" +
            "            <h2>" + mensaje + "</h2>" +
            "            <p>" + descripcion + "</p>" +
            (mostrarBotonLogin ? "            <a href='http://localhost:8080/login' class='button'>Iniciar sesión</a>" : "") +
            "            <p class='footer'>© " + Calendar.getInstance().get(Calendar.YEAR) + " Santorini Hills. Todos los derechos reservados.</p>" +
            "        </div>" +
            "    </div>" +
            "</body>" +
            "</html>";

        HttpStatus status = tipo.equals("error") ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return ResponseEntity.status(status)
                .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                .body(htmlContent);
    }

    private ResponseEntity<?> crearRespuestaHTMLSimple(String titulo, String mensaje, String descripcion, String tipo) {
        return crearRespuestaHTML(titulo, mensaje, descripcion, tipo, false);
    }

    private ResponseEntity<?> crearFormularioResetHTML(String token) {
        String htmlContent =
            "<!DOCTYPE html>" +
            "<html lang='es'>" +
            "<head>" +
            "    <meta charset='UTF-8'>" +
            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "    <title>Nueva Contraseña - Santorini Hills</title>" +
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
            "        }" +
            "        .header {" +
            "            background-color: #1a3e5c;" +
            "            padding: 30px 20px;" +
            "            color: white;" +
            "            text-align: center;" +
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
            "        h2 {" +
            "            color: #333;" +
            "            font-size: 24px;" +
            "            margin-bottom: 15px;" +
            "            text-align: center;" +
            "        }" +
            "        .form-group {" +
            "            margin-bottom: 20px;" +
            "        }" +
            "        label {" +
            "            display: block;" +
            "            color: #333;" +
            "            font-weight: 600;" +
            "            margin-bottom: 8px;" +
            "        }" +
            "        input[type='password'] {" +
            "            width: 100%;" +
            "            padding: 12px;" +
            "            border: 1px solid #ddd;" +
            "            border-radius: 6px;" +
            "            font-size: 16px;" +
            "            transition: border-color 0.3s;" +
            "            box-sizing: border-box;" +
            "        }" +
            "        input[type='password']:focus {" +
            "            outline: none;" +
            "            border-color: #1a3e5c;" +
            "        }" +
            "        .button {" +
            "            width: 100%;" +
            "            background-color: #1a3e5c;" +
            "            color: white;" +
            "            border: none;" +
            "            padding: 12px 30px;" +
            "            border-radius: 6px;" +
            "            font-size: 16px;" +
            "            font-weight: 600;" +
            "            cursor: pointer;" +
            "            transition: background-color 0.3s;" +
            "        }" +
            "        .button:hover {" +
            "            background-color: #15334b;" +
            "        }" +
            "        .requirements {" +
            "            background-color: #f8f9fa;" +
            "            padding: 15px;" +
            "            border-radius: 6px;" +
            "            margin-top: 20px;" +
            "        }" +
            "        .requirements h3 {" +
            "            margin: 0 0 10px 0;" +
            "            color: #333;" +
            "            font-size: 16px;" +
            "        }" +
            "        .requirements ul {" +
            "            margin: 0;" +
            "            padding-left: 20px;" +
            "            color: #666;" +
            "        }" +
            "        .requirements li {" +
            "            margin-bottom: 5px;" +
            "        }" +
            "        .footer {" +
            "            text-align: center;" +
            "            margin-top: 30px;" +
            "            color: #999;" +
            "            font-size: 14px;" +
            "        }" +
            "        .error-message {" +
            "            color: #ff3b30;" +
            "            font-size: 14px;" +
            "            margin-top: 10px;" +
            "            display: none;" +
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
            "            <h2>Crear nueva contraseña</h2>" +
            "            <form id='resetForm' method='POST' action='/UAO/apirest/password/reset'>" +
            "                <input type='hidden' name='token' value='" + token + "'>" +
            "                <div class='form-group'>" +
            "                    <label for='nuevaContraseña'>Nueva contraseña</label>" +
            "                    <input type='password' id='nuevaContraseña' name='nuevaContraseña' required>" +
            "                </div>" +
            "                <div class='form-group'>" +
            "                    <label for='confirmarContraseña'>Confirmar contraseña</label>" +
            "                    <input type='password' id='confirmarContraseña' name='confirmarContraseña' required>" +
            "                    <div class='error-message' id='errorMessage'>Las contraseñas no coinciden</div>" +
            "                </div>" +
            "                <button type='submit' class='button'>Actualizar contraseña</button>" +
            "            </form>" +
            "            <div class='requirements'>" +
            "                <h3>La contraseña debe tener:</h3>" +
            "                <ul>" +
            "                    <li>Al menos 8 caracteres</li>" +
            "                    <li>Una letra mayúscula</li>" +
            "                    <li>Una letra minúscula</li>" +
            "                    <li>Un número</li>" +
            "                    <li>Un carácter especial</li>" +
            "                </ul>" +
            "            </div>" +
            "            <p class='footer'>© " + Calendar.getInstance().get(Calendar.YEAR) + " Santorini Hills. Todos los derechos reservados.</p>" +
            "        </div>" +
            "    </div>" +
            "    <script>" +
            "        document.getElementById('resetForm').addEventListener('submit', function(e) {" +
            "            const nuevaContraseña = document.getElementById('nuevaContraseña').value;" +
            "            const confirmarContraseña = document.getElementById('confirmarContraseña').value;" +
            "            const errorMessage = document.getElementById('errorMessage');" +
            "            " +
            "            if (nuevaContraseña !== confirmarContraseña) {" +
            "                e.preventDefault();" +
            "                errorMessage.style.display = 'block';" +
            "                return false;" +
            "            }" +
            "            " +
            "            errorMessage.style.display = 'none';" +
            "        });" +
            "    </script>" +
            "</body>" +
            "</html>";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                .body(htmlContent);
    }

    private String obtenerIconoSVG(String tipo) {
        switch (tipo) {
            case "success":
                return "<svg viewBox='0 0 24 24'><path d='M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41L9 16.17z'/></svg>";
            case "error":
                return "<svg viewBox='0 0 24 24'><path d='M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z'/></svg>";
            case "warning":
                return "<svg viewBox='0 0 24 24'><path d='M12 5.99L19.53 19H4.47L12 5.99M12 2L1 21h22L12 2zm1 14h-2v2h2v-2zm0-6h-2v4h2v-4z'/></svg>";
            case "info":
            default:
                return "<svg viewBox='0 0 24 24'><path d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z'/></svg>";
        }
    }
}