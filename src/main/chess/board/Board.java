package main.chess.board;

import main.chess.game.Checkmate;
import main.chess.pieces.*;
import main.chess.game.Team;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private Square[][] squares; // Mảng chứa các ô trên bàn cờ

    public Board() {
        this.squares = new Square[8][8]; // Khởi tạo bàn cờ 8x8
        resetBoard(); // Thiết lập trạng thái ban đầu của bàn cờ
    }

    private void resetBoard() {
        // Thiết lập quân cờ màu trắng
        for (int col = 0; col < 8; col++) {
            squares[6][col] = new Square(6, col, new Pawn(Team.WHITE));
        }

        squares[7][0] = new Square(7, 0, new Rook(Team.WHITE));
        squares[7][7] = new Square(7, 7, new Rook(Team.WHITE));

        squares[7][1] = new Square(7, 1, new Knight(Team.WHITE));
        squares[7][6] = new Square(7, 6, new Knight(Team.WHITE));

        squares[7][2] = new Square(7, 2, new Bishop(Team.WHITE));
        squares[7][5] = new Square(7, 5, new Bishop(Team.WHITE));

        squares[7][3] = new Square(7, 3, new Queen(Team.WHITE));
        squares[7][4] = new Square(7, 4, new King(Team.WHITE));

        // Thiết lập quân cờ màu đen
        for (int col = 0; col < 8; col++) {
            squares[1][col] = new Square(1, col, new Pawn(Team.BLACK));
        }

        squares[0][0] = new Square(0, 0, new Rook(Team.BLACK));
        squares[0][7] = new Square(0, 7, new Rook(Team.BLACK));

        squares[0][1] = new Square(0, 1, new Knight(Team.BLACK));
        squares[0][6] = new Square(0, 6, new Knight(Team.BLACK));

        squares[0][2] = new Square(0, 2, new Bishop(Team.BLACK));
        squares[0][5] = new Square(0, 5, new Bishop(Team.BLACK));

        squares[0][3] = new Square(0, 3, new Queen(Team.BLACK));
        squares[0][4] = new Square(0, 4, new King(Team.BLACK));

        // Thiết lập các ô trống
        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new Square(row, col, null);
            }
        }
    }

    // Trả về ô trên bàn cờ theo hàng và cột
    public Square getSquare(int row, int col) {
        return squares[row][col];
    }

    // Loại bỏ quân cờ khỏi ô trên bàn cờ
    public void removePiece(int row, int col) {
        squares[row][col].setPiece(null);
    }

    // Đặt quân cờ vào ô trên bàn cờ
    public void placePiece(int row, int col, Piece piece) {
        squares[row][col].setPiece(piece);
    }

    // Di chuyển quân cờ từ ô bắt đầu đến ô kết thúc
    public boolean movePiece(Square start, Square end) {
        if (start == null || end == null || start == end || !start.isOccupied() || !start.getPiece().canMove(this, start, end)) {
            return false;
        }

        Piece piece = start.getPiece();
        end.setPiece(piece);
        start.setPiece(null);

        return true;
    }

    // Trả về danh sách các ô có thể di chuyển từ một ô đã chọn
    public List<Square> highlightMovableSquares(Square selectedSquare) {
        List<Square> movableSquares = new ArrayList<>();
        Piece selectedPiece = selectedSquare.getPiece();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square destinationSquare = squares[row][col];
                if (selectedPiece != null && selectedPiece.canMove(this, selectedSquare, destinationSquare)) {
                    movableSquares.add(destinationSquare);
                }
            }
        }

        return movableSquares;
    }

    // Trả về vị trí của Vua của một đội
    public Checkmate.KingPosition getKingPosition(Team team) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = squares[row][col];
                Piece piece = square.getPiece();
                if (piece instanceof main.chess.pieces.King && piece.getTeam() == team) {
                    return new Checkmate.KingPosition(row, col);
                }
            }
        }
        return null;
    }
}
