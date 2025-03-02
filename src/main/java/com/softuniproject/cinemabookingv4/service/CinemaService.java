package com.softuniproject.cinemabookingv4.service;

import com.softuniproject.cinemabookingv4.entity.Cinema;
import com.softuniproject.cinemabookingv4.exception.DomainException;
import com.softuniproject.cinemabookingv4.repository.CinemaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    @Autowired
    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Cacheable("cinemas")
    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    public Cinema getById(UUID cinemaId) {
        return cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new DomainException("Cinema with ID [%s] does not exist.".formatted(cinemaId)));
    }

    @CacheEvict(value = "cinemas", allEntries = true)
    @Transactional
    public Cinema addCinema(String name, String location) {
        if (cinemaRepository.existsByName(name)) {
            throw new DomainException("A cinema with name [%s] already exists.".formatted(name));
        }

        Cinema cinema = Cinema.builder()
                .name(name)
                .location(location)
                .build();

        Cinema savedCinema = cinemaRepository.save(cinema);
        log.info("Added a new Cinema: %s (%s)".formatted(savedCinema.getName(), savedCinema.getLocation()));
        return savedCinema;
    }

    @CacheEvict(value = "cinemas", allEntries = true)
    @Transactional
    public void deleteCinema(UUID cinemaId) {
        if (!cinemaRepository.existsById(cinemaId)) {
            throw new DomainException("Cinema with ID [%s] does not exists.".formatted(cinemaId));
        }
        cinemaRepository.deleteById(cinemaId);
        log.info("Cinema with ID [%s] was deleted successful.".formatted(cinemaId));
    }
}
