package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

/**
 * Lớp đại diện cho quân Mã trong trò chơi cờ vua.
 */
public class Knight extends Piece {
    /**
     * Khởi tạo một quân Mã với đội tương ứng.
     *
     * @param team Đội của quân Mã
     */
    public Knight(Team team) {
        super(PieceType.KNIGHT,team);
    }

    /**
     * Kiểm tra xem quân Mã có thể di chuyển từ ô ban đầu đến ô kết thúc trên bàn cờ hay không.
     *
     * @param board Bàn cờ
     * @param start Ô ban đầu của quân Mã
     * @param end   Ô kết thúc của quân Mã
     * @return true nếu quân Mã có thể di chuyển, ngược lại trả về false
     */
    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        int deltaRow = Math.abs(startRow - endRow);
        int deltaCol = Math.abs(startCol - endCol);

        // Mã di chuyển hình chữ L
        if (endRow >= 0 && endRow <= 7 && endCol >= 0 && endCol <= 7) {
            if (deltaRow == 2 && deltaCol == 1 || deltaRow == 1 && deltaCol == 2) {
                // Di chuyển đến ô trống
                if (end.getPiece() == null) {
                    return true;
                } else {
                    // Ăn quân đối phương
                    return start.getPiece().getTeam() != end.getPiece().getTeam();
                }
            }
        }
        return false;
    }

    /**
     * Trả về tên file biểu tượng của quân Mã.
     *
     * @return Tên file biểu tượng của quân Mã
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Knight.png";
        } else {
            return "src/main/resources/Black/Black_Knight.png";
        }
    }
}
