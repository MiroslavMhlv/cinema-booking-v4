package com.softuniproject.cinemabookingv4.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private Double price;

    @OneToMany(mappedBy = "screening", fetch = FetchType.EAGER)
    private List<Ticket> tickets = new ArrayList<>();
}