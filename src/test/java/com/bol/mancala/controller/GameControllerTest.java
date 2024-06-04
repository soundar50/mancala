package com.bol.mancala.controller;

import com.bol.mancala.model.Game;
import com.bol.mancala.model.PlayerRole;
import com.bol.mancala.model.dto.GameDto;
import com.bol.mancala.security.SecurityConfig;
import com.bol.mancala.service.GamePlayService;
import com.bol.mancala.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;

@WebFluxTest(GameController.class)
@Import(SecurityConfig.class)
public class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private WebTestClient webTestClientUnauthorized;

    @MockBean
    private GameService gameService;

    @MockBean
    private GamePlayService gamePlayService;

    @BeforeEach
    public void setUp() {
        this.webTestClient = webTestClient.mutateWith(mockUser("user").password("password").roles("USER"));
    }

    @Test
    public void testGetAllGamesAuthenticated() {
        // Given
        Game game1 = Game.builder().players(new HashMap<>()).build();
        Game game2 = Game.builder().players(new HashMap<>()).build();

        GameDto gameDto1 = new GameDto(game1.getId(), game1.getBoard(), new HashMap<>(), game1.getPointsScored(), game1.isEnded());
        GameDto gameDto2 = new GameDto(game2.getId(), game2.getBoard(), new HashMap<>(), game2.getPointsScored(), game2.isEnded());

        when(gameService.getAll()).thenReturn(Flux.just(game1, game2));

        // When and Then
        webTestClient.get()
                .uri("/mancala/v1/games")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GameDto.class)
                .hasSize(2)
                .contains(gameDto1, gameDto2);
    }

    @Test
    public void testGetAllGamesUnauthorized() {
        webTestClientUnauthorized.get()
                .uri("/mancala/v1/games")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    public void testGetGameById() {
        // Given
        UUID gameId = UUID.randomUUID();
        Game game = Game.builder().id(gameId).players(new HashMap<>()).build();
        GameDto gameDto = new GameDto(game.getId(), game.getBoard(), new HashMap<>(), game.getPointsScored(), game.isEnded());

        when(gameService.getById(gameId)).thenReturn(Mono.just(game));

        // When and Then
        webTestClient.get()
                .uri("/mancala/v1/games/{id}", gameId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameDto.class)
                .isEqualTo(gameDto);
    }

    @Test
    public void testMakeMove() {
        // Given
        UUID gameId = UUID.randomUUID();
        Game game = Game.builder().id(gameId).players(new HashMap<>()).build();
        GameDto gameDto = new GameDto(game.getId(), game.getBoard(), new HashMap<>(), game.getPointsScored(), game.isEnded());

        when(gamePlayService.makeMove(gameId, PlayerRole.ONE, 1)).thenReturn(Mono.just(game));

        // When and Then
        webTestClient.post()
                .uri("/mancala/v1/games/{id}/make-move?playerRole=ONE&position=1", gameId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameDto.class)
                .isEqualTo(gameDto);
    }

    @Test
    public void testPlayAgain() {
        // Given
        UUID gameId = UUID.randomUUID();
        Game game = Game.builder().id(gameId).players(new HashMap<>()).build();
        GameDto gameDto = new GameDto(game.getId(), game.getBoard(), new HashMap<>(), game.getPointsScored(), game.isEnded());

        when(gamePlayService.playAgain(gameId)).thenReturn(Mono.just(game));

        // When and Then
        webTestClient.post()
                .uri("/mancala/v1/games/{gameId}/play-again", gameId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameDto.class)
                .isEqualTo(gameDto);
    }

}