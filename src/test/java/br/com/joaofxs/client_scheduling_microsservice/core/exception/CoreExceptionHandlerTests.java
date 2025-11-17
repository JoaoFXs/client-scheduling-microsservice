package br.com.joaofxs.client_scheduling_microsservice.core.exception;

import br.com.joaofxs.client_scheduling_microsservice.core.dto.exception.ResponseException;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CoreExceptionHandlerTests {


    private static CoreExceptionHandler coreExceptionHandler;

    @BeforeAll
    static void onInitTests(){
        coreExceptionHandler = new CoreExceptionHandler();
    }

    @Test
    @DisplayName("Teste para tratamento de exceção para usuário não encontrado")
    void testHandleUserNotFoundException() {
        // Arrange
         UsernameNotFoundException ex = new UsernameNotFoundException("Usuário não encontrado");

        //act
        ResponseEntity<ResponseException> response = coreExceptionHandler.handleUserNotFoundException(ex);
        ResponseException responseBody = response.getBody();


        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getStatusCode());
        assertNotNull(response.getHeaders());
        Assertions.assertEquals("Usuário não encontrado", responseBody.message());
    }

    @Test
    @DisplayName("Teste para tratamento de exceção para usuário já existentecaso o usuário ja exista")
    void testHandleUserAlreadyExists() {
        // Arrange
        UserAlreadyExistException ex = new UserAlreadyExistException("Usuário já existe");
        //act

        ResponseEntity<?> response = coreExceptionHandler.handleUserAlreadyExists(ex);
        ResponseException responseBody = (ResponseException) response.getBody();

        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getStatusCode());
        assertNotNull(response.getHeaders());
        Assertions.assertEquals("Usuário já existe", responseBody.message());
    }

    @Test
    @DisplayName("Teste para tratamento de exceção para token inválido")
    void testHandleInvalidTokenException() {
        // Arrange
        InvalidTokenException ex = new InvalidTokenException("Token inválido");

        //act
        ResponseEntity<?> response = coreExceptionHandler.handleInvalidTokenException(ex);
        ResponseException responseBody = (ResponseException) response.getBody();
        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getStatusCode());
        assertNotNull(response.getHeaders());
        Assertions.assertEquals("Token inválido", responseBody.message());
    }
}
