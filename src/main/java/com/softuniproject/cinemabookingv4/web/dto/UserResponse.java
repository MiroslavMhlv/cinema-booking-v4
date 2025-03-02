package com.softuniproject.cinemabookingv4.web.dto;

import com.softuniproject.cinemabookingv4.entity.UserRole;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private Double balance;
    private UserRole role;
}
