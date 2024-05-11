package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Team;

public class Bishop extends Piece {
    public Bishop(Team team) {
        super(team);
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        if (isValidSquare(startRow, startCol) || isValidSquare(endRow, endCol)) {
            return false;
        }

        int deltaRow = endRow - startRow;
        int deltaCol = endCol - startCol;

        if (Math.abs(deltaRow) != Math.abs(deltaCol)) {
            return false;
        }

        int rowIncrement = deltaRow > 0 ? 1 : -1;
        int colIncrement = deltaCol > 0 ? 1 : -1;
        for (int row = startRow + rowIncrement, col = startCol + colIncrement;
             row != endRow && col != endCol;
             row += rowIncrement, col += colIncrement) {
            if (isValidSquare(row, col)) {
                return false;
            }
            Square nextSquare = board.getSquare(row, col);
            if (nextSquare.isOccupied()) {
                return false;
            }
        }

        if (end.isOccupied() && start.getPiece().getTeam() != end.getPiece().getTeam()) {
            return true;
        }

        return !end.isOccupied();
    }

    private boolean isValidSquare(int row, int col) {
        return row < 0 || row >= 8 || col < 0 || col >= 8;
    }

    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Bishop.png";
        } else {
            return "src/main/resources/Black/Black_Bishop.png";
        }
    }
}
