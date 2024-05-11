package main.chess.game;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.ui.EndGameUI;

import static main.chess.game.Checkmate.availableMove;

public class ChessGame {
    private Board board;
    private Team currentTurn;
    private GameStatus status;


    public ChessGame() {
        this.board = new Board();
        this.currentTurn = Team.WHITE;
        this.status = GameStatus.ACTIVE;
    }

    public void makeMove(Square start, Square end) {
        if (!isValidMove(start, end)) {
            return;
        }

        board.movePiece(start, end);

        checkGameStatus();

        switchTurn();
    }

    private void switchTurn() {
        currentTurn = (currentTurn == Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    private boolean isValidMove(Square start, Square end) {
        if (start.getRow() == end.getRow() && start.getCol() == end.getCol()) {
            return false;
        }
        return true;
    }

    private void checkGameStatus() {
        if (Checkmate.isCheckmate(board, currentTurn)) {
            status = (currentTurn == Team.WHITE) ? GameStatus.BLACK_WIN : GameStatus.WHITE_WIN;
            displayEndGameMessage(status);
        } else if (isStalemate(board, currentTurn)) {
            status = GameStatus.STALEMATE;
            displayEndGameMessage(status);
        } else {
            status = GameStatus.ACTIVE;
        }
    }

    public static boolean isStalemate(Board board, Team currentTurn) {
        Checkmate.KingPosition kingPosition = board.getKingPosition(currentTurn);
        if (kingPosition != null) {
            int kingRow = kingPosition.getRow();
            int kingCol = kingPosition.getCol();
            if (!Checkmate.isCheckmate(board, currentTurn) && !availableMove(board, kingRow, kingCol)) {
                return true;
            }
        }
        return false;
    }

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
        EndGameUI.displayEndGameMessage(message);
    }

    public Board getBoard() {
        return board;
    }
}
