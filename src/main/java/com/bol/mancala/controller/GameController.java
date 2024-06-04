package com.bol.mancala.controller;

import com.bol.mancala.model.PlayerRole;
import com.bol.mancala.model.dto.GameDto;
import com.bol.mancala.service.GamePlayService;
import com.bol.mancala.service.GameService;
import com.bol.mancala.util.CopyUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/mancala/v1/games")
@Tag(name = "Game API", description = "API for games")
public class GameController {

    @Autowired
    GameService gameService;
    @Autowired
    GamePlayService gamePlayService;

    @Operation(summary = "Get all games")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<GameDto> getAll() {
        return gameService.getAll()
                .map(CopyUtil::toGameDto);
    }

    @Operation(summary = "Get game by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<GameDto> get(@Parameter(description = "Game ID", required = true) @PathVariable final UUID id) {
        return gameService.getById(id)
                .map(CopyUtil::toGameDto);
    }

    @Operation(summary = "Make a move")
    @PostMapping(value = "/{id}/make-move", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<GameDto> makeMove(@Parameter(description = "Game ID", required = true) @PathVariable final UUID id,
                                  @Parameter(description = "Player role", required = true) @NotNull @RequestParam final PlayerRole playerRole,
                                  @Parameter(description = "Start position", required = true) @RequestParam final int position) {
        return gamePlayService.makeMove(id, playerRole, position)
                .map(CopyUtil::toGameDto);
    }

    @Operation(summary = "Play again")
    @PostMapping(value = "/{gameId}/play-again", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<GameDto> playAgain(@Parameter(description = "Game ID", required = true) @PathVariable final UUID gameId) {
        return gamePlayService.playAgain(gameId)
                .map(CopyUtil::toGameDto);
    }
}
