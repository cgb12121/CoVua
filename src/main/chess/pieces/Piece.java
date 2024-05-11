package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Team;

public abstract class Piece {
    private Team team;

    public Piece(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public abstract String getIconFileName();

    public abstract boolean canMove(Board board, Square start, Square end);
}
