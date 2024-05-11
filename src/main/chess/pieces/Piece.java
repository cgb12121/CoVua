package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Team;

// Lớp Piece là lớp trừu tượng đại diện cho các quân cờ trong trò chơi cờ vua
public abstract class Piece {
    private Team team; // Đội của quân cờ

    // Constructor để khởi tạo một quân cờ với đội của nó
    public Piece(Team team) {
        this.team = team;
    }

    // Phương thức trả về đội của quân cờ
    public Team getTeam() {
        return team;
    }

    // Phương thức để đặt đội cho quân cờ
    public void setTeam(Team team) {
        this.team = team;
    }

    // Phương thức trừu tượng trả về tên file hình ảnh của quân cờ
    public abstract String getIconFileName();

    // Phương thức trừu tượng kiểm tra xem quân cờ có thể di chuyển từ ô bắt đầu đến ô kết thúc không
    public abstract boolean canMove(Board board, Square start, Square end);
}
