package com.softuniproject.cinemabookingv4.web.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class MovieResponse {
    private UUID id;
    private String title;
    private String genre;
    private int duration;
}
