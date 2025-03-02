package com.softuniproject.cinemabookingv4.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private Double balance = 100.0;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
}