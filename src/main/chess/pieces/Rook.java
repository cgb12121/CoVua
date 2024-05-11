package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Team;

public class Rook extends Piece {
    private boolean rookHasMoved = false;

    public Rook(Team team) {
        super(team);
    }

    public boolean rookHasMoved() {
        return this.rookHasMoved;
    }

    public void setRookHasMoved(boolean hasMoved) {
        this.rookHasMoved = hasMoved;
    }

    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        if (end.isOccupied()) {
            if (start.getPiece().getTeam() != end.getPiece().getTeam()) {

                if (startRow == endRow && startCol != endCol) {
                    int minCol = Math.min(startCol, endCol);
                    int maxCol = Math.max(startCol, endCol);
                    for (int col = minCol + 1; col < maxCol; col++) {
                        if (board.getSquare(startRow, col).isOccupied()) {
                            return false;
                        }
                    }
                    return true;
                } else if (startRow != endRow && startCol == endCol) {
                    int minRow = Math.min(startRow, endRow);
                    int maxRow = Math.max(startRow, endRow);
                    for (int row = minRow + 1; row < maxRow; row++) {
                        if (board.getSquare(row, startCol).isOccupied()) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        } else {
            if (startRow == endRow && startCol != endCol) {
                int minCol = Math.min(startCol, endCol);
                int maxCol = Math.max(startCol, endCol);
                for (int col = minCol + 1; col < maxCol; col++) {
                    if (board.getSquare(startRow, col).isOccupied()) {
                        return false;
                    }
                }
                return true;
            } else if (startRow != endRow && startCol == endCol) {
                int minRow = Math.min(startRow, endRow);
                int maxRow = Math.max(startRow, endRow);
                for (int row = minRow + 1; row < maxRow; row++) {
                    if (board.getSquare(row, startCol).isOccupied()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Rook.png";
        } else {
            return "src/main/resources/Black/Black_Rook.png";
        }
    }
}
