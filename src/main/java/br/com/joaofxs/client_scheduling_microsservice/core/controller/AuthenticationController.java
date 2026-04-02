package br.com.joaofxs.client_scheduling_microsservice.core.controller;


import br.com.joaofxs.client_scheduling_microsservice.core.dto.AccessToken;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.AuthRequest;

import br.com.joaofxs.client_scheduling_microsservice.core.dto.SocialLoginRequest;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.UserDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.service.AuthenticationService;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AccessToken> register(@RequestBody UserDTO request) {
        return new ResponseEntity<>(service.register(request, "user"), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<AccessToken> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    /** Verifica se usuario social existe **/
    @GetMapping("/social-login/{jwt}")
    public ResponseEntity<AccessToken> verifyIfUserExist(@PathVariable String jwt){
        return ResponseEntity.ok(service.verifyIfUserExist(jwt));
    }

    /** Cria um usuario social **/
    @PostMapping("/social-login/{request}")
    public ResponseEntity<AccessToken> startSocialLogin(@PathVariable String request, @RequestBody AuthRequest socialLoginRequest){
        service.socialLoginRequest(request, socialLoginRequest);
        return ResponseEntity.noContent().build();
    }
}