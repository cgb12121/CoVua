package main.chess.game;

import main.chess.game.board.Board;

public class ChessGame {
    private Board board;

    public ChessGame() {
        this.board = new Board();
    }

    public Board getBoard() {
        return board;
    }
}
