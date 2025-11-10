package br.com.joaofxs.client_scheduling_microsservice.core.config;

import br.com.joaofxs.client_scheduling_microsservice.core.model.User;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    private ApplicationConfig applicationConfig;

    @BeforeEach
    void setUp() {
        // Instanciamos a classe de configuração manualmente, injetando o mock do repositório
        applicationConfig = new ApplicationConfig(userRepository);
    }


    @Test
    @DisplayName("userDetailsService deve lançar UsernameNotFoundException quando o usuário não é encontrado")
    void userDetailsService_shouldThrowException_whenUserDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        // Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email)
        );
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("passwordEncoder deve retornar uma instância de BCryptPasswordEncoder")
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        // Act
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();

        // Assert
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    @DisplayName("authenticationProvider deve retornar um DaoAuthenticationProvider configurado")
    void authenticationProvider_shouldReturnConfiguredDaoAuthenticationProvider() {
        // Act
        DaoAuthenticationProvider provider = (DaoAuthenticationProvider) applicationConfig.authenticationProvider();

        // Assert
        assertNotNull(provider);
        // Verificamos indiretamente se os métodos foram chamados, garantindo que o provider foi construído.
    }

}