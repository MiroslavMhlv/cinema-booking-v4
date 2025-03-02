package com.softuniproject.cinemabookingv4.service;

import com.softuniproject.cinemabookingv4.entity.Screening;
import com.softuniproject.cinemabookingv4.entity.Ticket;
import com.softuniproject.cinemabookingv4.entity.User;
import com.softuniproject.cinemabookingv4.exception.DomainException;
import com.softuniproject.cinemabookingv4.repository.ScreeningRepository;
import com.softuniproject.cinemabookingv4.repository.TicketRepository;
import com.softuniproject.cinemabookingv4.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ScreeningRepository screeningRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         UserRepository userRepository,
                         ScreeningRepository screeningRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.screeningRepository = screeningRepository;
    }

    @CacheEvict(value = "tickets", allEntries = true)
    @Transactional
    public Ticket buyTicket(UUID userId, UUID screeningId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException("User with ID [%s] does not exist.".formatted(userId)));

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new DomainException("Screening with ID [%s] does not exist.".formatted(screeningId)));

        if (user.getBalance() < screening.getPrice()) {
            throw new DomainException("Insufficient balance! Ticket price: %.2f, Available balance: %.2f"
                    .formatted(screening.getPrice(), user.getBalance()));
        }

        user.setBalance(user.getBalance() - screening.getPrice());
        userRepository.save(user);

        Ticket ticket = Ticket.builder()
                .userId(userId)
                .screening(screening)
                .purchaseTime(LocalDateTime.now())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        log.info("User [%s] bought a ticket for screening [%s] on [%s]."
                .formatted(user.getEmail(), screening.getMovie().getTitle(), screening.getStartTime()));

        return savedTicket;
    }

    @Cacheable("tickets")
    public List<Ticket> getUserTickets(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new DomainException("User with ID [%s] does not exist.".formatted(userId));
        }
        return ticketRepository.findByUserId(userId);
    }

    public Ticket getById(UUID ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new DomainException("Ticket with ID [%s] does not exist.".formatted(ticketId)));
    }
}
