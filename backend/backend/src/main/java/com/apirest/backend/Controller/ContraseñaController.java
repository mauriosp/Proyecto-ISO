package com.apirest.backend.Controller;

import com.apirest.backend.Dto.CambioContraseñaDTO;
import com.apirest.backend.Model.Usuario;
import com.apirest.backend.Model.VerificacionEmail;
import com.apirest.backend.Repository.UsuarioRepository;
import com.apirest.backend.Service.IContraseñaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/UAO/apirest/password")
@RequiredArgsConstructor
public class ContraseñaController {

    private final IContraseñaService contraseñaService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/reset")
    public ResponseEntity<?> actualizarContraseña(@RequestBody CambioContraseñaDTO cambioContraseñaDTO) {
        // Validar que las contraseñas coincidan
        if (!cambioContraseñaDTO.getNuevaContraseña().equals(cambioContraseñaDTO.getConfirmarContraseña())) {
            return ResponseEntity.badRequest().body("Las contraseñas no coinciden");
        }

        // Validar complejidad de la contraseña
        if (!validarComplejidadContraseña(cambioContraseñaDTO.getNuevaContraseña())) {
            return ResponseEntity.badRequest()
                    .body("La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas, números y caracteres especiales");
        }

        // Intentar actualizar la contraseña
        boolean actualizado = contraseñaService.actualizarContraseña(
                cambioContraseñaDTO.getToken(),
                cambioContraseñaDTO.getNuevaContraseña()
        );

        if (actualizado) {
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } else {
            return ResponseEntity.badRequest()
                    .body("No se pudo actualizar la contraseña. El token puede ser inválido o haber expirado");
        }
    }

    private boolean validarComplejidadContraseña(String contraseña) {
        // Al menos 8 caracteres
        if (contraseña.length() < 8) return false;

        // Al menos una mayúscula
        if (!contraseña.matches(".*[A-Z].*")) return false;

        // Al menos una minúscula
        if (!contraseña.matches(".*[a-z].*")) return false;

        // Al menos un número
        if (!contraseña.matches(".*\\d.*")) return false;

        // Al menos un carácter especial
        return contraseña.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
    }
    
    @PostMapping("/solicitar-reset")
    public ResponseEntity<?> solicitarResetContraseña(@RequestParam String email) {
        contraseñaService.enviarCorreoResetContraseña(email);
        // Por seguridad, siempre devuelve un mensaje positivo para evitar enumerar usuarios
        return ResponseEntity.ok("Si el email está registrado, recibirás un correo con instrucciones para restablecer tu contraseña");
    }
    
    @GetMapping("/formulario")
    public ResponseEntity<?> mostrarFormularioReset(@RequestParam String token) {
        // Verificar que el token es válido y no ha expirado
        Optional<Usuario> usuarioOpt = usuarioRepository.findByVerificacionEmail_Token(token);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Token inválido");
        }

        Usuario usuario = usuarioOpt.get();
        // Obtener la verificación de email que contiene el token
        List<VerificacionEmail> verificaciones = usuario.getVerificacionEmail();
        VerificacionEmail ve = null;
        
        for (VerificacionEmail v : verificaciones) {
            if (token.equals(v.getToken())) {
                ve = v;
                break;
            }
        }
        
        if (ve == null) {
            return ResponseEntity.badRequest().body("Token inválido");
        }
        

        if (ve.getFechaExpiracion().before(new Date()) || ve.getVerificado()) {
            return ResponseEntity.badRequest().body("El enlace ha expirado o ya ha sido utilizado");
        }

        // En una aplicación real, aquí devolverías una vista HTML con el formulario
        // Para esta API RESTful, podemos devolver un mensaje indicando que el token es válido
        return ResponseEntity.ok("Token válido, puedes proceder a actualizar tu contraseña");
    }
}