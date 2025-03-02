package com.softuniproject.cinemabookingv4.web.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TicketResponse {
    private UUID id;
    private String movieTitle;
    private String cinemaName;
    private LocalDateTime purchaseTime;
    private double price;
    private int seatNumber;
}
