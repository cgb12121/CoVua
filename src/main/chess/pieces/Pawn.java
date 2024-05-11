package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Move;
import main.chess.game.Team;


import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Team team) {
        super(team);
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        int direction = (getTeam() == Team.WHITE) ? -1 : 1;

        if (end.getPiece() == null) {
            if (endCol == startCol && endRow == startRow + direction) {
                return true;
            }

            if (!hasMoved(start) && startCol == endCol && endRow == startRow + 2 * direction) {
                return board.getSquare(startRow + direction, startCol).getPiece() == null &&
                        board.getSquare(startRow + 2 * direction, startCol).getPiece() == null;
            }
        } else {
            if (end.getPiece().getTeam() != this.getTeam() && Math.abs(endCol - startCol) == 1 && endRow == startRow + direction) {
                return true;
            }
        }
        return false;
    }

    private boolean hasMoved(Square square) {
        int currentRow = square.getRow();
        return (getTeam() == Team.WHITE && currentRow != 6) || (getTeam() == Team.BLACK && currentRow != 1);
    }

    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Pawn.png";
        } else {
            return "src/main/resources/Black/Black_Pawn.png";
        }
    }
}
