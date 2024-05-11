package main.chess.game;

import main.chess.board.Square;

public class Move {
    private Square start;
    private Square end;

    public Move(Square start, Square end) {
        this.start = start;
        this.end = end;
    }

    public Square getStart() {
        return start;
    }

    public void setStart(Square start) {
        this.start = start;
    }

    public Square getEnd() {
        return end;
    }

    public void setEnd(Square end) {
        this.end = end;
    }
}

