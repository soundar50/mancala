package com.bol.mancala.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "games")
@Builder
public class Game {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Builder.Default
    private Board board = Board.builder().build();

    @NotNull
    private Map<PlayerRole, User> players;

    @Builder.Default
    private Map<PlayerRole, Integer> pointsScored = Map.of(PlayerRole.ONE, 0, PlayerRole.TWO, 0);

    @Builder.Default
    private boolean ended = false;

    @Version
    @Builder.Default
    private Long version = null;

    @CreatedDate
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    @Builder.Default
    private LocalDateTime updateDate = LocalDateTime.now();

    public User getPlayer(final @NotNull PlayerRole playerRole) {
        return getPlayers().get(playerRole);
    }

}
