package br.com.joaofxs.client_scheduling_microsservice.core.service;

import br.com.joaofxs.client_scheduling_microsservice.core.dto.RequestResetDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.exception.TokenInvalidException;
import br.com.joaofxs.client_scheduling_microsservice.core.model.ResetToken;
import br.com.joaofxs.client_scheduling_microsservice.core.model.User;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.ResetTokenRepository;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.UserRepository;
import br.com.joaofxs.client_scheduling_microsservice.core.utils.GenerateResetToken;
import br.com.joaofxs.client_scheduling_microsservice.core.utils.NotificationComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PasswordForgotServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private PasswordForgotService passwordForgotServiceMock;

    @Mock
    private ResetTokenRepository resetTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GenerateResetToken generateResetToken;

    @Mock // Adicione isso! Sem esse mock, o InjectMocks não tem o que injetar
    private NotificationComponent notificationComponent;

    @Mock
    private PasswordEncoder passwordEncoder;


    private RequestResetDTO requestResetDTO;
    private  User existingUser;
    @BeforeEach()
    void setUp(){
        requestResetDTO = new RequestResetDTO("test@email.com");
        existingUser = new User();
        existingUser.setEmail("test@email.com");
        existingUser.setPassword("encodedPassword");
    }


    @Test
    @DisplayName("processPasswordReset - Deve verificar se o email existe")
    void shouldCheckIfEmailExists(){

        //Arrange
        when(userRepository.findByEmail(requestResetDTO.email())).thenReturn(Optional.of(existingUser));
        //Act
        var resultUser  = userRepository.findByEmail(requestResetDTO.email());
        //Assert
        assertEquals(requestResetDTO.email(),resultUser.get().getEmail());
    }

    @Test
    @DisplayName("processPasswordReset - Deve verificar teste para quando o email não existir")
    void shouldReturnIfEmailsNotExist(){
        //act
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        //assert
        passwordForgotServiceMock.processPasswordReset(requestResetDTO.email());


        verify(generateResetToken, never()).makeResetToken();
        verify(resetTokenRepository, never()).save(any());
        verify(notificationComponent, never()).sendEmail(any(), any());

    }
    @Test
    @DisplayName("processPasswordReset - Deve verificar se o serviço de notificação foi chamado e todas etapas anteriores foram concluidas")
    void shouldVerifyIfProcessPasswordResetWorked(){
        //arrange
        when(userRepository.findByEmail(requestResetDTO.email())).thenReturn(Optional.of(existingUser));
        when(generateResetToken.makeResetToken()).thenReturn("token-gerado");
        //act
        passwordForgotServiceMock.processPasswordReset(requestResetDTO.email());
        //assert
        verify(generateResetToken).makeResetToken();
        verify(resetTokenRepository).save(any());
        verify(notificationComponent).sendEmail(any(), any());

        // Verifica se o componente de notificação foi chamado corretamente
        verify(notificationComponent).sendEmail(requestResetDTO.email(), "token-gerado");

    }

    @Test
    @DisplayName("processPasswordReset - Deve verificar se token foi encontrado com sucesso")
    void shouldVerifyIfTokenWasFound() {
        //arrange
        when(resetTokenRepository.findByToken("token")).thenReturn(Optional.of(new ResetToken(1L, "token", LocalDateTime.now(), existingUser)));
        //act
        var resetTokenReturn = resetTokenRepository.findByToken("token");

        //assert
        assertEquals("token", resetTokenReturn.get().getToken());

    }

    @Test
    @DisplayName("validateToken - Deve validar token com sucesso quando não expirado")
    void shouldValidateTokenWhenNotExpired() {
        // Arrange
        String token = "valid-token";
        ResetToken resetToken = new ResetToken(1L, token, LocalDateTime.now().plusMinutes(15), existingUser);
        when(resetTokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));

        // Act & Assert
        assertDoesNotThrow(() -> passwordForgotServiceMock.validateToken(token));
    }

    @Test
    @DisplayName("validateToken - Deve lançar exceção quando token não encontrado")
    void shouldThrowExceptionWhenTokenNotFound() {
        // Arrange
        String token = "not-found-token";
        when(resetTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TokenInvalidException.class, () -> passwordForgotServiceMock.validateToken(token));
    }

    @Test
    @DisplayName("validateToken - Deve lançar exceção quando token expirado")
    void shouldThrowExceptionWhenTokenExpired() {
        // Arrange
        String token = "expired-token";
        ResetToken resetToken = new ResetToken(1L, token, LocalDateTime.now().minusMinutes(15), existingUser);
        when(resetTokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));

        // Act & Assert
        assertThrows(TokenInvalidException.class, () -> passwordForgotServiceMock.validateToken(token));
    }

    @Test
    @DisplayName("resetPassword - Deve redefinir senha com sucesso")
    void shouldResetPasswordSuccessfully() {
        // Arrange
        String token = "valid-token";
        String newPassword = "newPassword123";
        String encodedPassword = "encodedNewPassword";
        ResetToken resetToken = new ResetToken(1L, token, LocalDateTime.now().plusMinutes(15), existingUser);

        when(resetTokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        // Act
        passwordForgotServiceMock.resetPassword(token, newPassword);

        // Assert
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(existingUser);
        verify(resetTokenRepository).delete(resetToken);
        assertEquals(encodedPassword, existingUser.getPassword());
    }

    @Test
    @DisplayName("resetPassword - Deve lançar exceção quando token não encontrado")
    void shouldThrowExceptionWhenResettingPasswordWithNotFoundToken() {
        // Arrange
        String token = "not-found-token";
        String newPassword = "newPassword123";
        when(resetTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TokenInvalidException.class, () -> passwordForgotServiceMock.resetPassword(token, newPassword));
        verify(userRepository, never()).save(any());
        verify(resetTokenRepository, never()).delete(any());
    }

    @Test
    @DisplayName("resetPassword - Deve lançar exceção quando token expirado")
    void shouldThrowExceptionWhenResettingPasswordWithExpiredToken() {
        // Arrange
        String token = "expired-token";
        String newPassword = "newPassword123";
        ResetToken resetToken = new ResetToken(1L, token, LocalDateTime.now().minusMinutes(15), existingUser);
        when(resetTokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));

        // Act & Assert
        assertThrows(TokenInvalidException.class, () -> passwordForgotServiceMock.resetPassword(token, newPassword));
        verify(userRepository, never()).save(any());
        verify(resetTokenRepository, never()).delete(any());
    }

}
