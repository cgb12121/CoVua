package main.chess.game.board;

import main.chess.game.Checkmate;
import main.chess.game.pieces.*;
import main.chess.game.Team;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Square[][] squares;

    // Tạo bảng mới khi bắt đầu
    public Board() {
        this.squares = new Square[8][8];
        resetBoard();
    }

    // Đặt các quân cờ tại vị trí ban đầu
    private void resetBoard() {
        // Quân trắng
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

        // Quân đen
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

        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new Square(row, col, null);
            }
        }
    }

    public Square getSquare(int row, int col) {
        return squares[row][col];
    }

    public void removePiece(int row, int col) {
        squares[row][col].setPiece(null);
    }

    public void placePiece(Piece piece, int row, int col) {
        Square square = squares[row][col];
        square.setPiece(piece);
    }

    // Di chuyển quân cờ
    public boolean movePiece(Square start, Square end) {
        if (start == null || end == null || start == end || !start.isOccupied() || !start.getPiece().canMove(this, start, end)) {
            return false;
        }

        Piece piece = start.getPiece();

        // Tạm thời xóa ô đã chọn để kiểm tra nếu sau khi nó di chuyển vua sẽ bị chiếu
        // Nếu ô end có quân dịch coi như là capture
        end.setPiece(piece);
        start.setPiece(null);

        // Kiểm tra nếu sau khi di chuyển vua sẽ bị chiếu
        Square kingSquare = findKingSquare(piece.getTeam());
        if (Checkmate.kingInCheck(this, kingSquare)) {
            // Nếu nước di chuyển đó làm vua bị chiếu, trả quân lại vị trí ban đầu
            start.setPiece(piece);
            end.setPiece(null);
            return false;
        }

        // Nếu di chuyển không làm vua bị chiếu, cập nhật bàn sau nước đi
        return true;
    }

    // Cho các ô có thể di chuyển vào List
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

    // Tìm vị trí của vua dựa vào đội
    public Square findKingSquare(Team team) {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Square kingPos = getSquare(i, j);
                if (kingPos != null && kingPos.getPiece() != null &&
                        kingPos.getPiece().getType() == PieceType.KING &&
                        kingPos.getPiece().getTeam() == team){
                    return kingPos;
                }
            }
        }
        return null;
    }

}
