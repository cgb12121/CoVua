package main.chess.game;

import main.chess.game.board.Board;
import main.chess.game.board.Square;

import javax.swing.*;

public class ChessGame {
    private Board board;
    private Team currentTurn;
    private GameStatus status;

    public ChessGame() {
        this.board = new Board();
        this.currentTurn = Team.WHITE;
        this.status = GameStatus.ACTIVE;
    }

    public Team getCurrentTurn() {
        return currentTurn;
    }

    public void switchTurn() {
        currentTurn = (currentTurn == Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    private void checkGameStatus(Square start, Square end) {
        if (Checkmate.isCheckMate(board, start, end)) {
            status = (currentTurn == Team.WHITE) ? GameStatus.BLACK_WIN : GameStatus.WHITE_WIN;
            endGame();
        } else {
            status = GameStatus.ACTIVE;
        }
    }

    private void endGame(){
        String winner = null;
        if (status == GameStatus.BLACK_WIN){
            winner = "Black";
        } if (status == GameStatus.WHITE_WIN){
            winner = "White";
        }
        JOptionPane.showMessageDialog(null, "Checkmate! " + winner + " wins!", "Checkmate", JOptionPane.INFORMATION_MESSAGE);
    }

    public Board getBoard() {
        return board;
    }
}
