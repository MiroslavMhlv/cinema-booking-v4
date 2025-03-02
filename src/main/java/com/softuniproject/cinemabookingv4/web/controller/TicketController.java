package com.softuniproject.cinemabookingv4.web.controller;

import com.softuniproject.cinemabookingv4.web.dto.TicketResponse;
import com.softuniproject.cinemabookingv4.entity.Ticket;
import com.softuniproject.cinemabookingv4.service.TicketService;
import com.softuniproject.cinemabookingv4.service.UserService;
import com.softuniproject.cinemabookingv4.web.dto.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @PostMapping("/buy")
    public ResponseEntity<TicketResponse> buyTicket(@RequestParam UUID userId,
                                                    @RequestParam UUID screeningId,
                                                    @RequestParam int seatNumber) {
        Ticket ticket = ticketService.buyTicket(userId, screeningId, seatNumber);
        return ResponseEntity.ok(DtoMapper.fromTicket(ticket));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponse>> getUserTickets(@PathVariable UUID userId) {
        List<TicketResponse> tickets = ticketService.getUserTickets(userId)
                .stream()
                .map(DtoMapper::fromTicket)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tickets);
    }
}
