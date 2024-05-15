package main.chess.game.pieces;

import main.chess.game.board.Move;
import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

public class Pawn extends Piece {
    public Pawn(Team team) {
        super(PieceType.PAWN,team);
    } //TODO: Them phuong thuc cho Promotion
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

                if (lastEnd.getRow() == startRow && Math.abs(lastEnd.getCol() - startCol) == 1) {
                    Piece movedPiece = lastEnd.getPiece();
                    if (movedPiece != null && movedPiece.getType() == PieceType.PAWN && movedPiece.getTeam() != this.getTeam()) {
                        if (Math.abs(lastStart.getRow() - lastEnd.getRow()) == 2) {
                            return true;
                        }
                    }
                }
            }
        }

        return false; // Không có ô di chuyển hợp lệ
    }

    // Quân tốt đã rời khỏi vị trí ban đầu chưa
    private boolean hasMoved(Square square) {
        int currentRow = square.getRow();
        return (getTeam() == Team.WHITE && currentRow != 6) || (getTeam() == Team.BLACK && currentRow != 1);
    }

    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Pawn.png";
        } else {
            return "src/main/resources/Black/Black_Pawn.png";
        }
    }
}
