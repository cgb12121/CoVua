package main.chess.game;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.pieces.King;
import main.chess.pieces.Piece;

public class Checkmate {

    // Kiểm tra xem có phải là checkmate không
    public static boolean isCheckmate(Board board, Team team) {
        KingPosition kingPosition = findKing(board, team);
        if (kingPosition != null) {
            int kingRow = kingPosition.getRow();
            int kingCol = kingPosition.getCol();
            return isKingInCheck(board, kingRow, kingCol);
        }
        return false;
    }

    // Tìm vị trí của Vua trong bàn cờ
    private static KingPosition findKing(Board board, Team team) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = board.getSquare(row, col);
                Piece piece = square.getPiece();
                if (piece instanceof King && piece.getTeam() == team) {
                    return new KingPosition(row, col);
                }
            }
        }
        return null;
    }

    // Kiểm tra xem Vua có đang bị chiếu không
    private static boolean isKingInCheck(Board board, int kingRow, int kingCol) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = board.getSquare(row, col);
                Piece piece = square.getPiece();
                if (piece != null && piece.getTeam() != board.getSquare(kingRow, kingCol).getPiece().getTeam()) {
                    if (piece.canMove(board, square, board.getSquare(kingRow, kingCol))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Kiểm tra xem có nước đi nào khả dụng cho Vua không
    public static boolean availableMove(Board board, int kingRow, int kingCol) {
        for (int i = kingRow - 1; i <= kingRow + 1; i++) {
            for (int j = kingCol - 1; j <= kingCol + 1; j++) {
                if (i >= 0 && i < 8 && j >= 0 && j < 8) {
                    Square square = board.getSquare(i, j);
                    Piece piece = square.getPiece();
                    if (piece == null || piece.getTeam() != board.getSquare(kingRow, kingCol).getPiece().getTeam()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Lớp vị trí của Vua
    public static class KingPosition {
        private int row; // Hàng
        private int col; // Cột

        public KingPosition(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }
}
