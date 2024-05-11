package main.chess.game;

public enum GameStatus {
    ACTIVE, // Trò chơi đang diễn ra
    BLACK_WIN, // Người chơi màu đen chiến thắng
    WHITE_WIN, // Người chơi màu trắng chiến thắng
    STALEMATE, // Tình trạng stalemate (không ai chiến thắng)
}
