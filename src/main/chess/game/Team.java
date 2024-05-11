package main.chess.game;

import java.awt.Color;

public enum Team {
    WHITE(Color.WHITE),
    BLACK(Color.BLACK);

    private final Color color;

    Team(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}


