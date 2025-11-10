package br.com.joaofxs.client_scheduling_microsservice.core.config;


import br.com.joaofxs.client_scheduling_microsservice.core.exception.InvalidTokenException;
import br.com.joaofxs.client_scheduling_microsservice.core.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAutheticationFilterTests {

    @Mock
    private JwtService jwtService;
    @Mock
    private  UserDetailsService userDetailsService;
    @Mock
    private FilterChain filterChain;

    //Injeta os Mocks acima na instância do filtro que será testado
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @AfterEach
    void tearDown(){
        // Limpa o contexto de segurança após cada teste para evitar interferencia
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve autenticar o usuário quando o token JWT for válido")
    void doFilterInternal_whenValidToken_shouldAuthenticateUser() throws Exception {
        //Arrange
        String token = "valid-token";
        String userEmail = "user@example.com";
        request.addHeader("Authorization", "Bearer " + token);

        UserDetails userDetails = new User(userEmail, "password", new ArrayList<>());

        when(jwtService.getClaimFromToken(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        //act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication, "O contexto de segurança não deveria ser nulo");
        assertEquals(userEmail, authentication.getName(), "O nome do usuário está incorreto");
        verify(filterChain, times(1)).doFilter(request,response);

    }

    @Test
    @DisplayName("Não deve autenticar quando o cabeçalho de autorização estiver ausente")
    void doFilterInternal_whenNoAuthorizationHeader_shouldNotAuthenticate() throws Exception {
        // Arrange (nenhum header é adicionado)

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication, "O contexto de segurança deveria permanecer nulo");
        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService); // Garante que os serviços não foram chamados
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized quando o token for inválido")
    void doFilterInternal_whenInvalidToken_shouldReturnUnauthorized() throws IOException, ServletException {
        //Arrange
        String token = "invalid-token";
        String errorMessage = "Token signature is invalid";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtService.getClaimFromToken(token)).thenThrow(new InvalidTokenException(errorMessage));

        //act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //Assert
        //Verifica se a resposta HTTP foi configurada corretamente
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        //Verifica se o corpo da resposta JSON está correto
        String jsonResponse = response.getContentAsString();
        assertTrue(jsonResponse.contains(errorMessage), "A mensagem de erro não está no corpo da resposta");
        assertTrue(jsonResponse.contains("\"code\":401"), "O código de erro não está no corpo da resposta");
        //Garante que a cadeia de filtros foi interrompida
        verify(filterChain, never()).doFilter(request, response);
        //Garante que o usuario não foi autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication, "O contexto de segurança deveria permanecer nulo");

    }

}
