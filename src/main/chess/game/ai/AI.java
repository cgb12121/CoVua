package main.chess.game.ai;

import main.chess.game.Team;
import main.chess.game.board.Board;
import main.chess.game.board.Move;
import main.chess.game.board.Square;
import main.chess.game.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static main.chess.game.ai.PiecePositionValue.*;
import static main.chess.game.ai.PieceValue.*;

/**
 * AI class xử lý các bước đi của máy tính trong trò chơi cờ vua.
 */
public class AI {
    private Board board;
    private JPanel[][] squarePanels;
    private Square lastStartSquare;
    private Square lastEndSquare;

    /**
     * Khởi tạo đối tượng AI với thông tin bàn cờ và các ô trên bàn cờ.
     *
     * @param board Bàn cờ hiện tại
     * @param squarePanels Mảng JPanel chứa các ô trên bàn cờ
     */
    public AI(Board board, JPanel[][] squarePanels) {
        this.board = board;
        this.squarePanels = squarePanels;
        this.lastStartSquare = null;
        this.lastEndSquare = null;
    }

    /**
     * Trả về danh sách tất cả các nước đi hợp lệ cho một đội nhất định.
     *
     * @param team Đội cần kiểm tra các nước đi hợp lệ
     * @return Danh sách các nước đi hợp lệ
     */
    public List<Move> getAllValidMoves(Team team) {
        List<Move> validMoves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square startSquare = board.getSquare(row, col);
                // Kiểm tra nếu ô có chứa quân cờ và quân cờ đó thuộc về đội được chỉ định
                if (startSquare.isOccupied() && startSquare.getPiece().getTeam() == team) {
                    List<Square> movableSquares = board.highlightMovableSquares(startSquare);
                    for (Square endSquare : movableSquares) {
                        validMoves.add(new Move(startSquare, endSquare));
                    }
                }
            }
        }

        return validMoves;
    }

    /**
     * Thuật toán minimax được sử dụng để đánh giá điểm số tốt nhất cho bảng cờ hiện tại.
     *
     * @param depth Độ sâu tối đa của thuật toán
     * @param alpha Giá trị tốt nhất hiện tại cho người chơi tối đa
     * @param beta Giá trị tốt nhất hiện tại cho người chơi tối thiểu
     * @param isMaximizingPlayer Xác định xem đang đến lượt của người chơi tối đa hay không
     * @return Giá trị điểm số tốt nhất cho bảng cờ
     */
    private int minimax(int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        // Base case
        if (depth == 0) {
            return evaluateBoard();
        }

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            // Duyệt qua tất cả các nước đi hợp lệ cho người chơi tối đa (trắng)
            for (Move move : getAllValidMoves(Team.WHITE)) {
                Square start = move.getStart();
                Square end = move.getEnd();
                Piece movedPiece = movePieceTemporarily(start, end);

                // Gọi đệ quy để đánh giá bảng cờ với độ sâu giảm đi một đơn vị
                int eval = minimax(depth - 1, alpha, beta, false);
                undoMove(start, end, movedPiece);
                // Cập nhật giá trị tốt nhất cho người chơi tối đa
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                // Dừng đệ quy khi  một nhánh trong cây tìm kiếm Minimax đã được đánh giá và đã xác định được giá trị tốt nhất
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            // Duyệt qua tất cả các nước đi hợp lệ cho người chơi tối thiểu (đen)
            for (Move move : getAllValidMoves(Team.BLACK)) {
                Square start = move.getStart();
                Square end = move.getEnd();
                Piece movedPiece = movePieceTemporarily(start, end);

                // Gọi đệ quy để đánh giá bảng cờ với độ sâu giảm đi một đơn vị
                int eval = minimax(depth - 1, alpha, beta, true);
                undoMove(start, end, movedPiece);
                // Cập nhật giá trị tốt nhất cho người chơi tối thiểu
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                // Dừng đệ quy khi  một nhánh trong cây tìm kiếm Minimax đã được đánh giá và đã xác định được giá trị tốt nhất
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }

    /**
     * Tìm và trả về nước đi tốt nhất cho một đội nhất định sử dụng thuật toán Minimax với cắt tỉa Alpha-Beta.
     *
     * @param team  Đội cần tìm nước đi tốt nhất
     * @param depth Độ sâu tối đa của thuật toán
     * @return Nước đi tốt nhất cho đội đã được chỉ định
     */
    public Move findBestMove(Team team, int depth) {
        Move bestMove = null;
        int bestValue = team == Team.WHITE ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // Lấy danh sách tất cả các nước đi hợp lệ cho đội hiện tại
        List<Move> allPossibleMoves = getAllValidMoves(team);
        for (Move move : allPossibleMoves) {
            Square start = move.getStart();
            Square end = move.getEnd();
            Piece movedPiece = movePieceTemporarily(start, end);

            // Đánh giá giá trị của bảng sau khi thực hiện nước đi
            int boardValue = minimax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, team == Team.BLACK);

            // Hoàn nguyên nước đi
            undoMove(start, end, movedPiece);

            // Cập nhật nước đi tốt nhất
            if (team == Team.WHITE && boardValue > bestValue) {
                bestValue = boardValue;
                bestMove = move;
            } else if (team == Team.BLACK && boardValue < bestValue) {
                bestValue = boardValue;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Trả về giá trị của quân cờ dựa trên loại của quân cờ.
     *
     * @param piece Quân cờ cần đánh giá giá trị
     * @return Giá trị của quân cờ
     * @throws IllegalArgumentException Nếu loại của quân cờ không hợp lệ
     */
    private int getPieceValue(Piece piece) {
        switch (piece.getType()) {
            case PAWN:
                return PAWN_VALUE;
            case KNIGHT:
                return KNIGHT_VALUE;
            case BISHOP:
                return BISHOP_VALUE;
            case ROOK:
                return ROOK_VALUE;
            case QUEEN:
                return QUEEN_VALUE;
            case KING:
                return KING_VALUE;
            default:
                throw new IllegalArgumentException("Unknown piece type: " + piece.getType());
        }
    }

    /**
     * Trả về giá trị vị trí của một quân cờ dựa trên loại của quân cờ và vị trí trên bàn cờ.
     *
     * @param piece  Quân cờ cần đánh giá giá trị vị trí
     * @param square Ô trên bàn cờ mà quân cờ đang đứng
     * @return Giá trị vị trí của quân cờ
     * @throws IllegalArgumentException Nếu loại của quân cờ không hợp lệ
     */
    private int getPositionalValue(Piece piece, Square square) {
        int row = square.getRow();
        int col = square.getCol();
        switch (piece.getType()) {
            case PAWN:
                return piece.getTeam() == Team.WHITE ? PAWN_TABLE[row][col] : PAWN_TABLE[7 - row][col];
            case KNIGHT:
                return piece.getTeam() == Team.WHITE ? KNIGHT_TABLE[row][col] : KNIGHT_TABLE[7 - row][col];
            case BISHOP:
                return piece.getTeam() == Team.WHITE ? BISHOP_TABLE[row][col] : BISHOP_TABLE[7 - row][col];
            case ROOK:
                return piece.getTeam() == Team.WHITE ? ROOK_TABLE[row][col] : ROOK_TABLE[7 - row][col];
            case QUEEN:
                return piece.getTeam() == Team.WHITE ? QUEEN_TABLE[row][col] : QUEEN_TABLE[7 - row][col];
            case KING:
                return piece.getTeam() == Team.WHITE ? KING_TABLE[row][col] : KING_TABLE[7 - row][col];
            default:
                throw new IllegalArgumentException("Unknown piece type: " + piece.getType());
        }
    }

    /**
     * Đánh giá và trả về giá trị của bảng cờ dựa trên các yếu tố như giá trị quân cờ, giá trị vị trí,
     * sức di động, kiểm soát các ô quan trọng và sự an toàn của vua.
     *
     * @return Giá trị đánh giá của bảng cờ
     */
    private int evaluateBoard() {
        int evaluation = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = board.getSquare(row, col);
                // Nếu ô không trống
                if (square.isOccupied()) {
                    Piece piece = square.getPiece();
                    // Lấy giá trị của quân cờ và giá trị vị trí của quân cờ
                    int pieceValue = getPieceValue(piece);
                    int positionalValue = getPositionalValue(piece, square);
                    // Tổng giá trị của quân cờ bao gồm cả giá trị của quân và giá trị vị trí
                    int totalValue = pieceValue + positionalValue;
                    // Cập nhật giá trị đánh giá với dấu phù hợp tùy thuộc vào đội của quân cờ
                    if (piece.getTeam() == Team.WHITE) {
                        evaluation += totalValue;
                    } else {
                        evaluation -= totalValue;
                    }
                }
            }
        }

        // Tính độ cơ động của các đội
        int whiteMobility = getMobility(Team.WHITE);
        int blackMobility = getMobility(Team.BLACK);
        evaluation += whiteMobility - blackMobility;

        // Tính kiểm soát của các ô quan trọng bởi các đội
        int whiteControl = getControlOfKeySquares(Team.WHITE);
        int blackControl = getControlOfKeySquares(Team.BLACK);
        evaluation += whiteControl - blackControl;

        // Tính sự an toàn của vua cho mỗi đội
        int whiteKingSafety = getKingSafety(Team.WHITE);
        int blackKingSafety = getKingSafety(Team.BLACK);
        evaluation += whiteKingSafety - blackKingSafety;

        return evaluation;
    }

    /**
     * Tính và trả về sức di động của một đội bằng cách đếm số lượng nước đi hợp lệ.
     *
     * @param team Đội cần tính sức di động
     * @return Số lượng nước đi hợp lệ
     */
    private int getMobility(Team team) {
        int mobility;
        List<Move> allMoves = getAllValidMoves(team);
        mobility = allMoves.size();
        return mobility;
    }

    /**
     * Tính và trả về số lượng ô quan trọng đang được kiểm soát bởi một đội.
     *
     * @param team Đội cần kiểm soát
     * @return Số lượng ô quan trọng đang được kiểm soát
     */
    private int getControlOfKeySquares(Team team) {
        int control = 0;
        int[][] keySquares = {
                {3, 3}, {3, 4}, {4, 3}, {4, 4}
        };
        for (int[] keySquare : keySquares) {
            Square square = board.getSquare(keySquare[0], keySquare[1]);
            // Nếu ô đang được chiếm giữ bởi quân cờ của đội hiện tại, tăng điểm kiểm soát
            if (square.isOccupied() && square.getPiece().getTeam() == team) {
                control += 1;
            }
        }
        return control;
    }

    /**
     * Tính và trả về sự an toàn của vua của một đội bằng cách đếm số lượng ô xung quanh vua không bị chiếm giữ
     * hoặc bị chiếm giữ bởi quân cờ cùng đội.
     *
     * @param team Đội cần kiểm tra sự an toàn của vua
     * @return Số lượng ô an toàn xung quanh vua
     */
    private int getKingSafety(Team team) {
        int kingSafety = 0;
        // Tìm ô chứa vua của đội hiện tại
        Square kingSquare = board.findKingSquare(team);
        if (kingSquare != null) {
            int row = kingSquare.getRow();
            int col = kingSquare.getCol();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                        Square adjacentSquare = board.getSquare(newRow, newCol);
                        if (!adjacentSquare.isOccupied() || adjacentSquare.getPiece().getTeam() == team) {
                            // Nếu ô không được chiếm giữ hoặc chỉ được chiếm giữ bởi quân cờ của đội hiện tại, tăng sự an toàn
                            kingSafety += 1;
                        }
                    }
                }
            }
        }
        return kingSafety;
    }

    /**
     * Di chuyển một quân cờ từ ô bắt đầu đến ô kết thúc tạm thời. Trả về quân cờ bị bắt nếu có.
     *
     * @param start Ô bắt đầu của quân cờ
     * @param end   Ô kết thúc của quân cờ
     * @return Quân cờ bị bắt, nếu có
     */
    private Piece movePieceTemporarily(Square start, Square end) {
        Piece piece = start.getPiece();
        Piece capturedPiece = end.getPiece();
        end.setPiece(piece);
        start.setPiece(null);
        return capturedPiece;
    }

    /**
     * Hoàn tác một nước đi tạm thời bằng cách di chuyển quân cờ từ ô kết thúc đến ô bắt đầu và trả về quân cờ bị bắt.
     *
     * @param start         Ô bắt đầu của nước đi
     * @param end           Ô kết thúc của nước đi
     * @param capturedPiece Quân cờ bị bắt
     */
    private void undoMove(Square start, Square end, Piece capturedPiece) {
        Piece piece = end.getPiece();
        start.setPiece(piece);
        end.setPiece(capturedPiece);
    }

    /**
     * Thực hiện nước đi tốt nhất cho một đội đã được tính toán bằng thuật toán Minimax với cắt tỉa Alpha-Beta.
     * Nước đi được thực hiện trên bảng và các ô trên giao diện người dùng được cập nhật màu nền.
     *
     * @param team Đội cần thực hiện nước đi (AI)
     */
    public void makeAIMove(Team team) {
        Move bestMove = findBestMove(team, 3);

        if (bestMove != null) {
            Square start = bestMove.getStart();
            Square end = bestMove.getEnd();

            this.lastStartSquare = start;
            this.lastEndSquare = end;

            board.movePiece(start, end);

            // Highligh last move cho AI
            SwingUtilities.invokeLater(() -> {
                squarePanels[start.getRow()][start.getCol()].setBackground(Color.BLUE);
                squarePanels[end.getRow()][end.getCol()].setBackground(Color.BLUE);
            });
        }
    }

    public Square getLastStartSquare() {
        return lastStartSquare;
    }

    public Square getLastEndSquare() {
        return lastEndSquare;
    }
}
