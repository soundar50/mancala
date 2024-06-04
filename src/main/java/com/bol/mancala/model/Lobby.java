package com.bol.mancala.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "lobbies")
public class Lobby {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Version
    @Builder.Default
    private Long version = null;

    @NotNull
    private User subscribedPlayer;

    @CreatedDate
    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    @LastModifiedDate
    @Builder.Default
    private LocalDateTime lastUpdated = LocalDateTime.now();

}
