package br.com.joaofxs.client_scheduling_microsservice.core.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SecretKeyGeneratorTest {

    // Instância real da classe que queremos testar
    private SecretKeyGenerator secretKeyGenerator;

    @BeforeEach
    void setUp() {
        // Criamos uma nova instância antes de cada teste para garantir o isolamento
        secretKeyGenerator = new SecretKeyGenerator();
    }

    @Test
    @DisplayName("Deve gerar uma SecretKey válida na primeira chamada")
    void getKey_shouldGenerateValidKey_onFirstCall() {
        // Act
        SecretKey key = secretKeyGenerator.getKey();

        // Assert
        assertNotNull(key, "A chave gerada não deveria ser nula.");
        assertEquals("HmacSHA256", key.getAlgorithm(), "O algoritmo da chave deveria ser HmacSHA256.");
    }

    @Test
    @DisplayName("Deve retornar a mesma instância da chave em chamadas subsequentes")
    void getKey_shouldReturnSameKeyInstance_onSubsequentCalls() {
        // Act
        SecretKey firstKey = secretKeyGenerator.getKey();
        SecretKey key = secretKeyGenerator.getKey();

        // Assert
        assertNotNull(firstKey, "A primeira chave não deveria ser nula.");
        assertNotNull(key, "A segunda chave não deveria ser nula.");
        Assertions.assertSame(firstKey, key, "Deveria retornar a mesma instância da chave em chamadas repetidas.");
    }
}
