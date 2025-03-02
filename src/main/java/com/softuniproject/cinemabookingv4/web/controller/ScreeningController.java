package com.softuniproject.cinemabookingv4.web.controller;

import com.softuniproject.cinemabookingv4.web.dto.ScreeningRequest;
import com.softuniproject.cinemabookingv4.web.dto.ScreeningResponse;
import com.softuniproject.cinemabookingv4.entity.Screening;
import com.softuniproject.cinemabookingv4.entity.User;
import com.softuniproject.cinemabookingv4.entity.UserRole;
import com.softuniproject.cinemabookingv4.web.dto.DtoMapper;
import com.softuniproject.cinemabookingv4.service.ScreeningService;
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
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;
    private final UserService userService;

    public ScreeningController(ScreeningService screeningService, UserService userService) {
        this.screeningService = screeningService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ScreeningResponse>> getAllScreenings() {
        List<ScreeningResponse> screenings = screeningService.getAllScreenings()
                .stream()
                .map(DtoMapper::fromScreening)
                .collect(Collectors.toList());

        return ResponseEntity.ok(screenings);
    }

    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<List<ScreeningResponse>> getScreeningsByCinema(@PathVariable UUID cinemaId) {
        List<ScreeningResponse> screenings = screeningService.getScreeningsByCinema(cinemaId)
                .stream()
                .map(DtoMapper::fromScreening)
                .collect(Collectors.toList());

        return ResponseEntity.ok(screenings);
    }

    @PostMapping
    public ResponseEntity<ScreeningResponse> addScreening(@RequestParam UUID userId,
                                                          @Valid @RequestBody ScreeningRequest request) {
        User user = userService.getById(userId);

        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).body(null);
        }

        Screening screening = screeningService.addScreening(request.getMovieId(), request.getCinemaName(), request.getStartTime(), request.getPrice());
        return ResponseEntity.ok(DtoMapper.fromScreening(screening));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteScreening(@RequestParam UUID userId, @PathVariable UUID id) {
        User user = userService.getById(userId);

        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).body("Only ADMIN users can delete screenings.");
        }

        screeningService.deleteScreening(id);
        return ResponseEntity.ok("Screening deleted successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ScreeningResponse>> getScreeningsForMovieInCinema(
            @RequestParam String cinemaName,
            @RequestParam String movieTitle) {

        List<ScreeningResponse> screenings = screeningService.getScreeningsForMovieInCinema(cinemaName, movieTitle)
                .stream()
                .map(DtoMapper::fromScreening)
                .collect(Collectors.toList());

        return ResponseEntity.ok(screenings);
    }

}
