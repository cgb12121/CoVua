package main.chess.game;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.pieces.PieceType;

public class Checkmate {

    public static boolean kingInCheck(Board board, Square start) {
        return checkByKing(board, start, start) || checkByBishop(board, start, start) || checkByQueen(board, start, start) ||
                (checkByRook(board, start, start)) || (checkByKnight(board, start, start)) || (checkByPawn(board, start, start));
    }

    public static boolean checkByRook(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();
        int numRows = 8;
        int numCols = 8;

        // Kiểm tra cùng hàng
        for (int row = 0; row < numRows; row++) {
            if (row != endX) {
                Square targetSquare = board.getSquare(row, endY);
                if (targetSquare != null && targetSquare.getPiece() != null) {
                    // Kiểm tra xem ô nằm giữa quân xe và ô end có bị chặn không
                    if (isSquareBetweenUnblocked(board, targetSquare, end)) {
                        continue;
                    }
                    if (targetSquare.getPiece().getType() == PieceType.ROOK &&
                            targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
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
                    // Kiểm tra xem ô nằm giữa quân xe và ô end có bị chặn không
                    if (isSquareBetweenUnblocked(board, targetSquare, end)) {
                        continue;
                    }
                    if (targetSquare.getPiece().getType() == PieceType.ROOK &&
                            targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean checkByBishop(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();
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
                        targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                    // Kiểm tra xem quân tượng có bị chặn trên đường đi không
                    if (isSquareBetweenUnblocked(board, targetSquare, end)) {
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


    //TODO: Fix lỗi vẫn di chuyển đến ô mà vua sẽ bị chiếu
    public static boolean checkByQueen(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();
        int numRows = 8;
        int numCols = 8;

        // Xe
        for (int row = 0; row < numRows; row++) {
            if (row != endX) {
                Square targetSquare = board.getSquare(row, endY);
                if (targetSquare != null && targetSquare.getPiece() != null) {
                    // Kiểm tra xem ô nằm giữa quân xe và ô end có bị chặn không
                    if (isSquareBetweenUnblocked(board, targetSquare, end)) {
                        continue;
                    }
                    if (targetSquare.getPiece().getType() == PieceType.QUEEN &&
                            targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
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
                    // Kiểm tra xem ô nằm giữa quân xe và ô end có bị chặn không
                    if (isSquareBetweenUnblocked(board, targetSquare, end)) {
                        continue;
                    }
                    if (targetSquare.getPiece().getType() == PieceType.QUEEN &&
                            targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
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
                        targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                    // Kiểm tra xem quân tượng có bị chặn trên đường đi không
                    if (isSquareBetweenUnblocked(board, targetSquare, end)) {
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

    public static boolean checkByKing(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();
        int numRows = 8;
        int numCols = 8;

        // Các hướng mà vua địch có thể di chuyển đến ô end
        int[][] possibleMoves = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1}, {1, 0},  {1, 1}
        };

        // Kiểm tra các ô xung quanh ô end có vua địch không
        for (int[] move : possibleMoves) {
            int newX = endX + move[0];
            int newY = endY + move[1];

            if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                Square targetSquare = board.getSquare(newX, newY);
                if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.KING &&
                        targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                    return true;    // Ô end trong tầm vua địch
                }
            }
        }
        return false;      // Ô end không trong tầm vua địch
    }

    public static boolean checkByPawn(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();
        int numRows = 8;
        int numCols = 8;

        // Tốt trắng
        if (start.getPiece().getTeam() == Team.WHITE){
            // Các hướng ô end có thể bị tốt tấn công từ đó
            int[][] possibleMoves = {{-1,1}, {-1,-1}};
            for (int[] move : possibleMoves){
                // Xác định 2 ô chéo trước mặt ô end có tốt trắng không
                int newX = endX + move[0];
                int newY = endY + move[1];

                if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                    Square targetSquare = board.getSquare(newX, newY);
                    if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.PAWN &&
                            targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                        return true;    // Ô end trong tầm tấn công của tốt trắng
                    }
                }
            }
        }


        // Tốt đen
        if (start.getPiece().getTeam() == Team.BLACK){
            // Các hướng ô end có thể bị tốt tấn công từ đó
            int[][] possibleMoves = {{1,1}, {1,-1}};
            for (int[] move : possibleMoves){
                // Xác định 2 ô chéo trước mặt ô end có tốt đen không
                int newX = endX + move[0];
                int newY = endY + move[1];

                if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                    Square targetSquare = board.getSquare(newX, newY);
                    if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.PAWN &&
                            targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                        return true; // Ô end trong tầm tấn công của tốt đen
                    }
                }
            }
        }

        return false;   // Không có tốt của đối thủ có thế tấn công ô end
    }

    public static boolean checkByKnight(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();
        int numRows = 8;
        int numCols = 8;

        // Các hướng mà mã có thế từ đó tới ô end
        int[][] possibleMoves = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] move : possibleMoves) {
            // Xác định các ô đó có mã địch hay không
            int newX = endX + move[0];
            int newY = endY + move[1];

            if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                Square targetSquare = board.getSquare(newX, newY);
                if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.KNIGHT &&
                        targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                    return true;    // Mã địch nằm trong ô có thể di chuyển đến ô end
                }
            }
        }

        return false; // Không có mã địch có thể tới ô end
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