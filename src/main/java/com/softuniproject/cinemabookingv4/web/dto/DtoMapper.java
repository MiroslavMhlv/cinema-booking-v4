package com.softuniproject.cinemabookingv4.web.dto;

import com.softuniproject.cinemabookingv4.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .balance(user.getBalance())
                .role(user.getRole())
                .build();
    }
}
