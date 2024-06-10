package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

/**
 * Lớp đại diện cho quân Xe trong trò chơi cờ vua.
 */
public class Rook extends Piece {

    /**
     * Tạo một quân xe thuộc một đội nhất định.
     *
     * @param team Đội của quân xe.
     */
    public Rook(Team team) {
        super(PieceType.ROOK,team);
    }

    /**
     * Phương thức kiểm tra khả năng di chuyển của quân xe trên bàn cờ.
     *
     * @param board Bàn cờ hiện tại.
     * @param start Ô bắt đầu của quân xe.
     * @param end   Ô kết thúc của quân xe.
     * @return true nếu quân xe có thể di chuyển đến ô kết thúc, ngược lại trả về false.
     */
    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Di chuyển thẳng, sang ngang
        if (end.isOccupied()) {
            if (start.getPiece().getTeam() != end.getPiece().getTeam()) {
                if (startRow == endRow && startCol != endCol) {
                    int minCol = Math.min(startCol, endCol);
                    int maxCol = Math.max(startCol, endCol);
                    for (int col = minCol + 1; col < maxCol; col++) {
                        if (board.getSquare(startRow, col).isOccupied()) {
                            return false;
                        }
                    }
                    return true;
                }
                else if (startRow != endRow && startCol == endCol) {
                    int minRow = Math.min(startRow, endRow);
                    int maxRow = Math.max(startRow, endRow);
                    for (int row = minRow + 1; row < maxRow; row++) {
                        if (board.getSquare(row, startCol).isOccupied()) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        // Trên đường đến bị chặn
        else {
            if (startRow == endRow && startCol != endCol) {
                int minCol = Math.min(startCol, endCol);
                int maxCol = Math.max(startCol, endCol);
                for (int col = minCol + 1; col < maxCol; col++) {
                    if (board.getSquare(startRow, col).isOccupied()) {
                        return false;
                    }
                }
                return true;
            }
            else if (startRow != endRow && startCol == endCol) {
                int minRow = Math.min(startRow, endRow);
                int maxRow = Math.max(startRow, endRow);
                for (int row = minRow + 1; row < maxRow; row++) {
                    if (board.getSquare(row, startCol).isOccupied()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Phương thức di chuyển quân xe từ ô bắt đầu đến ô kết thúc.
     *
     * @param start Ô bắt đầu của quân xe.
     * @param end   Ô kết thúc của quân xe.
     */
    @Override
    public void move(Square start, Square end) {
        super.move(start, end);
    }

    /**
     * Trả về tên file biểu tượng của quân Xe.
     *
     * @return Tên file biểu tượng của quân Xe
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Rook.png";
        } else {
            return "src/main/resources/Black/Black_Rook.png";
        }
    }
}
