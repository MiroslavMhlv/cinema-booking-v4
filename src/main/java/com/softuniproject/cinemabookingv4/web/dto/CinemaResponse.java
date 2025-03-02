package com.softuniproject.cinemabookingv4.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CinemaResponse {
    private UUID id;
    private String name;
    private String location;
}
