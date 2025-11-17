package br.com.joaofxs.client_scheduling_microsservice.core.service;

import br.com.joaofxs.client_scheduling_microsservice.core.controller.AuthenticationController;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.AccessToken;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.AuthRequest;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.UserDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.exception.UserAlreadyExistException;
import br.com.joaofxs.client_scheduling_microsservice.core.model.User;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    private UserDTO userDTO;
    private AuthRequest authRequest;

    private User userMock;
    private AccessToken accessToken;
    @BeforeEach
    void setUp(){
        userDTO = new UserDTO("testUser",
                "test@example.com",
                "testPassword");
        accessToken = new AccessToken("fake-jwt-token");
        authRequest = new AuthRequest("test@example.com", "testPassword");
    }

    @Test
    @DisplayName("Deve verificar se o usuário já existe")
    void shouldVerifyIfUserAlreadyExists() throws Exception {

        when(userRepository.getByEmail(userDTO.email())).thenReturn(new User());


       UserAlreadyExistException exception =
               Assertions.assertThrows(UserAlreadyExistException.class, () ->  authenticationService.register(userDTO, "user"));

       Assertions.assertEquals(userDTO.email() + " já cadastrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com a role 'user'")
    void shouldRegisteraNewUserWithRoleUser(){
        when(userRepository.getByEmail(userDTO.email())).thenReturn(null);
        when(passwordEncoder.encode(userDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(jwtService.generateToken(any(User.class))).thenReturn(accessToken);

        AccessToken result = authenticationService.register(userDTO, "user");

        assertNotNull(result);
        assertNotNull(result.token());
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com a role 'admin'")
    void shouldRegisteraNewUserWithRoleAdmin(){
        when(userRepository.getByEmail(userDTO.email())).thenReturn(null);
        when(passwordEncoder.encode(userDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(jwtService.generateToken(any(User.class))).thenReturn(accessToken);

        AccessToken result = authenticationService.register(userDTO, "admin");

        assertNotNull(result);
        assertNotNull(result.token());
    }

    @Test
    @DisplayName("Deve autenticar um usuário com credenciais válidas")
    void shouldAuthenticateUserWithValidCredentials() {
        User existingUser = new User();
        existingUser.setPassword("encodedPassword");
        //act
        when(userRepository.getByEmail(authRequest.email())).thenReturn(existingUser);

        when(passwordEncoder.matches(eq(authRequest.password()), eq(existingUser.getPassword()))).thenReturn(true);
        when(jwtService.generateToken(any(User.class))).thenReturn(accessToken);

        AccessToken result = authenticationService.authenticate(authRequest);

        assertNotNull(result);
        assertNotNull(result.token());


    }

    @Test
    @DisplayName("Deve tentar autenticar usuario, e retornar um exceção para usuario nao encontrado")
    public void shouldTryAuthenticateAndReturnAnExceptionForNotFoundUser(){
        when(userRepository.getByEmail(authRequest.email())).thenReturn(null);

        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> authenticationService.authenticate(authRequest));
        Assertions.assertEquals(authRequest.email() + " não encontrado", exception.getMessage());

    }

    @Test
    @DisplayName("Deve retornar null ao tentar autenticar e senhas não derem match")
    void shouldReturnNullToTryAuthenticateAndPasswordsDontMatch(){
        //arrange
        User existingUser = new User();
        existingUser.setPassword("encodedPassword");
        //act
        when(userRepository.getByEmail(authRequest.email())).thenReturn(existingUser);
        when(passwordEncoder.matches(eq(authRequest.password()), eq(existingUser.getPassword()))).thenReturn(false);
        //Assert

        AccessToken result = authenticationService.authenticate(authRequest);
        Assertions.assertNull(result);

    }


}
