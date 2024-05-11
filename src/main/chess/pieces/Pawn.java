package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Team;

// Lớp Pawn đại diện cho quân Tốt trong trò chơi cờ vua
public class Pawn extends Piece {
    // Constructor để khởi tạo một quân Tốt với đội của nó
    public Pawn(Team team) {
        super(team);
    }

    @Override
    // Phương thức kiểm tra xem quân Tốt có thể di chuyển từ ô bắt đầu đến ô kết thúc không
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        int direction = (getTeam() == Team.WHITE) ? -1 : 1; // Xác định hướng di chuyển của quân Tốt

        // Nếu ô kết thúc trống
        if (end.getPiece() == null) {
            // Kiểm tra xem quân Tốt di chuyển đúng 1 ô lên trước không
            if (endCol == startCol && endRow == startRow + direction) {
                return true;
            }

            // Kiểm tra xem quân Tốt đã di chuyển chưa và có thể di chuyển 2 ô lên trước không
            if (!hasMoved(start) && startCol == endCol && endRow == startRow + 2 * direction) {
                return board.getSquare(startRow + direction, startCol).getPiece() == null &&
                        board.getSquare(startRow + 2 * direction, startCol).getPiece() == null;
            }
        } else { // Nếu ô kết thúc có quân cờ
            // Kiểm tra xem quân Tốt có thể ăn quân đối phương không
            if (end.getPiece().getTeam() != this.getTeam() && Math.abs(endCol - startCol) == 1 && endRow == startRow + direction) {
                return true;
            }
        }
        return false; // Trả về false nếu không thỏa mãn điều kiện nào
    }

    // Phương thức kiểm tra xem quân Tốt đã di chuyển chưa
    private boolean hasMoved(Square square) {
        int currentRow = square.getRow();
        return (getTeam() == Team.WHITE && currentRow != 6) || (getTeam() == Team.BLACK && currentRow != 1);
    }

    // Phương thức trả về tên file hình ảnh của quân Tốt
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Pawn.png";
        } else {
            return "src/main/resources/Black/Black_Pawn.png";
        }
    }
}
