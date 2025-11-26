package br.com.joaofxs.client_scheduling_microsservice.admin.model;


import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_opening_time")
public class OpeningTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "opening_hours", nullable = false)
    private LocalDateTime openingHours;

    @Column(name = "closed_hours", nullable = false)
    private LocalDateTime closedHours;

    @Column
    private boolean open;

    //Relacionemnto com a Empresa/Profissional
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

}
