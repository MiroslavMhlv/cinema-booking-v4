package com.softuniproject.cinemabookingv4.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CinemaRequest {

    @NotBlank(message = "Cinema name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;
}
