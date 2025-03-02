package com.softuniproject.cinemabookingv4.web.dto;

import com.softuniproject.cinemabookingv4.entity.*;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .balance(user.getBalance())
                .role(user.getRole())
                .build();
    }

    public static CinemaResponse fromCinema(Cinema cinema) {
        return CinemaResponse.builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .location(cinema.getLocation())
                .build();
    }

    public static MovieResponse fromMovie(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .duration(movie.getDuration())
                .build();
    }

    public static ScreeningResponse fromScreening(Screening screening) {
        return ScreeningResponse.builder()
                .id(screening.getId())
                .movieTitle(screening.getMovie().getTitle())
                .cinemaName(screening.getCinema().getName())
                .startTime(screening.getStartTime())
                .price(screening.getPrice())
                .build();
    }

    public static TicketResponse fromTicket(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .movieTitle(ticket.getScreening().getMovie().getTitle())
                .cinemaName(ticket.getScreening().getCinema().getName())
                .purchaseTime(ticket.getPurchaseTime())
                .price(ticket.getScreening().getPrice())
                .build();
    }
}
