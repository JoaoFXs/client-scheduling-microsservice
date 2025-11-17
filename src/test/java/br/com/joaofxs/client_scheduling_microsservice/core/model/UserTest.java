package br.com.joaofxs.client_scheduling_microsservice.core.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("onCreate deve inicializar publicId e createdAt quando forem nulos")
    void onCreate_shouldInitializeFields_whenTheyAreNull() {
        // Arrange
        User user = new User();
        assertNull(user.getPublicId(), "PublicId deve ser nulo antes de onCreate");
        assertNull(user.getCreatedAt(), "CreatedAt deve ser nulo antes de onCreate");

        // Act
        user.onCreate();

        // Assert
        assertNotNull(user.getPublicId(), "PublicId não deve ser nulo após onCreate");
        assertNotNull(user.getCreatedAt(), "CreatedAt não deve ser nulo após onCreate");
    }

    @Test
    @DisplayName("onCreate não deve sobrescrever um publicId existente")
    void onCreate_shouldNotOverwrite_existingPublicId() {
        // Arrange
        UUID existingPublicId = UUID.randomUUID();
        User user = User.builder()
                .publicId(existingPublicId)
                .build();

        // Act
        user.onCreate();

        // Assert
        assertEquals(existingPublicId, user.getPublicId(), "O publicId existente não deveria ter sido alterado");
    }

    @Test
    @DisplayName("getAuthorities deve converter roles corretamente para GrantedAuthority")
    void getAuthorities_shouldConvertRolesToGrantedAuthorities() {
        // Arrange
        User user = User.builder()
                .roles(Set.of("USER", "ADMIN"))
                .build();

        // Act
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(2, authorities.size(), "Deveria haver duas authorities");
        assertTrue(authorities.contains(new SimpleGrantedAuthority("USER")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    @DisplayName("getAuthorities deve retornar uma coleção vazia para um conjunto de roles vazio")
    void getAuthorities_shouldReturnEmptyCollection_forEmptyRoles() {
        // Arrange
        User user = User.builder()
                .roles(Collections.emptySet())
                .build();

        // Act
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty(), "A coleção de authorities deveria estar vazia");
    }

    @Test
    @DisplayName("getAuthorities deve lançar NullPointerException se o conjunto de roles for nulo")
    void getAuthorities_shouldThrowNullPointerException_forNullRoles() {
        // Arrange
        User user = new User(); // O campo 'roles' estará nulo

        // Act & Assert
        assertThrows(NullPointerException.class, user::getAuthorities,
                "Deveria lançar NullPointerException quando o conjunto de roles é nulo");
    }

    @Test
    @DisplayName("Métodos de UserDetails devem retornar true por padrão")
    void userDetailsMethods_shouldReturnTrueByDefault() {
        // Arrange
        User user = new User();

        // Act & Assert
        assertTrue(user.isAccountNonExpired(), "isAccountNonExpired deveria ser true");
        assertTrue(user.isAccountNonLocked(), "isAccountNonLocked deveria ser true");
        assertTrue(user.isCredentialsNonExpired(), "isCredentialsNonExpired deveria ser true");
        assertTrue(user.isEnabled(), "isEnabled deveria ser true");
    }


}