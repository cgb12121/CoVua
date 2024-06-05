package main.chess.ui;

import main.chess.game.Checkmate;
import main.chess.game.Team;
import main.chess.game.board.Board;
import main.chess.game.board.Move;
import main.chess.game.board.Square;
import main.chess.game.pieces.Pawn;
import main.chess.game.pieces.Piece;
import main.chess.game.pieces.PieceType;
import main.chess.game.pieces.Queen;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ChessBoardUI extends JPanel {
    private Board board;
    private JPanel[][] squarePanels;
    private Square selectedSquare;
    private List<Square> movableSquares;
    private Team currentTurn;

    public ChessBoardUI(Board board) {
        this.board = board;
        this.squarePanels = new JPanel[8][8];
        this.currentTurn = Team.WHITE; // Lượt đầu mặc định là trắng
        setLayout(new GridLayout(8, 8));
        initializeBoardUI();
        checkForCheckmate();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / (getHeight() / 8);
                int col = e.getX() / (getWidth() / 8);
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
                        // Đổi lượt sau khi di chuyển hợp lệ
                        currentTurn = (currentTurn == Team.WHITE) ? Team.BLACK : Team.WHITE;
                        checkForCheckmate();
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
            PieceIcon pieceIcon = new PieceIcon(piece);
            JPanel squarePanel = squarePanels[square.getRow()][square.getCol()];
            squarePanel.removeAll();
            squarePanel.add(pieceIcon);
            squarePanel.revalidate();
            squarePanel.repaint();
        }
    }

    private void highlightMovableSquares() {
        if (movableSquares != null) {
            for (Square square : movableSquares) {
                int row = square.getRow();
                int col = square.getCol();

                if (square.isOccupied() && square.getPiece().getTeam() != selectedSquare.getPiece().getTeam()) {
                    // Ô có thể tấn công địch
                    squarePanels[row][col].setBackground(Color.ORANGE);
                } else if (isEnPassantCapture(square)) {
                    // Ô en passant có thể tấn công
                    squarePanels[row][col].setBackground(Color.ORANGE);
                } else {
                    // Ô có thể di chuyển
                    squarePanels[row][col].setBackground(Color.GREEN);
                }
            }
        }
    }

    private boolean isEnPassantCapture(Square endSquare) {
        Piece selectedPiece = selectedSquare.getPiece();
        if (selectedPiece instanceof Pawn && Math.abs(selectedSquare.getCol() - endSquare.getCol()) == 1 && endSquare.getPiece() == null) {
            Move lastMove = board.getLastMove();
            if (lastMove != null) {
                Square lastStart = lastMove.getStart();
                Square lastEnd = lastMove.getEnd();

                if (lastEnd.getRow() == selectedSquare.getRow() && Math.abs(lastEnd.getCol() - selectedSquare.getCol()) == 1) {
                    Piece movedPiece = lastEnd.getPiece();
                    if (movedPiece != null && movedPiece.getType() == PieceType.PAWN && movedPiece.getTeam() != selectedPiece.getTeam()) {
                        if (Math.abs(lastStart.getRow() - lastEnd.getRow()) == 2) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void resetSquareColors() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squarePanels[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
            }
        }
        // Sau khi reset vẫn giữ lại màu những ô chiếu
        checkForChecks();
    }

    public void updateBoard() {
        removeAll();
        initializeBoardUI();
        revalidate();
        repaint();
    }

    private void checkForChecks() {
        // Tìm vị trí vua
        Square kingSquare = board.findKingSquare(currentTurn);

        // Kiểm tra nếu vua đang bị chiếu
        if (Checkmate.kingInCheck(board, kingSquare)) {
            // Lấy những ô đang chiếu
            List<Square> checkingPieces = Checkmate.findCheckingPieces(board, kingSquare);

            // Cảnh báo chiếu
            squarePanels[kingSquare.getRow()][kingSquare.getCol()].setBackground(Color.RED);
            for (Square checkingPiece : checkingPieces) {
                squarePanels[checkingPiece.getRow()][checkingPiece.getCol()].setBackground(Color.RED);
            }
        }
    }

    private void checkForCheckmate() {
        Square kingSquare = board.findKingSquare(currentTurn);
        List<Square> checkingPieces = Checkmate.findCheckingPieces(board, kingSquare);

        // Kiểm tra nếu vua đang bị chiếu
        if (Checkmate.kingInCheck(board, kingSquare)) {
            boolean canBlockCheck = false;

            for (Square checkingPiece : checkingPieces) {
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        Square blockSquare = board.getSquare(row, col);
                        if (blockSquare.isOccupied() && blockSquare.getPiece().getTeam() == currentTurn) {
                            if (blockSquare.getPiece().canMove(board, blockSquare, checkingPiece) ||
                                    blockSquare.getPiece().canMove(board, blockSquare, kingSquare)) {
                                canBlockCheck = true;
                                break;
                            }
                        }
                    }
                    if (canBlockCheck) break;
                }
                if (canBlockCheck) break;
            }

            if (!canBlockCheck && !Checkmate.canKingEscape(board, kingSquare)) {
                // Nếu vua không thể thoát và không thể chặn quân chiếu, đó là chiếu hết
                String winner = (currentTurn == Team.WHITE) ? "Black" : "White";
                JOptionPane.showMessageDialog(this, winner + " wins by checkmate!");
            }
        }
    }

    private void handlePawnPromotion(Square endSquare) {
        if (endSquare.getPiece() instanceof Pawn && (endSquare.getRow() == 0 || endSquare.getRow() == 7)) {
            PromotionDialog promotionDialog = new PromotionDialog((JFrame) SwingUtilities.getWindowAncestor(this), currentTurn);
            promotionDialog.setVisible(true);

            Piece chosenPiece = promotionDialog.getChosenPiece();
            if (chosenPiece != null) {
                endSquare.setPiece(chosenPiece);
                updateBoard();
            } else {
                endSquare.setPiece(new Queen(currentTurn));
                updateBoard();
            }
        }
    }
}