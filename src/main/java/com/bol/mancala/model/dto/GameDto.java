package com.bol.mancala.model.dto;

import com.bol.mancala.model.Board;
import com.bol.mancala.model.PlayerRole;

import java.util.Map;
import java.util.UUID;

public record GameDto(UUID id, Board board, Map<PlayerRole, UserDto> players, Map<PlayerRole, Integer> pointsScored, boolean ended) {
}
