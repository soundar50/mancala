package com.bol.mancala.service;

import com.bol.mancala.exception.IllegalMoveException;
import com.bol.mancala.exception.InvalidRequestException;
import com.bol.mancala.model.Game;
import com.bol.mancala.model.PlayerRole;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.function.Predicate.not;

@Service
public class GamePlayService {

    private static final Logger logger = LoggerFactory.getLogger(GamePlayService.class);

    @Autowired
    GameService gameService;

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    public Flux<Game> getAllGamesByPlayer(final UUID playerId) {
        return userService.getUser(playerId)
                .flatMapMany(player ->
                        gameService.getAll()
                                .filter(game -> game.getPlayers().containsValue(player)))
                .sort(Comparator.comparing(Game::getCreatedDate));
    }

    public Mono<Game> makeMove(final UUID id, final @NotNull PlayerRole playerRole, final Integer position) {
        return validateGameAndGet(id)
                .flatMap(game -> validateMove(game, playerRole, position))
                .doOnNext(game -> boardService.executeMove(game.getBoard(), position))
                .doOnNext(this::checkAndFinalizeGame)
                .flatMap(game -> gameService.save(game));
    }

    private Mono<Game> validateGameAndGet(UUID id) {
        return gameService.getById(id)
                .filter(not(Game::isEnded))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new InvalidRequestException("Invalid request. Game has already ended!"))));
    }

    private Mono<Game> validateMove(Game game, PlayerRole playerRole, Integer position) {
        return Mono.just(game)
                .doOnNext(__ -> logger.info("Validating move for player {} from position {}", playerRole, position))
                .filter(__ -> boardService.isLegalMove(game.getBoard(), playerRole, position))
                .switchIfEmpty(Mono.error(new IllegalMoveException(format("Illegal move by player %s from position %s", playerRole, position))));
    }

    private void checkAndFinalizeGame(Game game) {
        logger.info("Checking if game {} has ended", game.getId());
        if (boardService.isGameEnded(game.getBoard())) {
            boardService.collectStonesInBigPit(game.getBoard());
            PlayerRole winner = boardService.getWinner(game.getBoard()).orElse(null);
            updatePointsAndEndGame(game, winner);
        }
    }

    private void updatePointsAndEndGame(final @NotNull Game game, final PlayerRole winner) {
        logger.info("Game has ended. Updating points and ending game");
        updatePointsScored(game, winner);
        game.setEnded(true);

        if (winner == null) {
            userService.recordDraw(game.getPlayer(PlayerRole.ONE));
            userService.recordDraw(game.getPlayer(PlayerRole.TWO));
        } else {
            userService.recordVictory(game.getPlayer(winner));
            userService.recordLoss(game.getPlayer(winner.opponent()));
        }
    }

    public Mono<Game> playAgain(final UUID id) {
        return gameService.getById(id)
                .doOnNext(game -> logger.info("Request to play again for game {}", game.getId()))
                .filter(Game::isEnded)
                .flatMap(this::createNewMatchWithOldData)
                .switchIfEmpty(Mono.error(new InvalidRequestException("Match has not ended")));
    }

    public Mono<Game> createNewMatchWithOldData(@NotNull final Game finishedGame) {
        logger.info("Creating new match with old data. Game: {}", finishedGame.getId());
        final Game game = Game.builder()
                .players(Map.copyOf(finishedGame.getPlayers()))
                .pointsScored(Map.copyOf(finishedGame.getPointsScored()))
                .build();
        return gameService.save(game);
    }

    public void updatePointsScored(final Game game, final PlayerRole winner) {
        logger.info("Updating points scored for game. Winner: {}", winner);
        final Map<PlayerRole, Integer> existingPoints = game.getPointsScored();
        Map<PlayerRole, Integer> updatedPoints;

        if (winner != null) {
            final PlayerRole looser = winner.opponent();
            updatedPoints = Map.of(winner, existingPoints.get(winner) + 2, looser, existingPoints.get(looser));
        } else {
            updatedPoints = Map.of(PlayerRole.ONE, existingPoints.get(PlayerRole.ONE) + 1, PlayerRole.TWO, existingPoints.get(PlayerRole.TWO) + 1);
        }

        game.setPointsScored(updatedPoints);
    }

}
