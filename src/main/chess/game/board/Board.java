package main.chess.game.board;

import main.chess.game.Checkmate;
import main.chess.game.Team;
import main.chess.game.pieces.Bishop;
import main.chess.game.pieces.King;
import main.chess.game.pieces.Knight;
import main.chess.game.pieces.Pawn;
import main.chess.game.pieces.Piece;
import main.chess.game.pieces.PieceType;
import main.chess.game.pieces.Queen;
import main.chess.game.pieces.Rook;
import main.chess.ui.PromotionDialog;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private Square[][] squares;
    public Move lastMove;

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

//            Test AI promotion
//            if (col == 0){
//                squares[6][col] = new Square(6, col, new Pawn(Team.BLACK));
//            }
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

    // Di chuyển các quân cờ trên bàn cờ
    public boolean movePiece(Square start, Square end) {
        if (start == null || end == null || start == end || !start.isOccupied() || !start.getPiece().canMove(this, start, end)) {
            return false;
        }

        Piece piece = start.getPiece();
        boolean isEnPassant = false;
        boolean isCastling = false;
        int castlingRookCol = -1;

        // En passant
        if (piece.getType() == PieceType.PAWN && Math.abs(start.getCol() - end.getCol()) == 1 && end.getPiece() == null) {
            Move lastMove = getLastMove();
            if (lastMove != null) {
                Square lastStart = lastMove.getStart();
                Square lastEnd = lastMove.getEnd();

                if (lastEnd.getRow() == start.getRow() && Math.abs(lastEnd.getCol() - start.getCol()) == 1) {
                    Piece movedPiece = lastEnd.getPiece();
                    if (movedPiece != null && movedPiece.getType() == PieceType.PAWN && movedPiece.getTeam() != piece.getTeam()) {
                        if (Math.abs(lastStart.getRow() - lastEnd.getRow()) == 2) {
                            isEnPassant = true;
                        }
                    }
                }
            }
        }

        // Kiểm tra castling
        if (piece.getType() == PieceType.KING && Math.abs(end.getCol() - start.getCol()) == 2) {
            isCastling = true;
            castlingRookCol = end.getCol() == 6 ? 7 : 0;
        }

        // Tạm thời di chuyển các quân cờ/ăn quân cờ
        end.setPiece(piece);
        start.setPiece(null);

        // Kiểm tra nếu sau khi di chuyển vua bị chiếu
        Square kingSquare = findKingSquare(piece.getTeam());
        if (Checkmate.kingInCheck(this, kingSquare)) {
            start.setPiece(piece);
            end.setPiece(null);
            return false;
        }

        // Cập nhật last move nếu nó hợp lệ
        this.lastMove = new Move(start, end);

        // Loại bỏ quân tốt phía sau nếu đó là en passant
        if (isEnPassant) {
            int capturedPawnRow = piece.getTeam() == Team.WHITE ? end.getRow() + 1 : end.getRow() - 1;
            squares[capturedPawnRow][end.getCol()].setPiece(null);
        }

        // Thực hiện nhập thành
        if (isCastling) {
            int rookNewCol = end.getCol() == 6 ? 5 : 3;
            Square rookStart = getSquare(start.getRow(), castlingRookCol);
            Square rookEnd = getSquare(start.getRow(), rookNewCol);
            Piece rook = rookStart.getPiece();

            rookEnd.setPiece(rook);
            rookStart.setPiece(null);
        }

        // Thực hiện phong tốt
        if (piece instanceof Pawn && (end.getRow() == 0 || end.getRow() == 7)) {
            Piece promotedPiece = choosePromotionPiece(piece.getTeam());
            end.setPiece(promotedPiece);
        }

        // Cập nhật vị trí
        piece.move(start, end);
        System.out.println(piece.getType() + "_" + piece.getTeam() + " from (" + (start.getRow() + 1) + "," + (start.getCol() + 1) +
                                ") to " + "(" + (end.getRow() + 1) + "," + (end.getCol() + 1) + ")" );

        return true;
    }

    private Piece choosePromotionPiece(Team team) {
        JFrame frame = new JFrame();
        PromotionDialog dialog = new PromotionDialog(frame, team);
        dialog.setVisible(true);

        Piece chosenPiece = dialog.getChosenPiece();
        return chosenPiece != null ? chosenPiece : new Queen(team);
    }


    public Move getLastMove() {
        return lastMove;
    }

    public List<Square> highlightMovableSquares(Square selectedSquare) {
        List<Square> movableSquares = new ArrayList<>();
        Piece selectedPiece = selectedSquare.getPiece();
        Team currentTeam = selectedPiece.getTeam();

        // Kiểm tra toàn bộ bàn cờ
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square destinationSquare = squares[row][col];
                Piece destinationPiece = destinationSquare.getPiece();

                // Kiểm tra nếu quân được chọn có thể đi đến địa điểm
                if (selectedPiece.canMove(this, selectedSquare, destinationSquare)) {
                    // Tạm thời di chuyển quân đã chọn đến ô đích
                    destinationSquare.setPiece(selectedPiece);
                    selectedSquare.setPiece(null);

                    // Kiểm tra xem sau khi di chuyển vua có bị chiếu không
                    boolean inCheck = isKingInCheck(currentTeam);

                    // Nếu vua không bị chiếu, thêm vào danh sách các ô có thể di chuyển
                    if (!inCheck) {
                        movableSquares.add(destinationSquare);
                    }

                    // Nếu không, đặt quân lại vị trí ban đầu
                    selectedSquare.setPiece(selectedPiece);
                    destinationSquare.setPiece(destinationPiece);
                }

                // Kiểm tra các bước di chuyển en passant
                if (selectedPiece instanceof Pawn && selectedPiece.canMove(this, selectedSquare, destinationSquare)) {
                    // Tạm thời di chuyển quân đã chọn đến ô đích
                    destinationSquare.setPiece(selectedPiece);
                    selectedSquare.setPiece(null);

                    // Kiểm tra xem sau khi di chuyển vua có bị chiếu không
                    boolean inCheck = isKingInCheck(currentTeam);

                    // Nếu vua không bị chiếu, thêm vào danh sách các ô có thể di chuyển
                    if (!inCheck) {
                        movableSquares.add(destinationSquare);
                    }

                    // Đặt lại quân cờ về vị trí ban đầu
                    selectedSquare.setPiece(selectedPiece);
                    destinationSquare.setPiece(destinationPiece);
                }
            }
        }

        return movableSquares; // Trả lại các ô quân đó có thể di chuyển
    }

    private boolean isKingInCheck(Team team) {
        // Tìm vị trí vua cùng phe
        Square kingSquare = findKingSquare(team);

        // Kiểm tra vua bị chiếu
        return Checkmate.kingInCheck(this, kingSquare);
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
