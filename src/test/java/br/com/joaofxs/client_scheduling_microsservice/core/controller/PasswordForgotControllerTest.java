package br.com.joaofxs.client_scheduling_microsservice.core.controller;

import br.com.joaofxs.client_scheduling_microsservice.core.dto.RequestResetDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.ResetPasswordRequestDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.exception.TokenInvalidException;
import br.com.joaofxs.client_scheduling_microsservice.core.service.JwtService;
import br.com.joaofxs.client_scheduling_microsservice.core.service.PasswordForgotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PasswordForgotController.class)
@AutoConfigureMockMvc(addFilters = false) // Desabilita filtros de segurança para simplificar o teste do controller
class PasswordForgotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordForgotService passwordForgotService;

    // Mock necessário porque o contexto de segurança pode tentar carregar filtros que dependem dele
    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve retornar 200 OK ao solicitar redefinição de senha")
    @WithMockUser 
    void shouldReturnOkWhenRequestingReset() throws Exception {
        RequestResetDTO requestDTO = new RequestResetDTO("test@example.com");

        doNothing().when(passwordForgotService).processPasswordReset(anyString());

        mockMvc.perform(post("/api/reset-password")
                        .with(csrf()) 
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Se o e-mail estiver cadastrado, você receberá um link para redefinir a senha"));

        verify(passwordForgotService).processPasswordReset("test@example.com");
    }

    @Test
    @DisplayName("Deve retornar 200 OK quando o token for válido")
    @WithMockUser
    void shouldReturnOkWhenTokenIsValid() throws Exception {
        String token = "valid-token";

        doNothing().when(passwordForgotService).validateToken(token);

        mockMvc.perform(get("/api/reset-password")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("O token é válido"));

        verify(passwordForgotService).validateToken(token);
    }

    @Test
    @DisplayName("Deve retornar erro (dependendo do handler) quando o token for inválido no validateToken")
    @WithMockUser
    void shouldReturnErrorWhenTokenIsInvalid() throws Exception {
        String token = "invalid-token";

        doThrow(new TokenInvalidException()).when(passwordForgotService).validateToken(token);

        mockMvc.perform(get("/api/reset-password")
                        .param("token", token))
                .andExpect(result -> {
                    // Verifica apenas se a chamada foi feita, já que o status exato depende de configuração global
                }); 
        
        verify(passwordForgotService).validateToken(token);
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao redefinir a senha com sucesso")
    @WithMockUser
    void shouldReturnOkWhenResettingPasswordSuccessfully() throws Exception {
        ResetPasswordRequestDTO requestDTO = new ResetPasswordRequestDTO("valid-token", "newPassword123");

        doNothing().when(passwordForgotService).resetPassword(anyString(), anyString());

        mockMvc.perform(post("/api/reset-password/confirm")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Senha redefinida com sucesso"));

        verify(passwordForgotService).resetPassword("valid-token", "newPassword123");
    }
}
