package main.chess.game.pieces;

import main.chess.game.board.Move;
import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

/**
 * Lớp đại diện cho quân Tốt trong trò chơi cờ vua.
 */
public class Pawn extends Piece {
    /**
     * Khởi tạo một quân Tốt với đội tương ứng.
     *
     * @param team Đội của quân Tốt
     */
    public Pawn(Team team) {
        super(PieceType.PAWN,team);
    }

    /**
     * Kiểm tra xem quân Tốt có thể di chuyển từ ô ban đầu đến ô kết thúc trên bàn cờ hay không.
     *
     * @param board Bàn cờ
     * @param start Ô ban đầu của quân Tốt
     * @param end   Ô kết thúc của quân Tốt
     * @return true nếu quân Tốt có thể di chuyển, ngược lại trả về false
     */
    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        int direction = (getTeam() == Team.WHITE) ? -1 : 1;

        // Di chuyển đến ô trống
        if (end.getPiece() == null) {

            // Di chuyển 1 ô
            if (endCol == startCol && endRow == startRow + direction) {
                return true;
            }

            // Di chuyển 2 ô tại lượt đầu
            if (!hasMoved(start) && startCol == endCol && endRow == startRow + 2 * direction) {
                return board.getSquare(startRow + direction, startCol).getPiece() == null &&
                        board.getSquare(startRow + 2 * direction, startCol).getPiece() == null;
            }
        } else {
            // Ăn quân dịch tại ô chéo
            if (end.getPiece().getTeam() != this.getTeam() && Math.abs(endCol - startCol) == 1 && endRow == startRow + direction) {
                return true;
            }
        }

        // En passant
        if (Math.abs(startCol - endCol) == 1 && endRow == startRow + direction && end.getPiece() == null) {
            Move lastMove = board.getLastMove();
            if (lastMove != null) {
                Square lastStart = lastMove.getStart();
                Square lastEnd = lastMove.getEnd();

                // Kiem tra tot doi phuong di chuyen 2 o trong 1 luot
                if (lastEnd.getRow() == startRow && Math.abs(lastEnd.getCol() - startCol) == 1) {
                    Piece movedPiece = lastEnd.getPiece();
                    if (movedPiece != null && movedPiece.getType() == PieceType.PAWN && movedPiece.getTeam() != this.getTeam()) {
                        if (Math.abs(lastStart.getRow() - lastEnd.getRow()) == 2) {
                            // Kiem tra phuong huong cua quan tot doi phuong
                            if ((lastEnd.getCol() == startCol + 1 && endCol == startCol + 1) ||
                                    (lastEnd.getCol() == startCol - 1 && endCol == startCol - 1)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false; // Không có ô di chuyển hợp lệ
    }

    /**
     * Kiểm tra xem quân Tốt đã rời khỏi vị trí ban đầu chưa.
     *
     * @param square Ô hiện tại của quân Tốt
     * @return true nếu quân Tốt đã rời khỏi vị trí ban đầu, ngược lại trả về false
     */
    private boolean hasMoved(Square square) {
        int currentRow = square.getRow();
        return (getTeam() == Team.WHITE && currentRow != 6) || (getTeam() == Team.BLACK && currentRow != 1);
    }

    /**
     * Trả về tên file biểu tượng của quân Tốt.
     *
     * @return Tên file biểu tượng của quân Tốt
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Pawn.png";
        } else {
            return "src/main/resources/Black/Black_Pawn.png";
        }
    }
}
