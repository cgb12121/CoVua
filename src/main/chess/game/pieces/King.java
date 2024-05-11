package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

import main.chess.game.Checkmate;

public class King extends Piece {

    public King(Team team) {
        super(PieceType.KING,team);
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        if (Checkmate.checkByRook(board, start, end) || Checkmate.checkByKnight(board, start, end) || Checkmate.checkByPawn(board, start, end) ||
                Checkmate.checkByBishop(board, start, end) || Checkmate.checkByQueen(board, start, end) || Checkmate.checkByKing(board, start, end)) {
            return false;
        }

        if (Math.abs(endX - startX) <= 1 && Math.abs(endY - startY) <= 1 &&
                (start.getPiece() == null || end.getPiece() == null || start.getPiece().getTeam() != end.getPiece().getTeam() || !end.isOccupied())) {
            return true;
        }

        return false;
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
