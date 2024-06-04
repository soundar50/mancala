package com.bol.mancala.service;

import com.bol.mancala.model.Lobby;
import com.bol.mancala.repository.LobbyRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LobbyService {

    @Autowired
    LobbyRepository lobbyRepository;

    public Mono<Lobby> create(Lobby lobby) {
        return lobbyRepository.save(lobby);
    }

    @NotNull
    public Mono<Lobby> getLobby() {
        return lobbyRepository.findAll()
                .take(1)
                .singleOrEmpty();
    }

    public Mono<Void> delete(final @NotNull Lobby lobby) {
        return lobbyRepository.delete(lobby);
    }
}
