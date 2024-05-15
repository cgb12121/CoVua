package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

public class Knight extends Piece {
    public Knight(Team team) {
        super(PieceType.KNIGHT,team);
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        int deltaRow = Math.abs(startRow - endRow);
        int deltaCol = Math.abs(startCol - endCol);

        // Mã di chuyển hình chữ L
        if (endRow >= 0 && endRow <= 7 && endCol >= 0 && endCol <= 7) {
            if (deltaRow == 2 && deltaCol == 1 || deltaRow == 1 && deltaCol == 2) {
                // Di chuyển đến ô trống
                if (end.getPiece() == null) {
                    return true;
                } else {
                    // Ăn quân đối phương
                    return start.getPiece().getTeam() != end.getPiece().getTeam();
                }
            }
        }
        return false;
    }

    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Knight.png";
        } else {
            return "src/main/resources/Black/Black_Knight.png";
        }
    }
}
