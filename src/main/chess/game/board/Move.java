package main.chess.game.board;

/**
 * Đại diện cho một nước đi.
 */
public class Move {
    private Square start;
    private Square end;

    /**
     * Khởi tạo một nước đi mới.
     *
     * @param start Ô bắt đầu của nước đi
     * @param end   Ô kết thúc của nước đi
     */
    public Move(Square start, Square end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Trả về ô bắt đầu của nước đi.
     *
     * @return Ô bắt đầu của nước đi
     */
    public Square getStart() {
        return start;
    }

    /**
     * Trả về ô kết thúc của nước đi.
     *
     * @return Ô kết thúc của nước đi
     */
    public Square getEnd() {
        return end;
    }
}
