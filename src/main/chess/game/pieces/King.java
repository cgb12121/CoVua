package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

import main.chess.game.Checkmate;

public class King extends Piece {
    private boolean hasMoved;

    public King(Team team) {
        super(PieceType.KING,team);
        this.hasMoved = false;
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        // Nếu ô đến làm cho vua bị chiếu return false
        if (Checkmate.checkByRook(board, start, end) || Checkmate.checkByKnight(board, start, end) || Checkmate.checkByPawn(board, start, end) ||
                Checkmate.checkByBishop(board, start, end) || Checkmate.checkByQueen(board, start, end) || Checkmate.checkByKing(board, start, end)) {
            return false;
        }

        // Nhập thành
        if (!hasMoved && (Math.abs(endY - startY) == 2 || Math.abs(endY - startY) == 3) && startX == endX) {
            if (isCastlingMove(board, start, end)) {
                return true;
            }
        }

        // Di chuyển bình thường hoặc ăn quân đối phương
        if (Math.abs(endX - startX) <= 1 && Math.abs(endY - startY) <= 1 &&
                (start.getPiece() == null || end.getPiece() == null || start.getPiece().getTeam() != end.getPiece().getTeam() || !end.isOccupied())) {
            return true;
        }

        return false; // Không có ô di chuyển hợp lệ
    }

    private boolean isCastlingMove(Board board, Square start, Square end) {
        int row = start.getRow();
        int colDiff = end.getCol() - start.getCol();
        boolean isKingSideCastling = colDiff == 2;
        boolean isQueenSideCastling = colDiff == -2;

        if (!isKingSideCastling && !isQueenSideCastling) {
            return false;
        }

        // Kiểm tra điều kiện quân xe phù hợp để nhập thành
        // Hàng 7 cho trắng, 0 cho đen và chưa di chuyển
        int rookCol = isKingSideCastling ? 7 : 0;
        Square rookSquare = board.getSquare(row, rookCol);
        if (rookSquare == null || rookSquare.getPiece() == null || rookSquare.getPiece().getType() != PieceType.ROOK || rookSquare.getPiece().hasMoved()) {
            return false;
        }

        // Kiểm tra giữa vua và xe có bị chặn
        int colStep = isKingSideCastling ? 1 : -1;
        for (int col = start.getCol() + colStep; col != rookCol; col += colStep) {
            if (board.getSquare(row, col).isOccupied()) {
                return false;
            }
        }

        return true; // Đủ điều kiện tiến hành nhập thành
    }

    @Override
    public void move(Square start, Square end) {
        super.move(start, end);
        hasMoved = true;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_King.png";
        } else {
            return "src/main/resources/Black/Black_King.png";
        }
    }
}
