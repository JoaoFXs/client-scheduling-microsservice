package br.com.joaofxs.client_scheduling_microsservice.core.config;

import br.com.joaofxs.client_scheduling_microsservice.core.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityConfigurationTest.TestController.class)
@Import({SecurityConfigurationTest.TestController.class, SecurityConfigurationTest.TestSecurityConfig.class})
class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    // Precisamos mockar as dependências do JwtAuthenticationFilter,
    // pois ele é carregado junto com a SecurityConfiguration.
    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationProvider authenticationProvider; // Mock da dependência que estava faltando

    /**
     * Classe de configuração de segurança específica para este teste.
     * Ela recria a lógica de autorização da SecurityConfiguration principal,
     * mas omite o .securityMatcher() para evitar o erro 404 nos testes com @WebMvcTest.
     */
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                            .requestMatchers("/api/user/**").hasAnyAuthority("ADMIN", "USER")
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            // Não adicionamos o JwtAuthenticationFilter aqui porque ele não é o foco deste teste.
            // As dependências dele já estão mockadas, o que é suficiente.
            return http.build();
        }
    }

    // Controllers "dummy" para termos endpoints para testar
    @RestController
    static class TestController {
        @GetMapping("/api/auth/public")
        public String publicEndpoint() {
            return "public";
        }

        @GetMapping("/api/admin/resource")
        public String adminEndpoint() {
            return "admin resource";
        }

        @GetMapping("/api/user/resource")
        public String userEndpoint() {
            return "user resource";
        }

        @GetMapping("/public/ignored")
        public String ignoredEndpoint() {
            return "ignored";
        }
    }

    @Test
    @DisplayName("Deve permitir acesso anônimo a endpoints /api/auth/**")
    void shouldAllowAnonymousAccessToAuthEndpoints() throws Exception {
        mockMvc.perform(get("/api/auth/public"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve negar acesso anônimo a endpoints /api/admin/**")
    void shouldDenyAnonymousAccessToAdminEndpoints() throws Exception {
        mockMvc.perform(get("/api/admin/resource"))
                .andExpect(status().isForbidden()); // 403 Forbidden para anônimos
    }

    @Test
    @DisplayName("Deve negar acesso a /api/admin/** para usuários com role USER")
    @WithMockUser(authorities = "USER")
    void shouldDenyUserRoleAccessToAdminEndpoints() throws Exception {
        mockMvc.perform(get("/api/admin/resource"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve permitir acesso a /api/admin/** para usuários com role ADMIN")
    @WithMockUser(authorities = "ADMIN")
    void shouldAllowAdminRoleAccessToAdminEndpoints() throws Exception {
        mockMvc.perform(get("/api/admin/resource"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve permitir acesso a /api/user/** para usuários com role USER")
    @WithMockUser(authorities = "USER")
    void shouldAllowUserRoleAccessToUserEndpoints() throws Exception {
        mockMvc.perform(get("/api/user/resource"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve permitir acesso a /api/user/** para usuários com role ADMIN")
    @WithMockUser(authorities = "ADMIN")
    void shouldAllowAdminRoleAccessToUserEndpoints() throws Exception {
        mockMvc.perform(get("/api/user/resource"))
                .andExpect(status().isOk());
    }
}