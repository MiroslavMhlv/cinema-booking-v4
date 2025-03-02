package com.softuniproject.cinemabookingv4.service;

import com.softuniproject.cinemabookingv4.entity.Cinema;
import com.softuniproject.cinemabookingv4.entity.Movie;
import com.softuniproject.cinemabookingv4.entity.Screening;
import com.softuniproject.cinemabookingv4.exception.DomainException;
import com.softuniproject.cinemabookingv4.repository.CinemaRepository;
import com.softuniproject.cinemabookingv4.repository.MovieRepository;
import com.softuniproject.cinemabookingv4.repository.ScreeningRepository;
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
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ScreeningService(ScreeningRepository screeningRepository,
                            CinemaRepository cinemaRepository,
                            MovieRepository movieRepository) {
        this.screeningRepository = screeningRepository;
        this.cinemaRepository = cinemaRepository;
        this.movieRepository = movieRepository;
    }

    @Cacheable("screenings")
    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    public Screening getById(UUID screeningId) {
        return screeningRepository.findById(screeningId)
                .orElseThrow(() -> new DomainException("Screening with ID [%s] does not exist.".formatted(screeningId)));
    }

    public List<Screening> getScreeningsByCinema(UUID cinemaId) {
        if (!cinemaRepository.existsById(cinemaId)) {
            throw new DomainException("Cinema with ID [%s] does not exist.".formatted(cinemaId));
        }
        return screeningRepository.findByCinemaId(cinemaId);
    }

    @CacheEvict(value = "screenings", allEntries = true)
    @Transactional
    public Screening addScreening(UUID movieId, String cinemaName, LocalDateTime startTime, Double price) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new DomainException("Movie with ID [%s] not found.".formatted(movieId)));

        Cinema cinema = cinemaRepository.findByName(cinemaName)
                .orElseThrow(() -> new DomainException("Cinema with name [%s] not found.".formatted(cinemaName)));

        Screening screening = Screening.builder()
                .movie(movie)
                .cinema(cinema)
                .startTime(startTime)
                .price(price)
                .build();

        Screening savedScreening = screeningRepository.save(screening);
        log.info("Added new screening: Movie [%s] at Cinema [%s] on [%s]".formatted(
                savedScreening.getMovie().getTitle(),
                savedScreening.getCinema().getName(),
                savedScreening.getStartTime()
        ));
        return savedScreening;
    }

    @CacheEvict(value = "screenings", allEntries = true)
    @Transactional
    public void deleteScreening(UUID screeningId) {
        if (!screeningRepository.existsById(screeningId)) {
            throw new DomainException("Screening with ID [%s] does not exist.".formatted(screeningId));
        }
        screeningRepository.deleteById(screeningId);
        log.info("Screening with ID [%s] was deleted.".formatted(screeningId));
    }
}
