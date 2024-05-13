package main.chess.game;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Checkmate {

    //TODO: kiểm tra xem
    //TODO: 1. Kiểm tra vua có đang bị chiếu hay không  +++Done
    //TODO: 2. Kiểm tra vua có còn các di chuyển hợp lệ hay không
    //TODO: 3. Kiểm tra xem có quân nào có thể chặn đường chiếu hay không
    //TODO: 4. Kiểm tra xem có quân nào phe ta có thể ăn quân đang chiếu không
    public static boolean isCheckMate(Board board, Square kingPos, Square newPos){
        if (!kingInCheck(board, kingPos)){
            return false;
        }

        if (kingPos.getPiece().getType() == PieceType.KING && kingPos.getPiece().canMove(board, kingPos, newPos)){
            return false;
        }

        if (canStopCheck(board, kingPos, newPos)){
            return false;
        }

        return true;
    }

    public static boolean canStopCheck(Board board, Square kingPos, Square end){
        Team kingTeam = kingPos.getPiece().getTeam();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square teamate = board.getSquare(row, col);
                // Check if the teamate is occupied by a piece of the same team as the king
                if (teamate.isOccupied() && teamate.getPiece().getTeam() == kingTeam) {
                    if (board.movePiece(teamate, end)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean kingInCheck(Board board, Square kingPos) {
        return checkByPawn(board, kingPos, kingPos) ||
                checkByKnight(board, kingPos, kingPos) ||
                checkByRook(board, kingPos, kingPos) ||
                checkByBishop(board, kingPos, kingPos) ||
                checkByQueen(board, kingPos, kingPos) ||
                checkByKing(board, kingPos, kingPos);
    }

    // New method to find king's position and checking pieces
    public static List<Square> findCheckingPieces(Board board, Square kingPos) {
        List<Square> checkingPieces = new ArrayList<>();

        // Iterate through all squares to find checking pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = board.getSquare(row, col);
                if (square.isOccupied() && square.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                    // If the piece can move to king's position, it's a checking piece
                    if (square.getPiece().canMove(board, square, kingPos)) {
                        checkingPieces.add(square);
                    }
                }
            }
        }
        return checkingPieces;
    }

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