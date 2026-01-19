package br.com.joaofxs.client_scheduling_microsservice.core.controller;

import br.com.joaofxs.client_scheduling_microsservice.core.dto.AccessToken;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.AuthRequest;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.UserDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.service.AuthenticationService;
import br.com.joaofxs.client_scheduling_microsservice.core.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
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
        controllers = AuthenticationController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class} // Desabilita a segurança para este teste
)
@Slf4j
class AuthenticationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService; // Mock para a dependência do filtro de segurança

    @MockBean
    private UserDetailsService userDetailsService; // Mock para a dependência do filtro de segurança

    private UserDTO user;
    private AuthRequest authRequest;
    private AccessToken accessToken;


    @BeforeAll
    static void setBeforeAll(){
        log.info("==============================================");
        log.info("AUTHENTICATION CONTROLLER - INICIANDO TESTES");
        log.info("==============================================");
    }
    @BeforeEach
    void setUp(TestInfo testInfo){
        log.info("==============================================");
        log.info("Iniciando Teste: {}", testInfo.getDisplayName());
        user = new UserDTO("testUser","test@example.com", "testPassword", "12345678900", "1100000000");
        log.info("- Usuário para teste: {} \n", user);
        authRequest = new AuthRequest(user.email(), user.password());
        accessToken = new AccessToken("fake-jwt-token");
    }

    @Test
    @DisplayName("Deve registrar um novo usuário e retornar status 201 created com o token de acesso")
    void register_whenValidUser_shouldReturnCreatedWithAccessToken() throws Exception {
        //Mockito: Quando utilizar um any UserDTO no metodo register e uma string user, então retornará um accessToken
        when(authenticationService.register(any(UserDTO.class), eq("user"))).thenReturn(accessToken);


        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value(accessToken.token()));
    }

    @Test
    @DisplayName("Deve autenticar um usuário com credenciais válidas e retornar status 200 OK com o token de acesso")
    void login_whenValidCredentials_shouldReturnOkWithAccessToken() throws Exception {
        // Arrange
        when(authenticationService.authenticate(any(AuthRequest.class))).thenReturn(accessToken);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(accessToken.token()));
    }

    @Test
    @DisplayName("Deve retornar status 200 OK com corpo vazio ao tentar logar com senha inválida")
    void login_whenInvalidPassword_shouldReturnOkWithEmptyBody() throws Exception {
        // Arrange
        // Simula o comportamento atual do serviço, que retorna null para senhas inválidas
        when(authenticationService.authenticate(any(AuthRequest.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").doesNotExist());
    }

}
