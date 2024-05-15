package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

public abstract class Piece {
    private PieceType pieceType;
    private Team team;
    private boolean hasMoved;

    public Piece(PieceType pieceType, Team team) {
        this.pieceType = pieceType;
        this.team = team;
        this.hasMoved = false;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public PieceType getType() {
        return pieceType;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void move(Square start, Square end) {
        this.hasMoved = true;
    }

    public abstract String getIconFileName();

    public abstract boolean canMove(Board board, Square start, Square end);
}
