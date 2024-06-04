package com.bol.mancala.service;

import com.bol.mancala.exception.ResourceNotFoundException;
import com.bol.mancala.model.User;
import com.bol.mancala.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Mono<User> create(final String displayName) {
        final User user = User.builder()
                .displayName(displayName)
                .build();
        return userRepository.save(user);
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUser(final UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.defer(
                        () -> Mono.error(new ResourceNotFoundException("User not found for id : " + id))));
    }

    public void recordVictory(final @NotNull User user) {
        user.setNoOfGames(user.getNoOfGames() + 1);
        user.setNoOfWins(user.getNoOfWins() + 1);

        userRepository.save(user);
    }

    public void recordLoss(final @NotNull User user) {
        user.setNoOfGames(user.getNoOfGames() + 1);
        user.setNoOfLoses(user.getNoOfLoses() + 1);

        userRepository.save(user);
    }

    public void recordDraw(final @NotNull User user) {
        user.setNoOfGames(user.getNoOfGames() + 1);

        userRepository.save(user);
    }

}
