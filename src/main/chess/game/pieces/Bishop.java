package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

/**
 * Lớp đại diện cho quân Tượng trong trò chơi cờ vua.
 */
public class Bishop extends Piece {
    /**
     * Khởi tạo một quân Tượng với đội tương ứng.
     *
     * @param team Đội của quân Tượng
     */
    public Bishop(Team team) {
        super(PieceType.BISHOP,team);
    }

    /**
     * Kiểm tra xem quân Tượng có thể di chuyển từ ô ban đầu đến ô kết thúc trên bàn cờ hay không.
     *
     * @param board Bàn cờ
     * @param start Ô ban đầu của quân Tượng
     * @param end   Ô kết thúc của quân Tượng
     * @return true nếu quân Tượng có thể di chuyển, ngược lại trả về false
     */
    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Giới hạn di chuyển trong bàn cờ
        if (isValidSquare(startRow, startCol) || isValidSquare(endRow, endCol)) {
            return false;
        }

        // Tượng di chuyển cùng số hàng và cột
        int deltaRow = endRow - startRow;
        int deltaCol = endCol - startCol;
        if (Math.abs(deltaRow) != Math.abs(deltaCol)) {
            return false;
        }

        // Hướng di chuyển
        int rowIncrement = deltaRow > 0 ? 1 : -1;
        int colIncrement = deltaCol > 0 ? 1 : -1;
        for (int row = startRow + rowIncrement, col = startCol + colIncrement;
             row != endRow && col != endCol;
             row += rowIncrement, col += colIncrement) {
            if (isValidSquare(row, col)) {
                return false;
            }
            //Kiểm tra nếu hướng đi bị chặn
            Square nextSquare = board.getSquare(row, col);
            if (nextSquare.isOccupied()) {
                return false;
            }
        }

        // Ăn quân của địch
        if (end.isOccupied() && start.getPiece().getTeam() != end.getPiece().getTeam()) {
            return true;
        }

        // Ô không có quân ta
        return !end.isOccupied();
    }

    /**
     * Kiểm tra xem ô có hợp lệ trong bàn cờ hay không.
     *
     * @param row Hàng của ô
     * @param col Cột của ô
     * @return true nếu ô không hợp lệ, ngược lại trả về false
     */
    private boolean isValidSquare(int row, int col) {
        return row < 0 || row >= 8 || col < 0 || col >= 8;
    }

    /**
     * Trả về tên file biểu tượng của quân Tượng.
     *
     * @return Tên file biểu tượng của quân Tượng
     */
   @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Bishop.png";
        } else {
            return "src/main/resources/Black/Black_Bishop.png";
        }
    }
}
