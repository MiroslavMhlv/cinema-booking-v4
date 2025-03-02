package com.softuniproject.cinemabookingv4.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TicketRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Screening ID is required")
    private UUID screeningId;
}
