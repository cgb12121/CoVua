package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Team;

// Lớp Queen đại diện cho quân Hậu trong trò chơi cờ vua
public class Queen extends Piece {
    private final Rook rook; // Biến đại diện cho quân Xe
    private final Bishop bishop; // Biến đại diện cho quân Tượng

    // Constructor để khởi tạo một quân Hậu với đội của nó
    public Queen(Team team) {
        super(team);
        this.rook = new Rook(team); // Khởi tạo một quân Xe với đội tương ứng
        this.bishop = new Bishop(team); // Khởi tạo một quân Tượng với đội tương ứng
    }

    @Override
    // Phương thức kiểm tra xem quân Hậu có thể di chuyển từ ô bắt đầu đến ô kết thúc không
    public boolean canMove(Board board, Square start, Square end) {
        // Quân Hậu có thể di chuyển như quân Xe hoặc quân Tượng
        return rook.canMove(board, start, end) || bishop.canMove(board, start, end);
    }

    // Phương thức trả về tên file hình ảnh của quân Hậu
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Queen.png";
        } else {
            return "src/main/resources/Black/Black_Queen.png";
        }
    }
}
