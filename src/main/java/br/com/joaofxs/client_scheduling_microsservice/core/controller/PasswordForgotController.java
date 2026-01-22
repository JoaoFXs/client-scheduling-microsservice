package br.com.joaofxs.client_scheduling_microsservice.core.controller;


import br.com.joaofxs.client_scheduling_microsservice.core.dto.RequestResetDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.ResetPasswordRequestDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.SimpleMessage;
import br.com.joaofxs.client_scheduling_microsservice.core.model.ResetToken;
import br.com.joaofxs.client_scheduling_microsservice.core.service.PasswordForgotService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        return ResponseEntity.ok(new SimpleMessage("Se o e-mail estiver cadastrado, você receberá um link para redefinir a senha"));
    }

    @GetMapping
    public ResponseEntity<?> validateToken(@RequestParam String token){
        service.validateToken(token);
        return ResponseEntity.ok("O token é válido");
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO){
        service.resetPassword(resetPasswordRequestDTO.token(), resetPasswordRequestDTO.newPassword());
        return ResponseEntity.ok("Senha redefinida com sucesso");
    }
}
