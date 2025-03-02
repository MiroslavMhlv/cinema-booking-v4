package com.softuniproject.cinemabookingv4.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MovieRequest {

    @NotBlank(message = "Movie title is required")
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Min(value = 10, message = "Duration must be at least 10 minutes")
    private int duration;
}
