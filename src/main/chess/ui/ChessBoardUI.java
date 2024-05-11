package main.chess.ui;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ChessBoardUI extends JPanel {
    private Board board;
    private JPanel[][] squarePanels;
    private Square selectedSquare;
    private List<Square> movableSquares;

    public ChessBoardUI(Board board) {
        this.board = board;
        this.squarePanels = new JPanel[8][8];
        setLayout(new GridLayout(8, 8));
        initializeBoardUI();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / (getHeight() / 8);
                int col = e.getX() / (getWidth() / 8);
                Square clickedSquare = board.getSquare(row, col);
                if (selectedSquare == null && clickedSquare.isOccupied()) {
                    selectedSquare = clickedSquare;
                    squarePanels[row][col].setBackground(Color.BLUE);
                    movableSquares = board.highlightMovableSquares(selectedSquare);
                    highlightMovableSquares();
                } else if (selectedSquare != null) {
                    boolean moveSuccessful = board.movePiece(selectedSquare, clickedSquare);
                    if (moveSuccessful) {
                        updateBoard();
                    }
                    selectedSquare = null;
                    movableSquares = null;
                    resetSquareColors();
                }
            }
        });
    }

    private void initializeBoardUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = board.getSquare(row, col);
                JPanel squarePanel = new JPanel(new BorderLayout());
                squarePanel.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                squarePanels[row][col] = squarePanel;
                add(squarePanel);
                displayPiece(square);
            }
        }
    }

    private void displayPiece(Square square) {
        if (square.isOccupied()) {
            Piece piece = square.getPiece();
            PieceUI pieceUI = new PieceUI(piece);
            JPanel squarePanel = squarePanels[square.getRow()][square.getCol()];
            squarePanel.removeAll();
            squarePanel.add(pieceUI);
            squarePanel.revalidate();
            squarePanel.repaint();
        }
    }

    private void highlightMovableSquares() {
        if (movableSquares != null) {
            for (Square square : movableSquares) {
                int row = square.getRow();
                int col = square.getCol();
                squarePanels[row][col].setBackground(Color.GREEN);
            }
        }
    }

    private void resetSquareColors() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squarePanels[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
            }
        }
    }

    public void updateBoard() {
        removeAll();
        initializeBoardUI();
        revalidate();
        repaint();
    }
}
