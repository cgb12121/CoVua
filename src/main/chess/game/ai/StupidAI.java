package main.chess.game.ai;

import main.chess.game.Team;
import main.chess.game.board.Board;
import main.chess.game.board.Move;
import main.chess.game.board.Square;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StupidAI {
    private Board board;
    private JPanel[][] squarePanels;

    public StupidAI(Board board, JPanel[][] squarePanels){
        this.board = board;
        this.squarePanels = squarePanels;
    }

    public void makeAIMove(Team team) {
        Move move = getRandomMove(team);
        if (move != null) {
            board.movePiece(move.getStart(), move.getEnd());
        } else {
            System.out.println("No valid moves available for " + team);

        }
    }

    public Move getRandomMove(Team team) {
        List<Move> validMoves = getAllValidMoves(team);
        if (validMoves.isEmpty()) {
            return null;
        }
        Random rand = new Random();
        return validMoves.get(rand.nextInt(validMoves.size()));
    }

    public List<Move> getAllValidMoves(Team team) {
        List<Move> validMoves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square startSquare = board.getSquare(row, col);
                if (startSquare.isOccupied() && startSquare.getPiece().getTeam() == team) {
                    List<Square> movableSquares = board.highlightMovableSquares(startSquare);
                    for (Square endSquare : movableSquares) {
                        validMoves.add(new Move(startSquare, endSquare));
                    }
                }
            }
        }

        return validMoves;
    }
}
