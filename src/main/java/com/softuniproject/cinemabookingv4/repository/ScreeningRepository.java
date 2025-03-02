package com.softuniproject.cinemabookingv4.repository;

import com.softuniproject.cinemabookingv4.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ScreeningRepository extends JpaRepository<Screening, UUID> {
    List<Screening> findByCinemaId(UUID cinemaId);

    List<Screening> findByCinema_NameAndMovie_Title(String cinemaName, String movieTitle);

}
