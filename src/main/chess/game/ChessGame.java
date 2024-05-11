package main.chess.game;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.ui.EndGameUI;

import static main.chess.game.Checkmate.availableMove;

public class ChessGame {
    private Board board; // Bàn cờ
    private Team currentTurn; // Lượt hiện tại
    private GameStatus status; // Trạng thái của trò chơi


    public ChessGame() {
        this.board = new Board(); // Khởi tạo bàn cờ mới
        this.currentTurn = Team.WHITE; // Đặt lượt hiện tại là lượt của người chơi màu trắng
        this.status = GameStatus.ACTIVE; // Đặt trạng thái của trò chơi là đang diễn ra
    }

    // Thực hiện nước đi
    public void makeMove(Square start, Square end) {
        if (!isValidMove(start, end)) {
            return; // Nếu nước đi không hợp lệ, không làm gì cả
        }

        board.movePiece(start, end); // Di chuyển quân cờ từ ô start đến ô end

        checkGameStatus(); // Kiểm tra trạng thái của trò chơi

        switchTurn(); // Chuyển lượt cho người chơi khác
    }

    // Chuyển lượt cho người chơi khác
    private void switchTurn() {
        currentTurn = (currentTurn == Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    // Kiểm tra xem nước đi có hợp lệ không
    private boolean isValidMove(Square start, Square end) {
        if (start.getRow() == end.getRow() && start.getCol() == end.getCol()) {
            return false; // Nếu ô đích trùng vị trí với ô nguồn, nước đi không hợp lệ
        }
        return true; // Nếu không, nước đi hợp lệ
    }

    // Kiểm tra trạng thái của trò chơi
    private void checkGameStatus() {
        if (Checkmate.isCheckmate(board, currentTurn)) { // Nếu trò chơi kết thúc với checkmate
            status = (currentTurn == Team.WHITE) ? GameStatus.BLACK_WIN : GameStatus.WHITE_WIN; // Xác định người chiến thắng
            displayEndGameMessage(status); // Hiển thị thông báo kết thúc trò chơi
        } else if (isStalemate(board, currentTurn)) { // Nếu trò chơi kết thúc với stalemate
            status = GameStatus.STALEMATE; // Đặt trạng thái trò chơi là stalemate
            displayEndGameMessage(status); // Hiển thị thông báo kết thúc trò chơi
        } else {
            status = GameStatus.ACTIVE; // Nếu trò chơi vẫn đang diễn ra
        }
    }

    // Kiểm tra xem có stalemate không
    public static boolean isStalemate(Board board, Team currentTurn) {
        Checkmate.KingPosition kingPosition = board.getKingPosition(currentTurn);
        if (kingPosition != null) {
            int kingRow = kingPosition.getRow();
            int kingCol = kingPosition.getCol();
            if (!Checkmate.isCheckmate(board, currentTurn) && !availableMove(board, kingRow, kingCol)) {
                return true; // Nếu không có nước đi khả dụng cho Vua và không trong tình trạng checkmate, đó là stalemate
            }
        }
        return false;
    }

    // Hiển thị thông báo kết thúc trò chơi
    private void displayEndGameMessage(GameStatus status) {
        String message = "";
        switch (status) {
            case BLACK_WIN:
                message = "Black wins!";
                break;
            case WHITE_WIN:
                message = "White wins!";
                break;
            case STALEMATE:
                message = "Stalemate!";
                break;
            default:
                break;
        }
        EndGameUI.displayEndGameMessage(message); // Hiển thị thông báo kết thúc trò chơi lên giao diện người dùng
    }

    // Trả về bàn cờ
    public Board getBoard() {
        return board;
    }
}
