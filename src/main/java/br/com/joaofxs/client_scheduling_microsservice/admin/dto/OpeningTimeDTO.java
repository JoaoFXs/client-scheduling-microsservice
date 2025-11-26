package br.com.joaofxs.client_scheduling_microsservice.admin.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public record OpeningTimeDTO(
        DayOfWeek dayOfWeek,
        LocalDateTime openingHours,
        LocalDateTime closedHours,
        boolean open

){};
