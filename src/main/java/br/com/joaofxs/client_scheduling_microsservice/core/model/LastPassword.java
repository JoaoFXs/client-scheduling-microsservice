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
public class LastPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @ManyToOne
    private User user;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    @PrePersist
    protected void createdAt(){
        createdAt = LocalDateTime.now();
        expiredAt = LocalDateTime.now().plusMonths(5).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

}


