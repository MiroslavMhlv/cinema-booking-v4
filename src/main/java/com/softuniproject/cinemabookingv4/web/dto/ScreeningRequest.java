package com.softuniproject.cinemabookingv4.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ScreeningRequest {

    @NotNull(message = "Movie ID is required")
    private UUID movieId;

    @NotBlank(message = "Cinema name or location is required")
    private String cinemaName;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @Min(value = 1, message = "Price must be at least 1")
    private Double price;
}
