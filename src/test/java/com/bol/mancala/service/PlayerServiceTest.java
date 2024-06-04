package com.bol.mancala.service;

import com.bol.mancala.exception.InvalidRequestException;
import com.bol.mancala.model.Game;
import com.bol.mancala.model.Lobby;
import com.bol.mancala.model.User;
import com.bol.mancala.model.dto.SubscriptionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class PlayerServiceTest {

    @Mock
    UserService userService;

    @Mock
    LobbyService lobbyService;

    @Mock
    GameService gameService;

    @InjectMocks
    PlayerService playerService;

    @Test
    void startGameShouldReturnValidGame() {
        // given
        User user = User.builder().id(UUID.randomUUID()).build();
        Lobby lobby = Lobby.builder().subscribedPlayer(User.builder().build()).build();
        when(lobbyService.delete(any())).thenReturn(Mono.empty());
        when(gameService.create(any(), any()))
                .thenReturn(Mono.just(Game.builder().build()));

        // when
        SubscriptionDto subscriptionDto = playerService.startGame(user, lobby).block();

        // then
        assertThat(subscriptionDto).isNotNull();
        assertThat(subscriptionDto.gameCreated()).isTrue();
        assertThat(subscriptionDto.gameId()).isNotNull();
        assertThat(subscriptionDto.inLobby()).isFalse();
    }

    @Test
    void checkLobbyAndStartGameShouldCreateNewGameIfAnotherUserIsAlreadySubscribed() {
        // given
        User user = User.builder().id(UUID.randomUUID()).build();
        Lobby lobby = Lobby.builder().subscribedPlayer(User.builder().build()).build();
        when(gameService.create(any(), any()))
                .thenReturn(Mono.just(Game.builder().build()));
        when(lobbyService.delete(any())).thenReturn(Mono.empty());

        // when
        SubscriptionDto subscriptionDto = playerService.checkLobbyAndStartGame(user, lobby).block();

        // then
        assertThat(subscriptionDto).isNotNull();
        assertThat(subscriptionDto.gameCreated()).isTrue();
        assertThat(subscriptionDto.gameId()).isNotNull();
        assertThat(subscriptionDto.inLobby()).isFalse();
    }

    @Test
    void checkLobbyAndStartGameShouldReturnFalseIfUserIsAlreadySubscribed() {
        // given
        User user = User.builder().id(UUID.randomUUID()).build();
        Lobby lobby = Lobby.builder().subscribedPlayer(user).build();

        // when
        StepVerifier.create(playerService.checkLobbyAndStartGame(user, lobby))
                .expectError(InvalidRequestException.class);
    }
}