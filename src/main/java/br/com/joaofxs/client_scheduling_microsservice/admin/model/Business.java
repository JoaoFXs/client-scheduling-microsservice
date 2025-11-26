package br.com.joaofxs.client_scheduling_microsservice.admin.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "business_table")
@Builder
@Data
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
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningTime> times = new ArrayList<>();


}
