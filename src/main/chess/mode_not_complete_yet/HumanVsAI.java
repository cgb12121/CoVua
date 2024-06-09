package main.chess.mode_not_complete_yet;

import main.chess.game.Team;
import main.chess.game.ai.AI;
import main.chess.game.board.Board;
import main.chess.game.board.Square;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class HumanVsAI extends GameMode {

    public HumanVsAI(Board board, JPanel[][] squarePanels, AI ai) {
        super(board, squarePanels);
        this.ai = ai;
    }

    @Override
    public void handleMouseClick(MouseEvent e) {
        if (currentTurn == Team.WHITE && !gameOver) {
            int row = e.getY() / (squarePanels[0][0].getHeight());
            int col = e.getX() / (squarePanels[0][0].getWidth());
            Square clickedSquare = board.getSquare(row, col);

            if (selectedSquare == null) {
                if (clickedSquare.isOccupied() && clickedSquare.getPiece().getTeam() == currentTurn) {
                    selectedSquare = clickedSquare;
                    squarePanels[row][col].setBackground(Color.BLUE);
                    movableSquares = board.highlightMovableSquares(selectedSquare);
                    highlightMovableSquares();
                }
            } else {
                if (clickedSquare == selectedSquare) {
                    selectedSquare = null;
                    movableSquares = null;
                    resetSquareColors();
                } else if (movableSquares != null && movableSquares.contains(clickedSquare)) {
                    boolean moveSuccessful = board.movePiece(selectedSquare, clickedSquare);
                    if (moveSuccessful) {
                        handlePawnPromotion(clickedSquare);
                        updateBoard();
                        resetSquareColors();
                        checkForCheckmate();
                        selectedSquare = null;
                        movableSquares = null;
                        if (!gameOver) {
                            currentTurn = Team.BLACK;
                            new Thread(() -> {
                                ai.makeAIMove(currentTurn);
                                SwingUtilities.invokeLater(() -> {
                                    updateBoard();
                                    checkForCheckmate();
                                    if (!gameOver) {
                                        currentTurn = Team.WHITE;
                                    }
                                });
                            }).start();
                        }
                    } else {
                        selectedSquare = null;
                        movableSquares = null;
                    }
                } else {
                    selectedSquare = null;
                    movableSquares = null;
                    resetSquareColors();
                }
            }
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

