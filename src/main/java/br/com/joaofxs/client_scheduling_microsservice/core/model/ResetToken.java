package br.com.joaofxs.client_scheduling_microsservice.core.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expiresAt;

    @ManyToOne
    private User user;

    /**
     * Verifica se o token esta expirado,
     * caso estiver será feito uma trativa
     * para o usuairo tentar novamente.
     * @return
     */
    public boolean isExpired(){
        return expiresAt.isBefore(LocalDateTime.now());
    }


}
