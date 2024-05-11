package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Team;

// Lớp Knight đại diện cho quân Mã trong trò chơi cờ vua
public class Knight extends Piece {
    // Constructor để khởi tạo một quân Mã với đội của nó
    public Knight(Team team) {
        super(team);
    }

    @Override
    // Phương thức kiểm tra xem quân Mã có thể di chuyển từ ô bắt đầu đến ô kết thúc không
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        int deltaRow = Math.abs(startRow - endRow);
        int deltaCol = Math.abs(startCol - endCol);

        // Kiểm tra xem ô kết thúc có nằm trong bàn cờ không
        if (endRow >= 0 && endRow <= 7 && endCol >= 0 && endCol <= 7) {
            // Kiểm tra xem quân Mã di chuyển theo quy tắc của nó không
            if (deltaRow == 2 && deltaCol == 1 || deltaRow == 1 && deltaCol == 2) {
                // Kiểm tra xem ô kết thúc có trống không hoặc có quân cờ của đối phương không
                if (end.getPiece() == null) {
                    return true; // Trả về true nếu ô kết thúc trống
                } else {
                    return start.getPiece().getTeam() != end.getPiece().getTeam(); // Trả về true nếu quân Mã có thể ăn quân của đối phương
                }
            }
        }
        return false; // Trả về false nếu không thỏa mãn điều kiện nào
    }

    // Phương thức trả về tên file hình ảnh của quân Mã
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Knight.png";
        } else {
            return "src/main/resources/Black/Black_Knight.png";
        }
    }
}
