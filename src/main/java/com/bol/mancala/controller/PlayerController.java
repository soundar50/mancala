package com.bol.mancala.controller;

import com.bol.mancala.model.dto.GameDto;
import com.bol.mancala.model.dto.SubscriptionDto;
import com.bol.mancala.model.dto.UserDto;
import com.bol.mancala.service.GamePlayService;
import com.bol.mancala.service.PlayerService;
import com.bol.mancala.util.CopyUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/mancala/v1/players")
@Tag(name = "Player API", description = "API for players")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @Autowired
    GamePlayService gamePlayService;

    @Operation(summary = "Get a player by id")
    @GetMapping(value = "/{playerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserDto> getPlayer(@Parameter(description = "Player ID", required = true) @PathVariable final UUID playerId) {
        return playerService.getPlayer(playerId)
                .map(CopyUtil::toUserDto);
    }

    @Operation(summary = "Subscribe to play")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value = "/{playerId}/subscribe", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SubscriptionDto> subscribe(@Parameter(description = "Player ID", required = true) @PathVariable final UUID playerId) {
        return playerService.subscribe(playerId);
    }

    @Operation(summary = "Get all games by player id")
    @GetMapping(value = "/{playerId}/games", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Flux<GameDto> getGames(@Parameter(description = "Player ID", required = true) @PathVariable final UUID playerId) {
        return gamePlayService.getAllGamesByPlayer(playerId)
                .map(CopyUtil::toGameDto);
    }
}
