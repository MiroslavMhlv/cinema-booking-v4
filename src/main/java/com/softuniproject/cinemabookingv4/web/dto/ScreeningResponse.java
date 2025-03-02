package com.softuniproject.cinemabookingv4.web.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ScreeningResponse {
    private UUID id;
    private String movieTitle;
    private String cinemaName;
    private LocalDateTime startTime;
    private Double price;
}
