package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Board {

    @Builder.Default
    private int turnCount = 1;

    @Builder.Default
    private PlayerRole currentPlayer = PlayerRole.ONE;

    @Builder.Default
    private int[] pits = {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};

}
