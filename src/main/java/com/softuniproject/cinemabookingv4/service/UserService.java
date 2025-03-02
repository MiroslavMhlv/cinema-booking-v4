package com.softuniproject.cinemabookingv4.service;

import com.softuniproject.cinemabookingv4.entity.User;
import com.softuniproject.cinemabookingv4.exception.DomainException;
import com.softuniproject.cinemabookingv4.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public User register(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DomainException("Email [%s] already exist.".formatted(email));
        }

        User user = userRepository.save(initializeUser(email, password));

        log.info("A new user with email [%s] and id [%s] was created.".formatted(user.getEmail(), user.getId()));

        return user;
    }

    public UUID login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get().getId();
        }

        throw new DomainException("Wrong email or password!");
    }

    private User initializeUser(String email, String password) {
        return User.builder()
                .email(email)
                .password(password) // todo: password encoder
                .balance(100.0)
                .build();
    }

    public Double getBalance(UUID userId) {
        return getById(userId).getBalance();
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(id)));
    }

    @Cacheable("users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
