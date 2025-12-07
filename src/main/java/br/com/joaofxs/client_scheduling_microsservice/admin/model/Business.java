package br.com.joaofxs.client_scheduling_microsservice.admin.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "business_table")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Business {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Size(min = 100, max = 160)
    private String description;

    @NotNull
    private String phone;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String website;


    @NotNull
    private String address;

    @NotNull
    @Size(max = 8)
    private Long cep;

    // cascade = ALL: Se deletar a empresa, deleta os horários junto
    // orphanRemoval = true: Se tirar um horário da lista, deleta do banco
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OpeningTime> times = new ArrayList<>();



    @PrePersist
    public void prePersist() {
        if(this.times == null || this.times.isEmpty()){
            initializeOpeningTimes();
        }
    }
    
    // Método auxiliar para inicializar os horários padrão (um para cada dia da semana)
    private void initializeOpeningTimes() {
        for(DayOfWeek dayOfWeek: DayOfWeek.values()){
            // Cria uma NOVA instância de OpeningTime a cada iteração do loop
            OpeningTime newTime = OpeningTime.builder()
                    .dayOfWeek(dayOfWeek)
                    .open(false) // Por padrão, o estabelecimento está fechado
                    .openingHours(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)) // Define um valor padrão não nulo
                    .closedHours(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT))  // Define um valor padrão não nulo
                    .business(this) // Associa este horário a este negócio
                    .build();
            this.times.add(newTime);
        }
    }


}
