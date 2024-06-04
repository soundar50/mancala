package com.bol.mancala.service;

import com.bol.mancala.model.Board;
import com.bol.mancala.model.PlayerRole;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class BoardService {
    public boolean isLegalMove(final Board board, final PlayerRole playerRole, final int position) {
        if (board.getCurrentPlayer() == playerRole) {
            return position >= 0 && position < 6 && board.getPits()[position] != 0;
        }
        return false;
    }

    public void executeMove(final Board board, final int position) {
        // values for sowing
        final int rounds = board.getPits()[position] / 14;
        final int spare = board.getPits()[position] % 14;
        final int endingPit = spare + position;

        sow(board, position, rounds, spare);

        // capture if sowing ends on own empty pit
        if (isCapture(endingPit, board.getPits())) {
            capture(board, endingPit);
        }

        if (isEndOfTurn(endingPit)) {
            changeTurn(board);
        }

        board.setTurnCount(board.getTurnCount() + 1);
    }

    protected void sow(final Board board, final int position, final int rounds, int spare) {
        board.getPits()[position] = 0;
        for (int i = 0; i < board.getPits().length; i++) {
            board.getPits()[i] += rounds;
            if (spare > 0 && i > position) {
                board.getPits()[i]++;
                spare--;
            }
        }
    }

    private static boolean isCapture(final int endingPit, final int[] pits) {
        return endingPit >= 0 && endingPit < 6 && pits[endingPit] == 1;
    }

    protected void capture(final Board board, final int captureFrom) {
        final int opposingPosition = 12 - captureFrom;
        board.getPits()[6] += (board.getPits()[opposingPosition] + board.getPits()[captureFrom]);
        board.getPits()[opposingPosition] = 0;
        board.getPits()[captureFrom] = 0;
    }

    private static boolean isEndOfTurn(final int endingPit) {
        return endingPit != 6;
    }

    public void changeTurn(final Board board) {
        board.setCurrentPlayer(board.getCurrentPlayer().opponent());
        final int[] copy = Arrays.copyOf(board.getPits(), board.getPits().length);
        for (int i = 0; i <= 6; i++) {
            board.getPits()[i] = copy[i + 7];
            board.getPits()[i + 7] = copy[i];
        }
    }

    public boolean isGameEnded(final Board board) {
        final boolean isEmptyFirstRow = Arrays.stream(board.getPits())
                .limit(6)
                .allMatch(i -> i == 0);
        final boolean isEmptySecondRow = Arrays.stream(board.getPits())
                .skip(7)
                .limit(6)
                .allMatch(i -> i == 0);

        return isEmptyFirstRow || isEmptySecondRow;
    }

    public void collectStonesInBigPit(final Board board) {
        board.getPits()[6] = Arrays.stream(board.getPits()).limit(7).sum();
        IntStream.range(0, 6) .forEach(i -> board.getPits()[i] = 0);

        board.getPits()[13] = Arrays.stream(board.getPits()).skip(7).sum();
        IntStream.range(7, 13).forEach(i -> board.getPits()[i] = 0);
    }

    public Optional<PlayerRole> getWinner(final Board board) {
        final Map<PlayerRole, Integer> matchScore = this.getMatchScore(board);

        // Check if its draw
        if (matchScore.get(PlayerRole.ONE).equals(matchScore.get(PlayerRole.TWO))) {
            return Optional.empty();
        }

        return matchScore.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public Map<PlayerRole, Integer> getMatchScore(final Board board) {
        Map<PlayerRole, Integer> result = new HashMap<>();
        result.put(board.getCurrentPlayer(), board.getPits()[6]);
        result.put(board.getCurrentPlayer().opponent(), board.getPits()[13]);
        return result;
    }
}
