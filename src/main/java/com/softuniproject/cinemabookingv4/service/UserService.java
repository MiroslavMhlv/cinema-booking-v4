package com.softuniproject.cinemabookingv4.service;

import com.softuniproject.cinemabookingv4.entity.User;
import com.softuniproject.cinemabookingv4.entity.UserRole;
import com.softuniproject.cinemabookingv4.exception.DomainException;
import com.softuniproject.cinemabookingv4.repository.UserRepository;
import jakarta.annotation.PostConstruct;
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
                .role(UserRole.USER)
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

    @CacheEvict(value = "users", allEntries = true)
    public void switchRole(UUID userId) {
        User user = getById(userId);

        if (user.getRole() == UserRole.USER) {
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }

        userRepository.save(user);
        log.info("User role switched: [%s] is now [%s]".formatted(user.getEmail(), user.getRole()));
    }


    @PostConstruct
    public void initAdminUser() {
        if (userRepository.findByEmail("admin@kinomania.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@kinomania.com")
                    .password("123123")
                    .role(UserRole.ADMIN)
                    .balance(99999999.0)
                    .build();
            userRepository.save(admin);
            log.info("========================================");
            log.info("ADMIN ACCOUNT CREATED!");
            log.info("Email: admin@kinomania.com");
            log.info("Password: 123123");
            log.info("ID: [{}]", admin.getId());
            log.info("========================================");        }
    }
}
