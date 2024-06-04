package com.bol.mancala.service;

import com.bol.mancala.exception.ResourceNotFoundException;
import com.bol.mancala.model.Game;
import com.bol.mancala.model.PlayerRole;
import com.bol.mancala.model.User;
import com.bol.mancala.repository.GameRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    public Flux<Game> getAll() {
        return gameRepository.findAll()
                .sort(Comparator.comparing(Game::getCreatedDate));
    }

    public Mono<Game> getById(final UUID id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResourceNotFoundException("Game not found for id : " + id))));
    }

    public Mono<Game> create(@NotNull final User playerOne, @NotNull final User playerTwo) {
        return Mono.just(Map.of(PlayerRole.ONE, playerOne, PlayerRole.TWO, playerTwo))
                .map(playersByRole -> Game.builder().players(playersByRole).build())
                .flatMap(this::save);
    }

    public Mono<Game> save(@NotNull final Game game) {
        return gameRepository.save(game);
    }
}
