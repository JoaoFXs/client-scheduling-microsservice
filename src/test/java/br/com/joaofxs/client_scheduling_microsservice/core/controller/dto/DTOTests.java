package br.com.joaofxs.client_scheduling_microsservice.core.controller.dto;

import br.com.joaofxs.client_scheduling_microsservice.core.dto.AccessToken;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.AuthRequest;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.AuthResponse;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.UserDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Slf4j
public class DTOTests {

    @BeforeAll
    static void setBeforeAll(){
        log.info("==============================================");
        log.info("DTOTests - INICIANDO TESTES");
        log.info("==============================================");
    }
    @Test
    @DisplayName("DTO 1 - UserDTO")
    public void userDTOTest() {
        EqualsVerifier.forClass(UserDTO.class).verify();
    }

    @Test
    @DisplayName("DTO 2 - ResponseException")
    public void responseExceptionTest(){
        EqualsVerifier.forClass(ResponseException.class).verify();
    }

    @Test
    @DisplayName("DTO 3 - AuthRequest")
    public void authRequestTest(){
        EqualsVerifier.forClass(AuthRequest.class).verify();
    }
    @Test
    @DisplayName("DTO 4 - AuthResponse")
    public void authResponseTest(){
        EqualsVerifier.forClass(AuthResponse.class).verify();
    }

    @Test
    @DisplayName("DTO 5 - AccessToken")
    public void accessTokenTest() {
        EqualsVerifier.forClass(AccessToken.class).verify();
    }


}
