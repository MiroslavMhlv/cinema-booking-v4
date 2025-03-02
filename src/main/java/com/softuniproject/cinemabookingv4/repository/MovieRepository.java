package com.softuniproject.cinemabookingv4.repository;

import com.softuniproject.cinemabookingv4.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
}
