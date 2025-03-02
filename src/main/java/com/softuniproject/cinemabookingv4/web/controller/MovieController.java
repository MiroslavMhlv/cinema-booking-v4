package com.softuniproject.cinemabookingv4.web.controller;

import com.softuniproject.cinemabookingv4.web.dto.MovieRequest;
import com.softuniproject.cinemabookingv4.web.dto.MovieResponse;
import com.softuniproject.cinemabookingv4.entity.Movie;
import com.softuniproject.cinemabookingv4.entity.User;
import com.softuniproject.cinemabookingv4.entity.UserRole;
import com.softuniproject.cinemabookingv4.web.dto.DtoMapper;
import com.softuniproject.cinemabookingv4.service.MovieService;
import com.softuniproject.cinemabookingv4.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final UserService userService;

    public MovieController(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    /**
     * Returns all movies
     */
    @GetMapping
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        List<MovieResponse> movies = movieService.getAllMovies()
                .stream()
                .map(DtoMapper::fromMovie)
                .collect(Collectors.toList());

        return ResponseEntity.ok(movies);
    }

    /**
     * Returns a movie by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getById(@PathVariable UUID id) {
        Movie movie = movieService.getById(id);
        return ResponseEntity.ok(DtoMapper.fromMovie(movie));
    }

    /**
     * Adds a new movie (ADMIN only)
     */
    @PostMapping
    public ResponseEntity<MovieResponse> addMovie(@RequestParam UUID userId,
                                                  @Valid @RequestBody MovieRequest request) {
        User user = userService.getById(userId);

        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).body(null);
        }

        Movie movie = movieService.addMovie(request.getTitle(), request.getGenre(), request.getDuration());
        return ResponseEntity.ok(DtoMapper.fromMovie(movie));
    }

    /**
     * Deletes a movie (ADMIN only)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@RequestParam UUID userId, @PathVariable UUID id) {
        User user = userService.getById(userId);

        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).body("Only ADMIN users can delete movies.");
        }

        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie deleted successfully.");
    }
}
