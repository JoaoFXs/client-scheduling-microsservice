package br.com.joaofxs.client_scheduling_microsservice.core.controller;


import br.com.joaofxs.client_scheduling_microsservice.core.dto.RequestResetDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.model.ResetToken;
import br.com.joaofxs.client_scheduling_microsservice.core.service.PasswordForgotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/reset-password")
public class PasswordForgotController {

    @Autowired
    private PasswordForgotService service;

    @PostMapping
    public ResponseEntity<?> requestReset(@RequestBody RequestResetDTO requestResetDTO){

        service.processPasswordReset(requestResetDTO.email());

        return ResponseEntity.ok("Se o e-mail estiver cadastrado, você receberá um link para redefinir a senha");
    }

    @GetMapping
    public ResponseEntity<?> validateToken(@RequestParam String token){
        service.validateToken(token);
        return ResponseEntity.ok("O token é válido");
    }

}
