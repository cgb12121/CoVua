package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Team;

// Lớp Bishop đại diện cho quân Tượng trong trò chơi cờ vua
public class Bishop extends Piece {
    // Constructor để khởi tạo một quân Tượng với đội của nó
    public Bishop(Team team) {
        super(team);
    }

    @Override
    // Phương thức kiểm tra xem quân Tượng có thể di chuyển từ ô bắt đầu đến ô kết thúc không
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Kiểm tra xem ô bắt đầu và ô kết thúc có hợp lệ không
        if (isValidSquare(startRow, startCol) || isValidSquare(endRow, endCol)) {
            return false;
        }

        int deltaRow = endRow - startRow;
        int deltaCol = endCol - startCol;

        // Kiểm tra xem quân Tượng di chuyển theo đường chéo không
        if (Math.abs(deltaRow) != Math.abs(deltaCol)) {
            return false;
        }

        int rowIncrement = deltaRow > 0 ? 1 : -1;
        int colIncrement = deltaCol > 0 ? 1 : -1;
        for (int row = startRow + rowIncrement, col = startCol + colIncrement;
             row != endRow && col != endCol;
             row += rowIncrement, col += colIncrement) {
            if (isValidSquare(row, col)) {
                return false;
            }
            Square nextSquare = board.getSquare(row, col);
            if (nextSquare.isOccupied()) {
                return false;
            }
        }

        // Kiểm tra xem ô kết thúc có quân cờ và thuộc đội khác không
        if (end.isOccupied() && start.getPiece().getTeam() != end.getPiece().getTeam()) {
            return true;
        }

        return !end.isOccupied(); // Trả về true nếu ô kết thúc không có quân cờ
    }
    // Kiểm tra ô di chuyển có hợp lệ trên bàn cờ
    private boolean isValidSquare(int row, int col) {
        return row < 0 || row >= 8 || col < 0 || col >= 8;
    }

    // Phương thức trả về tên file hình ảnh của quân Tượng
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Bishop.png";
        } else {
            return "src/main/resources/Black/Black_Bishop.png";
        }
    }
}
