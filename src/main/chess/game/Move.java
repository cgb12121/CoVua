package main.chess.game;

import main.chess.board.Square;

// Lớp Move đại diện cho một nước đi trong trò chơi cờ vua
public class Move {
    private Square start; // Ô bắt đầu của nước đi
    private Square end; // Ô kết thúc của nước đi

    // Constructor để khởi tạo một nước đi từ ô bắt đầu đến ô kết thúc
    public Move(Square start, Square end) {
        this.start = start;
        this.end = end;
    }

    // Phương thức trả về ô bắt đầu của nước đi
    public Square getStart() {
        return start;
    }

    // Phương thức để đặt ô bắt đầu của nước đi
    public void setStart(Square start) {
        this.start = start;
    }

    // Phương thức trả về ô kết thúc của nước đi
    public Square getEnd() {
        return end;
    }

    // Phương thức để đặt ô kết thúc của nước đi
    public void setEnd(Square end) {
        this.end = end;
    }
}
