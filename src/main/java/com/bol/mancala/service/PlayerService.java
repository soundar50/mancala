package com.bol.mancala.service;

import com.bol.mancala.exception.InvalidRequestException;
import com.bol.mancala.model.dto.SubscriptionDto;
import com.bol.mancala.model.User;
import com.bol.mancala.model.Lobby;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    UserService userService;

    @Autowired
    LobbyService lobbyService;

    @Autowired
    GameService gameService;

    public Mono<User> getPlayer(final UUID pid) {
        return userService.getUser(pid);
    }

    public Mono<SubscriptionDto> subscribe(final @Nullable UUID playerId) {
        return getPlayer(playerId)
                .flatMap(user -> lobbyService.getLobby()
                        .flatMap(lobby -> checkLobbyAndStartGame(user, lobby))
                        .switchIfEmpty(Mono.defer(() -> createLobbyAndSubscribe(user))));
    }

    private Mono<SubscriptionDto> createLobbyAndSubscribe(User user) {
        return lobbyService.create(Lobby.builder().subscribedPlayer(user).build())
                .map(lobby -> new SubscriptionDto(true, false, null));
    }

    protected Mono<SubscriptionDto> checkLobbyAndStartGame(User user, Lobby lobby) {
        return Mono.justOrEmpty(lobby.getSubscribedPlayer())
                .filter(signedUser -> !signedUser.equals(user))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new InvalidRequestException("Already subscribed and waiting in the lobby. User : " + user.getId()))))
                .flatMap(__ -> startGame(user, lobby))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.just(new SubscriptionDto(true, false, null))));
    }

    protected Mono<SubscriptionDto> startGame(User playerTwo, Lobby lobby) {
        return gameService.create(lobby.getSubscribedPlayer(), playerTwo)
                .doOnNext(__ -> lobbyService.delete(lobby).subscribe()) // Delete the lobby on successful game creation
                .map(game -> new SubscriptionDto(false, true, game.getId()));
    }
}
