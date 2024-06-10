package main.chess.game;

import main.chess.game.board.Board;

/**
 * Lớp ChessGame đại diện cho một trò chơi cờ vua.
 */
public class ChessGame {
    private Board board;

    /**
     * Constructor của lớp ChessGame.
     * Tạo một trò chơi mới với một bàn cờ mới.
     */
    public ChessGame() {
        this.board = new Board();
    }

    /**
     * Phương thức getter để lấy bàn cờ của trò chơi.
     *
     * @return Bàn cờ của trò chơi.
     */
    public Board getBoard() {
        return board;
    }
}
