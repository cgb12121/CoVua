package main.chess.game;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Checkmate cung cấp các phương thức để kiểm tra tình trạng chiếu hết.
 */
public class Checkmate {

    /**
     * Kiểm tra xem vua có thể thoát khỏi chiếu hay không.
     *
     * @param board      Bàn cờ hiện tại.
     * @param kingSquare Ô mà vua đang đứng.
     * @return true nếu vua có thể thoát khỏi chiếu, ngược lại trả về false.
     */
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

    /**
     * Kiểm tra xem vua có đang bị chiếu không.
     *
     * @param board   Bàn cờ hiện tại.
     * @param kingPos Vị trí hiện tại của vua.
     * @return true nếu vua đang bị chiếu, ngược lại trả về false.
     */    public static boolean kingInCheck(Board board, Square kingPos) {
        return checkByPawn(board, kingPos, kingPos) ||
                checkByKnight(board, kingPos, kingPos) ||
                checkByRook(board, kingPos, kingPos) ||
                checkByBishop(board, kingPos, kingPos) ||
                checkByQueen(board, kingPos, kingPos) ||
                checkByKing(board, kingPos, kingPos);
    }

    /**
     * Tìm các quân cờ đang chiếu vua và vị trí của chúng.
     *
     * @param board   Bàn cờ hiện tại.
     * @param kingPos Vị trí hiện tại của vua.
     * @return Danh sách các ô chứa quân cờ đang chiếu vua.
     */    public static List<Square> findCheckingPieces(Board board, Square kingPos) {
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

    /**
     * Kiểm tra xem vua sau khi di chuyển có bị chiếu bởi xe hay không.
     *
     * @param board       Bàn cờ hiện tại.
     * @param kingPos     Vị trí hiện tại của vua.
     * @param kingNewPos  Vị trí mới của vua.
     * @return true nếu vua bị chiếu bởi xe sau khi di chuyển, ngược lại trả về false.
     */
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

    /**
     * Kiểm tra xem vua sau khi di chuyển có bị chiếu bởi tịnh hay không.
     *
     * @param board       Bàn cờ hiện tại.
     * @param kingPos     Vị trí hiện tại của vua.
     * @param kingNewPos  Vị trí mới của vua.
     * @return true nếu vua bị chiếu bởi tịnh sau khi di chuyển, ngược lại trả về false.
     */
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

    /**
     * Kiểm tra xem vua sau khi di chuyển có bị chiếu bởi hậu hay không.
     *
     * @param board       Bàn cờ hiện tại.
     * @param kingPos     Vị trí hiện tại của vua.
     * @param kingNewPos  Vị trí mới của vua.
     * @return true nếu vua bị chiếu bởi hậu sau khi di chuyển, ngược lại trả về false.
     */
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

    /**
     * Kiểm tra xem vua sau khi di chuyển có bị chiếu bởi vua địch hay không.
     *
     * @param board       Bàn cờ hiện tại.
     * @param kingPos     Vị trí hiện tại của vua.
     * @param kingNewPos  Vị trí mới của vua.
     * @return true nếu vua bị chiếu bởi vua địch sau khi di chuyển, ngược lại trả về false.
     */
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

    /**
     * Kiểm tra xem vua sau khi di chuyển có bị chiếu bởi tốt hay không.
     *
     * @param board       Bàn cờ hiện tại.
     * @param kingPos     Vị trí hiện tại của vua.
     * @param kingNewPos  Vị trí mới của vua.
     * @return true nếu vua bị chiếu bởi tốt sau khi di chuyển, ngược lại trả về false.
     */
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

    /**
     * Kiểm tra xem vua sau khi di chuyển có bị chiếu bởi mã hay không.
     *
     * @param board       Bàn cờ hiện tại.
     * @param kingPos     Vị trí hiện tại của vua.
     * @param kingNewPos  Vị trí mới của vua.
     * @return true nếu vua bị chiếu bởi mã sau khi di chuyển, ngược lại trả về false.
     */
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

    /**
     * Kiểm tra xem có quân cờ nào ở giữa hai ô trên bàn cờ không.
     *
     * @param board Bàn cờ hiện tại.
     * @param start Ô bắt đầu.
     * @param end   Ô kết thúc.
     * @return true nếu có quân cờ nằm giữa hai ô, ngược lại trả về false.
     */
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