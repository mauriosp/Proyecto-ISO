package com.apirest.backend.Controller.Verificaciones;

import com.apirest.backend.Service.IVerificacionEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/UAO/apirest/VerificacionEmail")
@RequiredArgsConstructor
public class VerificacionEmailController {

    private final IVerificacionEmailService verificacionEmailService;

    @GetMapping("/verificar")
    public ResponseEntity<String> verificarCuenta(@RequestParam("token") String token) {
        boolean verificado = verificacionEmailService.verificarToken(token);
        if (verificado) {
            return ResponseEntity.ok("Cuenta verificada correctamente.");
        } else {
            return ResponseEntity.badRequest().body("Token inválido o expirado.");
        }
    }   

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarVerificacion(
            @RequestParam("userId") String userId, 
            @RequestParam("email") String email) {
        try {
            verificacionEmailService.enviarCorreoVerificacion(userId, email);
            return ResponseEntity.ok("Correo de verificación enviado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al enviar correo: " + e.getMessage());
        }
    }
}