package main.chess.game;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Checkmate {

    public static boolean canKingEscape(Board board, Square kingSquare) {
        int kingRow = kingSquare.getRow();
        int kingCol = kingSquare.getCol();
        int[][] possibleMoves = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1}, {1, 0},  {1, 1}
        };

        // Di
        for (int[] move : possibleMoves) {
            int newRow = kingRow + move[0];
            int newCol = kingCol + move[1];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Square newSquare = board.getSquare(newRow, newCol);
                if (!newSquare.isOccupied() || newSquare.getPiece().getTeam() != kingSquare.getPiece().getTeam()) {
                    if (checkByPawn(board, kingSquare, newSquare) ||
                            checkByKnight(board, kingSquare, newSquare) ||
                            checkByRook(board, kingSquare, newSquare) ||
                            checkByBishop(board, kingSquare, newSquare) ||
                            checkByQueen(board, kingSquare, newSquare) ||
                            checkByKing(board, kingSquare, newSquare)) {
                        return false;
                    }
                }
            }
        }

        return true; // Vua không chạy thoát được
    }

    // Kiểm tra nếu vua đang bị chiếu
    public static boolean kingInCheck(Board board, Square kingPos) {
        return checkByPawn(board, kingPos, kingPos) ||
                checkByKnight(board, kingPos, kingPos) ||
                checkByRook(board, kingPos, kingPos) ||
                checkByBishop(board, kingPos, kingPos) ||
                checkByQueen(board, kingPos, kingPos) ||
                checkByKing(board, kingPos, kingPos);
    }

    // Tìm vị trí của vua và quân cờ đang chiếu vua
    public static List<Square> findCheckingPieces(Board board, Square kingPos) {
        List<Square> checkingPieces = new ArrayList<>();

        // Duyệt qua bàn cờ
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = board.getSquare(row, col);
                if (square.isOccupied() && square.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                    // Nếu quân khác phe có thể tiến tới ô của vua tức là quân đó đang chiếu
                    if (square.getPiece().canMove(board, square, kingPos)) {
                        checkingPieces.add(square);
                    }
                }
            }
        }
        return checkingPieces;
    }

    // Kiểm tra nếu sau khi di chuyển vua sẽ bị chiếu bởi xe
    public static boolean checkByRook(Board board, Square kingPos, Square kingNewPos) {
        int endX = kingNewPos.getRow();
        int endY = kingNewPos.getCol();
        int numRows = 8;
        int numCols = 8;

        // Kiểm tra cùng hàng
        for (int row = 0; row < numRows; row++) {
            if (row != endX) {
                Square targetSquare = board.getSquare(row, endY);
                if (targetSquare != null && targetSquare.getPiece() != null) {
                    // Kiểm tra xem ô nằm giữa quân xe và ô kingNewPos có bị chặn không
                    if (isSquareBetweenUnblocked(board, targetSquare, kingNewPos)) {
                        continue;
                    }
                    if (targetSquare.getPiece().getType() == PieceType.ROOK &&
                            targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                        return true;
                    }
                }
            }
        }

        // Kiểm tra cùng cột
        for (int col = 0; col < numCols; col++) {
            if (col != endY) {
                Square targetSquare = board.getSquare(endX, col);
                if (targetSquare != null && targetSquare.getPiece() != null) {
                    // Kiểm tra xem ô nằm giữa quân xe và ô kingNewPos có bị chặn không
                    if (isSquareBetweenUnblocked(board, targetSquare, kingNewPos)) {
                        continue;
                    }
                    if (targetSquare.getPiece().getType() == PieceType.ROOK &&
                            targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Kiểm tra nếu sau khi di chuyển vua sẽ bị chiếu bởi tịnh
    public static boolean checkByBishop(Board board, Square kingPos, Square kingNewPos) {
        int endX = kingNewPos.getRow();
        int endY = kingNewPos.getCol();
        int numRows = 8;
        int numCols = 8;

        // Các hướng di chuyển của quân tượng (4 hướng chéo)
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        // Duyệt qua các hướng di chuyển của quân tượng
        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];
            int x = endX + dx;
            int y = endY + dy;

            // Kiểm tra xem ô mới có nằm trong phạm vi bàn cờ không
            while (x >= 0 && x < numRows && y >= 0 && y < numCols) {
                Square targetSquare = board.getSquare(x, y);
                // Nếu ô mới có quân tượng của phe đối phương, kiểm tra xem có bị chặn không
                if (targetSquare != null && targetSquare.getPiece() != null &&
                        targetSquare.getPiece().getType() == PieceType.BISHOP &&
                        targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                    // Kiểm tra xem quân tượng có bị chặn trên đường đi không
                    if (isSquareBetweenUnblocked(board, targetSquare, kingNewPos)) {
                        break;
                    }
                    return true;
                }
                // Nếu ô mới có quân khác, không có nguy cơ chiếu, thoát khỏi vòng lặp
                assert targetSquare != null;
                if (targetSquare.isOccupied()) {
                    break;
                }
                // Di chuyển tiếp theo theo hướng chéo
                x += dx;
                y += dy;
            }
        }

        return false;
    }

    // Kiểm tra nếu sau khi di chuyển vua sẽ bị chiếu bởi hậu
    public static boolean checkByQueen(Board board, Square kingPos, Square kingNewPos) {
        int endX = kingNewPos.getRow();
        int endY = kingNewPos.getCol();
        int numRows = 8;
        int numCols = 8;

        // Xe
        for (int row = 0; row < numRows; row++) {
            if (row != endX) {
                Square targetSquare = board.getSquare(row, endY);
                if (targetSquare != null && targetSquare.getPiece() != null) {
                    // Kiểm tra xem ô nằm giữa quân xe và ô kingNewPos có bị chặn không
                    if (isSquareBetweenUnblocked(board, targetSquare, kingNewPos)) {
                        continue;
                    }
                    if (targetSquare.getPiece().getType() == PieceType.QUEEN &&
                            targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                        return true;
                    }
                }
            }
        }

        // Kiểm tra cùng cột
        for (int col = 0; col < numCols; col++) {
            if (col != endY) {
                Square targetSquare = board.getSquare(endX, col);
                if (targetSquare != null && targetSquare.getPiece() != null) {
                    // Kiểm tra xem ô nằm giữa quân xe và ô kingNewPos có bị chặn không
                    if (isSquareBetweenUnblocked(board, targetSquare, kingNewPos)) {
                        continue;
                    }
                    if (targetSquare.getPiece().getType() == PieceType.QUEEN &&
                            targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                        return true;
                    }
                }
            }
        }

        // Tượng
        // Các hướng di chuyển của quân tượng (4 hướng chéo)
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        // Duyệt qua các hướng di chuyển của quân tượng
        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];
            int x = endX + dx;
            int y = endY + dy;

            // Kiểm tra xem ô mới có nằm trong phạm vi bàn cờ không
            while (x >= 0 && x < numRows && y >= 0 && y < numCols) {
                Square targetSquare = board.getSquare(x, y);
                // Nếu ô mới có quân tượng của phe đối phương, kiểm tra xem có bị chặn không
                if (targetSquare != null && targetSquare.getPiece() != null &&
                        targetSquare.getPiece().getType() == PieceType.QUEEN &&
                        targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                    // Kiểm tra xem quân tượng có bị chặn trên đường đi không
                    if (isSquareBetweenUnblocked(board, targetSquare, kingNewPos)) {
                        break;
                    }
                    return true;
                }
                // Nếu ô mới có quân khác, không có nguy cơ chiếu, thoát khỏi vòng lặp
                assert targetSquare != null;
                if (targetSquare.isOccupied()) {
                    break;
                }
                // Di chuyển tiếp theo theo hướng chéo
                x += dx;
                y += dy;
            }
        }

        return false;
    }

    // Kiểm tra nếu sau khi di chuyển vua sẽ bị chiếu bởi vua
    public static boolean checkByKing(Board board, Square kingPos, Square kingNewPos) {
        int endX = kingNewPos.getRow();
        int endY = kingNewPos.getCol();
        int numRows = 8;
        int numCols = 8;

        int[][] possibleMoves = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1}, {1, 0},  {1, 1}
        };

        for (int[] move : possibleMoves) {
            int newX = endX + move[0];
            int newY = endY + move[1];

            if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                Square targetSquare = board.getSquare(newX, newY);
                if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.KING &&
                        targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Kiểm tra nếu sau khi di chuyển vua sẽ bị chiếu bởi tốt
    public static boolean checkByPawn(Board board, Square kingPos, Square kingNewPos) {
        int endX = kingNewPos.getRow();
        int endY = kingNewPos.getCol();
        int numRows = 8;
        int numCols = 8;

        if (kingPos.getPiece().getTeam() == Team.WHITE){
            int[][] possibleMoves = {{-1,1}, {-1,-1}};
            for (int[] move : possibleMoves){
                int newX = endX + move[0];
                int newY = endY + move[1];

                if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                    Square targetSquare = board.getSquare(newX, newY);
                    if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.PAWN &&
                            targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                        return true;
                    }
                }
            }
        }

        if (kingPos.getPiece().getTeam() == Team.BLACK){
            int[][] possibleMoves = {{1,1}, {1,-1}};
            for (int[] move : possibleMoves){
                int newX = endX + move[0];
                int newY = endY + move[1];

                if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                    Square targetSquare = board.getSquare(newX, newY);
                    if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.PAWN &&
                            targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Kiểm tra nếu sau khi di chuyển vua sẽ bị chiếu bởi mã
    public static boolean checkByKnight(Board board, Square kingPos, Square kingNewPos) {
        int endX = kingNewPos.getRow();
        int endY = kingNewPos.getCol();
        int numRows = 8;
        int numCols = 8;

        int[][] possibleMoves = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] move : possibleMoves) {
            int newX = endX + move[0];
            int newY = endY + move[1];

            if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                Square targetSquare = board.getSquare(newX, newY);
                if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.KNIGHT &&
                        targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                    return true;
                }
            }
        }

        return false;
    }

    // Kiểm tra nếu có quân cờ ở giữa 2 ô trên bàn cờ
    private static boolean isSquareBetweenUnblocked(Board board, Square start, Square end) {
        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        int dx = Integer.compare(endX, startX);
        int dy = Integer.compare(endY, startY);

        int x = startX + dx;
        int y = startY + dy;

        while (x != endX || y != endY) {
            Square square = board.getSquare(x, y);
            if (square != null && square.isOccupied()) {
                return true; // Ô nằm giữa bị chặn bởi quân khác
            }
            x += dx;
            y += dy;
        }
        return false; // Không có quân nào bị chặn trên đường đi giữa hai ô
    }
}