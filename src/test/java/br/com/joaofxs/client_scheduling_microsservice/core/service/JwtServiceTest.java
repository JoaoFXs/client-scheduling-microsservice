package br.com.joaofxs.client_scheduling_microsservice.core.service;

import br.com.joaofxs.client_scheduling_microsservice.core.dto.AccessToken;
import br.com.joaofxs.client_scheduling_microsservice.core.exception.InvalidTokenException;
import br.com.joaofxs.client_scheduling_microsservice.core.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {



    @Mock
    private SecretKeyGenerator keyGenerator;

    @InjectMocks
    private JwtService jwtService;

    private User testUser;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        // Initialize a fixed secret key for testing
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        when(keyGenerator.getKey()).thenReturn(secretKey);

        testUser = User.builder()
                .email("test@example.com")
                .username("testUser")
                .build();
    }

    @Test
    @DisplayName("Should generate a valid JWT token")
    void generateToken_shouldReturnValidAccessToken() {
        AccessToken accessToken = jwtService.generateToken(testUser);

        assertNotNull(accessToken);
        assertNotNull(accessToken.token());

        // Verify the token by parsing it
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken.token());

        assertNotNull(claims);
        assertEquals(testUser.getEmail(), claims.getPayload().getSubject());
        assertEquals(testUser.getUsername(), claims.getPayload().get("name"));
        assertNotNull(claims.getPayload().getExpiration());
    }

    @Test
    @DisplayName("Should extract email from a valid token")
    void getClaimFromToken_shouldReturnEmail_forValidToken() {
        AccessToken accessToken = jwtService.generateToken(testUser);
        String extractedEmail = jwtService.getClaimFromToken(accessToken.token());

        assertEquals(testUser.getEmail(), extractedEmail);
    }

    @Test
    @DisplayName("Should throw InvalidTokenException for an expired token")
    void getClaimFromToken_shouldThrowInvalidTokenException_forExpiredToken() {
        // Generate a token that expires immediately
        String expiredToken = Jwts.builder()
                .signWith(secretKey)
                .subject(testUser.getEmail())
                .expiration(Date.from(Instant.now().minusSeconds(1))) // Expired 1 second ago
                .compact();

        InvalidTokenException exception = assertThrows(InvalidTokenException.class,
                () -> jwtService.getClaimFromToken(expiredToken)
        );
        assertNotNull(exception);

    }


    @Test
    @DisplayName("Should throw InvalidTokenException for a malformed token")
    void getClaimFromToken_shouldThrowInvalidTokenException_forMalformedToken() {
        String malformedToken = "invalid.token.string";
        assertThrows(InvalidTokenException.class, () -> jwtService.getClaimFromToken(malformedToken));
    }

    @Test
    @DisplayName("Should throw InvalidTokenException for an unsupported token")
    void getClaimFromToken_shouldThrowInvalidTokenException_forUnsupportedToken() {
        // A token signed with a different algorithm or structure might be considered unsupported
        String unsupportedToken = Jwts.builder()
                .subject(testUser.getEmail())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour from now
                .compact(); // No signature, which might lead to UnsupportedJwtException depending on parser config

        assertThrows(InvalidTokenException.class, () -> jwtService.getClaimFromToken(unsupportedToken));
    }

    @Test
    @DisplayName("Should throw InvalidTokenException for a token with invalid signature")
    void getClaimFromToken_shouldThrowInvalidTokenException_forInvalidSignature() {
        SecretKey wrongKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Different key
        String tokenWithWrongSignature = Jwts.builder()
                .signWith(wrongKey)
                .subject(testUser.getEmail())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .compact();

        assertThrows(InvalidTokenException.class, () -> jwtService.getClaimFromToken(tokenWithWrongSignature));
    }

    @Test
    @DisplayName("Should throw InvalidTokenException for an empty token string")
    void getClaimFromToken_shouldThrowInvalidTokenException_forEmptyToken() {
        String emptyToken = "";
        assertThrows(InvalidTokenException.class, () -> jwtService.getClaimFromToken(emptyToken));
    }

    @Test
    @DisplayName("Should throw InvalidTokenException for a null token string")
    void getClaimFromToken_shouldThrowInvalidTokenException_forNullToken() {
        String nullToken = null;
        assertThrows(InvalidTokenException.class, () -> jwtService.getClaimFromToken(nullToken));
    }



}
