package com.bol.mancala.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @NotBlank
    private String displayName;

    @Builder.Default
    private Long noOfGames = 0L;

    @Builder.Default
    private Long noOfWins = 0L;

    @Builder.Default
    private Long noOfLoses = 0L;

    @CreatedDate
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    @Builder.Default
    private LocalDateTime updatedDate = LocalDateTime.now();

}
