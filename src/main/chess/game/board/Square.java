package main.chess.game.board;

import main.chess.game.pieces.Piece;

public class Square {
    private int row;
    private int col;
    private Piece piece;

    // Các ô trên bàn cờ gồm tọa độ cột, hàng và quân cờ
    public Square(int row, int col, Piece piece) {
        this.row = row;
        this.col = col;
        this.piece = piece;
    }

    // Lấy hàng của ô
    public int getRow() {
        return row;
    }

    // Lấy cột của ô
    public int getCol() {
        return col;
    }

    // Lấy quân cờ ở ô
    public Piece getPiece() {
        return piece;
    }

    // Thay thế quân cờ ở ô
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    // Có quân cờ ở ô
    public boolean isOccupied() {
        return piece != null;
    }
}
