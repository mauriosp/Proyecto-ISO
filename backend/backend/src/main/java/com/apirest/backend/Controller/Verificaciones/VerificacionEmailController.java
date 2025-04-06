package com.apirest.backend.Controller.Verificaciones;

import com.apirest.backend.Service.IVerificacionEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/UAO/apirest")
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
}