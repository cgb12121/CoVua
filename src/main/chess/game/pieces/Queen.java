package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

public class Queen extends Piece {
    private Rook rook;
    private Bishop bishop;

    // Hậu = xe + tượng
    public Queen(Team team) {
        super(PieceType.QUEEN,team);
        this.rook = new Rook(team);
        this.bishop = new Bishop(team);
    }

    // Phương thức di chuyển giống xe và tượng
    @Override
    public boolean canMove(Board board, Square start, Square end) {
        return rook.canMove(board, start, end) || bishop.canMove(board, start, end);
    }

    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Queen.png";
        } else {
            return "src/main/resources/Black/Black_Queen.png";
        }
    }
}
