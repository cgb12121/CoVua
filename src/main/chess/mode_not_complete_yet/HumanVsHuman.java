package main.chess.mode_not_complete_yet;

import main.chess.game.Team;
import main.chess.game.board.Board;
import main.chess.game.board.Square;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class HumanVsHuman extends GameMode {

    public HumanVsHuman(Board board, JPanel[][] squarePanels) {
        super(board, squarePanels);
    }

    @Override
    public void handleMouseClick(MouseEvent e) {
        int row = e.getY() / (squarePanels[0][0].getHeight());
        int col = e.getX() / (squarePanels[0][0].getWidth());
        Square clickedSquare = board.getSquare(row, col);
        if (selectedSquare == null && clickedSquare.isOccupied() && clickedSquare.getPiece().getTeam() == currentTurn) {
            selectedSquare = clickedSquare;
            squarePanels[row][col].setBackground(Color.BLUE);
            movableSquares = board.highlightMovableSquares(selectedSquare);
            highlightMovableSquares();
        } else if (selectedSquare != null) {
            boolean moveSuccessful = board.movePiece(selectedSquare, clickedSquare);
            if (moveSuccessful) {
                handlePawnPromotion(clickedSquare);
                updateBoard();
                currentTurn = (currentTurn == Team.WHITE) ? Team.BLACK : Team.WHITE;
                checkForCheckmate();
            }
            selectedSquare = null;
            movableSquares = null;
            resetSquareColors();
        }
    }

    private void highlightMovableSquares() {
    }

    private void resetSquareColors() {
    }

    private void updateBoard() {
    }

    private void handlePawnPromotion(Square endSquare) {
    }

    private void checkForCheckmate() {
    }
}
