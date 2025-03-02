package com.softuniproject.cinemabookingv4.repository;

import com.softuniproject.cinemabookingv4.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, UUID> {
    Optional<Cinema> findByName(String name);

    boolean existsByName(String name);
}
