package com.softuniproject.cinemabookingv4.controller;

import com.softuniproject.cinemabookingv4.web.dto.CinemaRequest;
import com.softuniproject.cinemabookingv4.web.dto.CinemaResponse;
import com.softuniproject.cinemabookingv4.entity.Cinema;
import com.softuniproject.cinemabookingv4.entity.User;
import com.softuniproject.cinemabookingv4.entity.UserRole;
import com.softuniproject.cinemabookingv4.web.dto.DtoMapper;
import com.softuniproject.cinemabookingv4.service.CinemaService;
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
@RequestMapping("/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;
    private final UserService userService;

    public CinemaController(CinemaService cinemaService, UserService userService) {
        this.cinemaService = cinemaService;
        this.userService = userService;
    }

    /**
     * Returns all cinemas
     */
    @GetMapping
    public ResponseEntity<List<CinemaResponse>> getAllCinemas() {
        List<CinemaResponse> cinemas = cinemaService.getAllCinemas()
                .stream()
                .map(DtoMapper::fromCinema)
                .collect(Collectors.toList());

        return ResponseEntity.ok(cinemas);
    }

    /**
     * Returns a cinema by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CinemaResponse> getById(@PathVariable UUID id) {
        Cinema cinema = cinemaService.getById(id);
        return ResponseEntity.ok(DtoMapper.fromCinema(cinema));
    }

    /**
     * Adds a new cinema (ADMIN only)
     */
    @PostMapping
    public ResponseEntity<CinemaResponse> addCinema(@RequestParam UUID userId,
                                                    @Valid @RequestBody CinemaRequest request) {
        User user = userService.getById(userId);

        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).body(null);
        }

        Cinema cinema = cinemaService.addCinema(request.getName(), request.getLocation());
        return ResponseEntity.ok(DtoMapper.fromCinema(cinema));
    }

    /**
     * Deletes a cinema (ADMIN only)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCinema(@RequestParam UUID userId, @PathVariable UUID id) {
        User user = userService.getById(userId);

        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).body("Only ADMIN users can delete cinemas.");
        }

        cinemaService.deleteCinema(id);
        return ResponseEntity.ok("Cinema deleted successfully.");
    }
}
