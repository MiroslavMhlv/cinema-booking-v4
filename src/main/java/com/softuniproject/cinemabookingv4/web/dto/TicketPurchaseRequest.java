package com.softuniproject.cinemabookingv4.web.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class TicketPurchaseRequest {

    @Min(value = 1, message = "You must buy at least 1 ticket")
    private int quantity;
}
