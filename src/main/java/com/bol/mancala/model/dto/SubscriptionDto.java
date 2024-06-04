package com.bol.mancala.model.dto;

import java.util.UUID;

public record SubscriptionDto(boolean inLobby, boolean gameCreated, UUID gameId) {
}
