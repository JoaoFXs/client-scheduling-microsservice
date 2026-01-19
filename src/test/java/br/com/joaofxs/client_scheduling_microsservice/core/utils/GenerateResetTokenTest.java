package br.com.joaofxs.client_scheduling_microsservice.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GenerateResetTokenTest {

    private GenerateResetToken generateResetToken;

    @BeforeEach
    void setUp() {
        generateResetToken = new GenerateResetToken();
    }

    @Test
    @DisplayName("Deve gerar um token não nulo e não vazio")
    void shouldGenerateNotNullAndNotEmptyToken() {
        String token = generateResetToken.makeResetToken();
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Deve gerar tokens únicos em chamadas consecutivas")
    void shouldGenerateUniqueTokens() {
        Set<String> tokens = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            tokens.add(generateResetToken.makeResetToken());
        }
        // Se todos forem únicos, o tamanho do set deve ser igual ao número de iterações
        assertEquals(100, tokens.size());
    }

    @Test
    @DisplayName("Deve gerar um token que seja uma string Base64 válida URL-safe")
    void shouldGenerateValidBase64UrlSafeToken() {
        String token = generateResetToken.makeResetToken();
        
        // Tenta decodificar para verificar se é Base64 válido
        assertDoesNotThrow(() -> Base64.getUrlDecoder().decode(token));
        
        // Verifica se não contém caracteres inválidos para URL (como + ou / se não for URL safe, mas o encoder usado é URL safe)
        // O encoder usado é getUrlEncoder().withoutPadding()
        assertFalse(token.contains("+"));
        assertFalse(token.contains("/"));
        assertFalse(token.contains("=")); // withoutPadding
    }
    
    @Test
    @DisplayName("Deve gerar um token com o tamanho esperado (aprox 43 caracteres para 32 bytes em Base64 sem padding)")
    void shouldGenerateTokenWithExpectedLength() {
        String token = generateResetToken.makeResetToken();
        // 32 bytes * 8 bits = 256 bits. 
        // Base64 usa 6 bits por caractere. 256 / 6 = 42.66 -> 43 caracteres.
        assertEquals(43, token.length());
    }
}
