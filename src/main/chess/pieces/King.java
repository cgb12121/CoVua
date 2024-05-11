package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Checkmate;
import main.chess.game.Team;

public class King extends Piece {
    private boolean castlingDone = false;
    private boolean kingHasMoved = false;
    private boolean rookHasMoved = false;

    public King(Team team) {
        super(team);
    }

    public boolean isRookMoved() {
        return this.rookHasMoved;
    }

    public void setRookMoved(boolean rookHasMoved) {
        this.rookHasMoved = rookHasMoved;
    }

    public boolean isCastlingDone() {
        return this.castlingDone;
    }

    public void setCastlingDone(boolean castlingDone) {
        this.castlingDone = castlingDone;
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        if (end.getPiece() != null && end.getPiece().getTeam() == this.getTeam()) {
            return false;
        }

        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);
        if ((rowDiff <= 1 && colDiff <= 1) && !(rowDiff == 0 && colDiff == 0)) {
            if (Checkmate.isCheckmate(board, getTeam())) {
                return false;
            }
            kingHasMoved = true;
            return true;
        }

        if (!kingHasMoved && !castlingDone &&
                startRow == endRow && Math.abs(startCol - endCol) == 2 && (startRow == 0 || startRow == 7)) {
            kingHasMoved = true;
            setCastlingDone(true);
            return true;
        } else if (!kingHasMoved && !castlingDone &&
                startRow == endRow && Math.abs(startCol - endCol) == 3 && (startRow == 0 || startRow == 7)){
            kingHasMoved = true;
            setCastlingDone(true);
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
