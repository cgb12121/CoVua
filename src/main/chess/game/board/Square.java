package main.chess.game.board;

import main.chess.game.pieces.Piece;

/**
 * Đại diện cho một ô trên bàn cờ với các thuộc tính như hàng, cột và quân cờ.
 */
public class Square {
    private int row;
    private int col;
    private Piece piece;        // Quân cờ đặt trên ô này (nếu có)

    /**
     * Khởi tạo một ô trên bàn cờ với hàng, cột và quân cờ tương ứng.
     *
     * @param row   Hàng của ô
     * @param col   Cột của ô
     * @param piece Quân cờ được đặt trên ô (có thể là null nếu ô trống)
     */
    public Square(int row, int col, Piece piece) {
        this.row = row;
        this.col = col;
        this.piece = piece;
    }

    /**
     * Trả về hàng của ô trên bàn cờ.
     *
     * @return Hàng của ô
     */
    public int getRow() {
        return row;
    }

    /**
     * Trả về cột của ô trên bàn cờ.
     *
     * @return Cột của ô
     */
    public int getCol() {
        return col;
    }

    /**
     * Trả về quân cờ được đặt trên ô này.
     *
     * @return Quân cờ được đặt trên ô
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Đặt quân cờ mới lên ô này.
     *
     * @param piece Quân cờ mới được đặt lên ô
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Kiểm tra xem ô này có được chiếm đóng bởi một quân cờ hay không.
     *
     * @return true nếu ô này đã được chiếm đóng, ngược lại trả về false
     */
    public boolean isOccupied() {
        return piece != null;
    }
}
