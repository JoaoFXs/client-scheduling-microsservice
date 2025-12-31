package br.com.joaofxs.client_scheduling_microsservice.core.controller;


import br.com.joaofxs.client_scheduling_microsservice.core.dto.RequestResetDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.service.PasswordForgotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/forgot-password")
public class PasswordForgotController {

    @Autowired
    private PasswordForgotService service;

    @PostMapping
    public ResponseEntity<?> requestReset(@RequestBody RequestResetDTO requestResetDTO){

        service.processPasswordReset(requestResetDTO.email());

        return ResponseEntity.ok("If the email is registered, you'll get a reset link");
    }

}
