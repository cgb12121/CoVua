package main.chess.game;

import java.awt.Color;

// Enum định nghĩa các màu của các đội trong trò chơi
public enum Team {
    WHITE(Color.WHITE), // Đội màu trắng
    BLACK(Color.BLACK); // Đội màu đen

    private final Color color; // Màu của đội

    // Constructor để khởi tạo một đội với màu tương ứng
    Team(Color color) {
        this.color = color;
    }

    // Phương thức trả về màu của đội
    public Color getColor() {
        return color;
    }
}
