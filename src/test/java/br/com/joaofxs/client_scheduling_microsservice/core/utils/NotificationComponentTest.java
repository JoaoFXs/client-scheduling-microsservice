package br.com.joaofxs.client_scheduling_microsservice.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationComponentTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationComponent notificationComponent;

    @BeforeEach
    void setUp() {
        // Injeta o valor da propriedade ${app.host} manualmente, já que @Value não funciona com @InjectMocks puro
        ReflectionTestUtils.setField(notificationComponent, "hostApp", "http://localhost:8080");
    }

    @Test
    @DisplayName("Deve enviar email com sucesso")
    void shouldSendEmailSuccessfully() {
        // Arrange
        String email = "test@example.com";
        String token = "valid-token";
        String expectedUrl = "http://localhost:8080/reset-password?token=" + token;
        String expectedBody = "Click the link below to reset your password.\n" + expectedUrl;

        // Act
        notificationComponent.sendEmail(email, token);

        // Assert
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals(email, sentMessage.getTo()[0]);
        assertEquals("Password Reset", sentMessage.getSubject());
        assertEquals(expectedBody, sentMessage.getText());
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando ocorrer erro ao enviar email")
    void shouldThrowRuntimeExceptionWhenMailSendingFails() {
        // Arrange
        String email = "test@example.com";
        String token = "valid-token";
        doThrow(new MailSendException("Failed to send email")).when(mailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> notificationComponent.sendEmail(email, token));
        assertTrue(exception.getCause() instanceof MailException);
    }
}
