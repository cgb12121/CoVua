package main.chess.game;

import main.chess.game.board.Board;
import main.chess.game.board.Square;

public class ChessGame {
    private Board board;
    private Team currentTurn;
    private GameStatus status;

    public ChessGame() {
        this.board = new Board();
        this.currentTurn = Team.WHITE;
        this.status = GameStatus.ACTIVE;
    }

    public void switchTurn() {
        currentTurn = (currentTurn == Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    private void checkGameStatus(Board board, Square start) {
        if (Checkmate.kingInCheck(board, start)) {
            status = (currentTurn == Team.WHITE) ? GameStatus.BLACK_WIN : GameStatus.WHITE_WIN;
        } else {
            status = GameStatus.ACTIVE;
        }
    }

    public Team getCurrentTurn() {
        return currentTurn;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isActive() {
        return status == GameStatus.ACTIVE;
    }

    public boolean hasEnded() {
        return status == GameStatus.BLACK_WIN || status == GameStatus.WHITE_WIN; }
}
