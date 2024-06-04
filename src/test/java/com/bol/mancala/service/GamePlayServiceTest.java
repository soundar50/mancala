package com.bol.mancala.service;

import com.bol.mancala.model.Game;
import com.bol.mancala.model.PlayerRole;
import com.bol.mancala.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GamePlayServiceTest {

    @Mock
    UserService userService;

    @Mock
    GameService gameService;

    @Mock
    BoardService boardService;

    @InjectMocks
    GamePlayService gamePlayService;

    @Test
    void getAllGamesByPlayerShouldReturnValidGames() {
        // given
        User user1 = User.builder().id(UUID.randomUUID()).build();
        Map<PlayerRole, User> players = Map.of(
                PlayerRole.ONE, user1,
                PlayerRole.TWO, User.builder().build()
        );
        when(userService.getUser(user1.getId())).thenReturn(Mono.just(user1));
        when(gameService.getAll()).thenReturn(Flux.just(
                Game.builder().players(players).build(),
                Game.builder().players(players).build()
        ));

        // when
        List<Game> result = gamePlayService.getAllGamesByPlayer(user1.getId()).collectList().block();

        // then
        assertThat(result).hasSize(2);

    }

    @Test
    void makeMoveShouldReturnValidGame() {
        // given
        UUID gameId = UUID.randomUUID();
        User user1 = User.builder().id(UUID.randomUUID()).build();
        Map<PlayerRole, User> players = Map.of(
                PlayerRole.ONE, user1,
                PlayerRole.TWO, User.builder().build()
        );
        Game game = Game.builder().id(gameId).players(players).build();
        when(gameService.getById(gameId)).thenReturn(Mono.just(game));
        when(gameService.save(game)).thenReturn(Mono.just(game));
        when(boardService.isLegalMove(game.getBoard(), PlayerRole.ONE, 0)).thenReturn(true);

        // when
        Game result = gamePlayService.makeMove(gameId, PlayerRole.ONE, 0).block();

        // then
        assertThat(result).isNotNull();
    }

    @Test
    void updatePointsScoredShouldUpdatePoints() {
        // given
        User user1 = User.builder().id(UUID.randomUUID()).build();
        User user2 = User.builder().id(UUID.randomUUID()).build();
        Map<PlayerRole, User> players = Map.of(
                PlayerRole.ONE, user1,
                PlayerRole.TWO, user2
        );
        Game game = Game.builder().players(players).build();
        PlayerRole winner = PlayerRole.ONE;

        // when
        gamePlayService.updatePointsScored(game, winner);

        // then
        assertThat(game.getPointsScored().get(PlayerRole.ONE)).isEqualTo(2);
        assertThat(game.getPointsScored().get(PlayerRole.TWO)).isEqualTo(0);
    }
}