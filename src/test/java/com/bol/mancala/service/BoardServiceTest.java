package com.bol.mancala.service;

import com.bol.mancala.model.Board;
import com.bol.mancala.model.PlayerRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BoardServiceTest {

    BoardService boardService = new BoardService();

    @Test
    void isLegalMoveShouldReturnTrueForValidParams() {
        // given
        Board board = new Board();
        PlayerRole playerRole = PlayerRole.ONE;
        int position = 0;

        // when
        boolean result = boardService.isLegalMove(board, playerRole, position);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void isLegalMoveShouldReturnFalseForInvalidParams() {
        // given
        Board board = new Board();
        PlayerRole playerRole = PlayerRole.TWO;
        int position = 0;

        // when
        boolean result = boardService.isLegalMove(board, playerRole, position);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void executeMoveShouldChangeTurn() {
        // given
        Board board = new Board();
        int position = 3;

        // when
        boardService.executeMove(board, position);

        // then
        assertThat(board.getCurrentPlayer()).isEqualTo(PlayerRole.TWO);
    }

    @Test
    void executeMoveShouldSow() {
        // given
        Board board = new Board();
        int position = 0;

        // when
        boardService.executeMove(board, position);

        // then
        assertThat(board.getPits()).isEqualTo(new int[]{0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0});
    }

    @Test
    void executeMoveShouldCapture() {
        // given
        Board board = new Board();
        int position = 4;
        board.setPits(new int[]{0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0});

        // when
        boardService.executeMove(board, position);

        // then
        assertThat(board.getPits()).isEqualTo(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2});
    }

    @Test
    void executeMoveShouldNotCapture() {
        // given
        Board board = new Board();
        int position = 5;
        board.setPits(new int[]{0, 0, 0, 0, 0, 1, 2, 6, 6, 6, 6, 6, 6, 0});

        // when
        boardService.executeMove(board, position);

        // then
        assertThat(board.getPits()).isEqualTo(new int[]{0, 0, 0, 0, 0, 0, 3, 6, 6, 6, 6, 6, 6, 0});
    }

    @Test
    void sowShouldSowWithSpare() {
        // given
        Board board = new Board();
        int position = 0;
        int rounds = 0;
        int spare = 6;

        // when
        boardService.sow(board, position, rounds, spare);

        // then
        assertThat(board.getPits()).isEqualTo(new int[]{0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0});
    }

    @Test
    void sowShouldSowWithoutSpare() {
        // given
        Board board = new Board();
        int position = 0;
        int rounds = 1;
        int spare = 0;

        // when
        boardService.sow(board, position, rounds, spare);

        // then
        assertThat(board.getPits()).isEqualTo(new int[]{1, 7, 7, 7, 7, 7, 1, 7, 7, 7, 7, 7, 7, 1});

    }

    @Test
    void captureShouldCapture() {
        // given
        Board board = new Board();
        int captureFrom = 5;
        board.setPits(new int[]{0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0});

        // when
        boardService.capture(board, captureFrom);

        // then
        assertThat(board.getPits()).isEqualTo(new int[]{0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0});
    }

    @Test
    void shouldChangeTurn() {
        // given
        Board board = new Board();
        board.setPits(new int[]{0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 0});

        // when
        boardService.changeTurn(board);

        // then
        assertThat(board.getPits()).isEqualTo(new int[]{1, 2, 3, 4, 5, 6, 0, 0, 0, 0, 0, 0, 0, 0});
    }

    @Test
    void shouldReturnTrueForGameEnded() {
        // given
        Board board = new Board();
        board.setPits(new int[]{0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 0});

        // when
        boolean result = boardService.isGameEnded(board);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldCollectStonesInBigPit() {
        // given
        Board board = new Board();
        board.setPits(new int[]{1, 2, 3, 4, 5, 6, 0, 1, 1, 1, 1, 1, 1, 4});

        // when
        boardService.collectStonesInBigPit(board);

        // then
        assertThat(board.getPits()).isEqualTo(new int[]{0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 10});
    }

    @Test
    void shouldReturnWinner() {
        // given
        Board board = new Board();
        board.setPits(new int[]{0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 10});

        // when
        PlayerRole result = boardService.getWinner(board).orElse(null);

        // then
        assertThat(result).isEqualTo(PlayerRole.ONE);
    }

    @Test
    void shouldReturnEmptyForDraw() {
        // given
        Board board = new Board();
        board.setPits(new int[]{0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 10});

        // when
        PlayerRole result = boardService.getWinner(board).orElse(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnWinnerPlayerTwo() {
        // given
        Board board = new Board();
        board.setPits(new int[]{0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 21});

        // when
        PlayerRole result = boardService.getWinner(board).orElse(null);

        // then
        assertThat(result).isEqualTo(PlayerRole.TWO);
    }

    @Test
    void shouldReturnMatchScore() {
        // given
        Board board = new Board();
        board.setPits(new int[]{0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 10});

        // when
        var result = boardService.getMatchScore(board);

        // then
        assertThat(result.get(PlayerRole.ONE)).isEqualTo(21);
        assertThat(result.get(PlayerRole.TWO)).isEqualTo(10);
    }

}