package br.com.joaofxs.client_scheduling_microsservice.admin.controller;

import br.com.joaofxs.client_scheduling_microsservice.core.dto.AccessToken;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.AuthRequest;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.UserDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.service.AuthenticationService;
import br.com.joaofxs.client_scheduling_microsservice.core.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = AuthenticationControllerOnlyAdmin.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
@Slf4j
public class AuthenticationControllerOnlyadminTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private UserDTO userDTO;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService; // Mock para a dependência do filtro de segurança

    @MockBean
    private UserDetailsService userDetailsService; // Mock para a dependência do filtro de segurança

    private AuthRequest authRequest;

    private AccessToken accessToken;


    @BeforeEach
    void setUp(){
        //Assert
        userDTO = new UserDTO("testUser","test@example.com", "testPassword");
        authRequest = new AuthRequest(userDTO.email(), userDTO.password());
        accessToken = new AccessToken("fake-jwt-token");
    }

    @Test
    @DisplayName("Deve registrar um novo usuário e retornar status 201 created com o token de acesso")
    void register_whenValidUser_shouldReturnCreatedWithAccessToken() throws Exception {
        //Act
        //Mockito: Quando utilizar um any UserDTO no metodo register e uma string admin, então retornará um accessToken
        when(authenticationService.register(any(UserDTO.class), eq("admin"))).thenReturn(accessToken);

        //Assert
        mockMvc.perform(
                post("/api/admin/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value(accessToken.token()));

    }

}
