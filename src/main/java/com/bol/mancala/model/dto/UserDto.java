package com.bol.mancala.model.dto;

import java.util.UUID;

public record UserDto (UUID id, String displayName, Long noOfGames, Long noOfWins, Long noOfLoses) {
}
