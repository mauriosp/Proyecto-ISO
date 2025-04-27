package com.apirest.backend.Controller.Verificaciones;

import com.apirest.backend.Service.IVerificacionEmailService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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
            // Convertir userId de String a ObjectId
            ObjectId objectId = new ObjectId(userId);

            // Enviar correo de verificación
            verificacionEmailService.enviarCorreoVerificacion(objectId, email);
            return ResponseEntity.ok("Correo de verificación enviado con éxito.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ID de usuario inválido.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al enviar correo: " + e.getMessage());
        }
    }
}