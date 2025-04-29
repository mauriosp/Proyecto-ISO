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
        if (contraseña.length() < 8) return false;
        if (!contraseña.matches(".*[A-Z].*")) return false;
        if (!contraseña.matches(".*[a-z].*")) return false;
        if (!contraseña.matches(".*\\d.*")) return false;
        return contraseña.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
    }

    @PostMapping("/solicitar-reset")
    public ResponseEntity<?> solicitarResetContraseña(@RequestParam String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body("El correo no está registrado en el sistema");
        }
        contraseñaService.enviarCorreoResetContraseña(email);
        return ResponseEntity.ok("Si el email está registrado, recibirás un correo con instrucciones para restablecer tu contraseña");
    }

    @GetMapping("/formulario")
    public ResponseEntity<?> mostrarFormularioReset(@RequestParam String token) {
        // Buscar el usuario que tiene el token en su verificación de correo
        Optional<Usuario> usuarioOpt = usuarioRepository.findByVerificacionEmail_Token(token);
        
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Token inválido");
        }

        Usuario usuario = usuarioOpt.get();
        // Buscar la verificación de correo electrónico con ese token
        VerificacionEmail ve = usuario.getVerificacionEmail().stream()
                                      .filter(v -> v.getToken().equals(token))
                                      .findFirst()
                                      .orElse(null);
        
        if (ve == null) {
            return ResponseEntity.badRequest().body("Token inválido");
        }

        // Verificar si el enlace ha expirado o si ya ha sido utilizado
        if (ve.getFechaExpiracion().before(new Date()) || ve.isVerificado()) {
            return ResponseEntity.badRequest().body("El enlace ha expirado o ya ha sido utilizado");
        }

        return ResponseEntity.ok("Token válido, puedes proceder a actualizar tu contraseña");
    }
}
