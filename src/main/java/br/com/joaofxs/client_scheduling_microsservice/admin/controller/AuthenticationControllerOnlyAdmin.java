package br.com.joaofxs.client_scheduling_microsservice.admin.controller;

import br.com.joaofxs.client_scheduling_microsservice.core.dto.AccessToken;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.UserDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AuthenticationControllerOnlyAdmin {

    @Autowired
    private AuthenticationService service;

    @PostMapping("/auth/register")
    public ResponseEntity<AccessToken> registerAdmin(@RequestBody UserDTO request) {
        return new ResponseEntity<>(service.register(request, "admin"), HttpStatus.CREATED);
    }
}
