package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

public class Rook extends Piece {

    public Rook(Team team) {
        super(PieceType.ROOK,team);
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Di chuyển thẳng, sang ngang
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
                }
                else if (startRow != endRow && startCol == endCol) {
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
        }
        // Trên đường đến bị chặn
        else {
            if (startRow == endRow && startCol != endCol) {
                int minCol = Math.min(startCol, endCol);
                int maxCol = Math.max(startCol, endCol);
                for (int col = minCol + 1; col < maxCol; col++) {
                    if (board.getSquare(startRow, col).isOccupied()) {
                        return false;
                    }
                }
                return true;
            }
            else if (startRow != endRow && startCol == endCol) {
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
    public void move(Square start, Square end) {
        super.move(start, end);
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
