package com.softuniproject.cinemabookingv4.service;

import com.softuniproject.cinemabookingv4.entity.Movie;
import com.softuniproject.cinemabookingv4.exception.DomainException;
import com.softuniproject.cinemabookingv4.repository.MovieRepository;
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
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Cacheable("movies")
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getById(UUID movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new DomainException("Movie with ID [%s] does not exist.".formatted(movieId)));
    }

    @CacheEvict(value = "movies", allEntries = true)
    @Transactional
    public Movie addMovie(String title, String genre, int duration) {
        if (movieRepository.existsByTitle(title)) {
            throw new DomainException("Movie with title [%s] already exists.".formatted(title));
        }

        Movie movie = Movie.builder()
                .title(title)
                .genre(genre)
                .duration(duration)
                .build();

        Movie savedMovie = movieRepository.save(movie);
        log.info("Added new movie: %s (%s, %d min)".formatted(savedMovie.getTitle(), savedMovie.getGenre(), savedMovie.getDuration()));
        return savedMovie;
    }

    @CacheEvict(value = "movies", allEntries = true)
    @Transactional
    public void deleteMovie(UUID movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new DomainException("Movie with ID [%s] does not exist.".formatted(movieId));
        }
        movieRepository.deleteById(movieId);
        log.info("Movie with ID [%s] was deleted.".formatted(movieId));
    }
}
