package main.chess.mode_not_complete_yet;

import main.chess.game.Team;
import main.chess.game.ai.AI;
import main.chess.game.board.Board;
import main.chess.game.board.Square;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class GameMode {
    protected Board board;
    protected JPanel[][] squarePanels;
    protected Square selectedSquare;
    protected List<Square> movableSquares;
    protected Team currentTurn;
    protected boolean gameOver;
    protected AI ai;

    public GameMode(Board board, JPanel[][] squarePanels) {
        this.board = board;
        this.squarePanels = squarePanels;
        this.currentTurn = Team.WHITE;
        this.gameOver = false;
    }

    public abstract void handleMouseClick(MouseEvent e);
}
