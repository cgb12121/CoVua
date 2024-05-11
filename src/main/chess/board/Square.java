package main.chess.board;

import main.chess.pieces.Piece;

public class Square {
    private int row; // Hàng của ô trên bàn cờ
    private int col; // Cột của ô trên bàn cờ
    private Piece piece; // Quân cờ đặt trên ô này (nếu có)

    // Constructor để khởi tạo ô với hàng, cột và quân cờ
    public Square(int row, int col, Piece piece) {
        this.row = row;
        this.col = col;
        this.piece = piece;
    }

    // Phương thức trả về hàng của ô
    public int getRow() {
        return row;
    }

    // Phương thức trả về cột của ô
    public int getCol() {
        return col;
    }

    // Phương thức trả về quân cờ đặt trên ô
    public Piece getPiece() {
        return piece;
    }

    // Phương thức để đặt quân cờ lên ô
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    // Phương thức kiểm tra xem ô có đang được chiếm bởi một quân cờ hay không
    public boolean isOccupied() {
        return piece != null;
    }
}
