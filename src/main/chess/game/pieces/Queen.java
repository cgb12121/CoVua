package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

public class Queen extends Piece {
    private final Rook rook;
    private final Bishop bishop;

    public Queen(Team team) {
        super(PieceType.QUEEN,team);
        this.rook = new Rook(team);
        this.bishop = new Bishop(team);
    }

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
