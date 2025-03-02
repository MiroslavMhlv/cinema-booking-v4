package com.softuniproject.cinemabookingv4.web.controller;

import com.softuniproject.cinemabookingv4.web.dto.DtoMapper;
import com.softuniproject.cinemabookingv4.web.dto.UserRegisterRequest;
import com.softuniproject.cinemabookingv4.web.dto.UserResponse;
import com.softuniproject.cinemabookingv4.entity.User;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        User user = userService.register(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(DtoMapper.fromUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UUID> login(@RequestParam String email, @RequestParam String password) {
        UUID userId = userService.login(email, password);
        return ResponseEntity.ok(userId);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(DtoMapper::fromUser)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserResponse> getProfile(@PathVariable UUID id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(DtoMapper.fromUser(user));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<String> switchUserRole(@PathVariable UUID id) {
        userService.switchRole(id);
        return ResponseEntity.ok("User role switched successfully.");
    }
}
