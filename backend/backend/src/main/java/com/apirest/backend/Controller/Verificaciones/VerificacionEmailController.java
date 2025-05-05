package com.apirest.backend.Controller.Verificaciones;

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
    public ResponseEntity<String> verificarCuenta(@RequestParam("token") String token) {
        try {
            boolean verificado = verificacionEmailService.verificarToken(token);
            if (verificado) {
                return ResponseEntity.ok("Cuenta verificada correctamente. Ahora puedes iniciar sesión.");
            } else {
                return ResponseEntity.badRequest().body("Token inválido o expirado. Por favor solicita un nuevo enlace de verificación.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al verificar la cuenta: " + e.getMessage());
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