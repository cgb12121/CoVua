package main.chess.ui;

import main.chess.game.Checkmate;
import main.chess.game.Team;
import main.chess.game.ai.AI;
import main.chess.game.board.Board;
import main.chess.game.board.Move;
import main.chess.game.board.Square;
import main.chess.game.pieces.Pawn;
import main.chess.game.pieces.Piece;
import main.chess.game.pieces.PieceType;
import main.chess.game.pieces.Queen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.List;

/**
 * Lớp đại diện cho giao diện người dùng của trò chơi cờ vua.
 */
public class ChessBoardUI extends JPanel {
    private Board board;
    private JPanel[][] squarePanels;
    private Square selectedSquare;
    private List<Square> movableSquares;
    private Team currentTurn;
    private boolean gameOver;
    private AI ai;

    public static boolean isAIturn = false;

    /**
     * Khởi tạo một bảng cờ vua với bàn cờ đã cho.
     *
     * @param board Bàn cờ vua để hiển thị trong giao diện người dùng.
     */
    public ChessBoardUI(Board board) {
        this.board = board;
        this.squarePanels = new JPanel[8][8];
        this.currentTurn = Team.WHITE; // Lượt đầu mặc định là trắng
        this.gameOver = false;
        this.ai = new AI(board, squarePanels);

        String[] options = {"Human vs Human", "Human vs AI"};
        int choice = JOptionPane.showOptionDialog(null, "Select Game Mode", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        //TODO: highlight lượt di chuyển cuối
        if (choice == 0) {
            //Human vs Human
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

        //TODO: fix lỗi chỉ hiện chiếu khi click ngẫu nhiên lên bàn cờ
        //      highlight lượt di chuyển cuối của AI
        if (choice == 1) {
            // Human vs AI
            setLayout(new GridLayout(8, 8));
            initializeBoardUI();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentTurn == Team.WHITE && !gameOver) { //Trắng là người chơi
                        int row = e.getY() / (getHeight() / 8);
                        int col = e.getX() / (getWidth() / 8);
                        Square clickedSquare = board.getSquare(row, col);

                        if (selectedSquare == null) {
                            // Chọn ô, nếu sai thì chọn lại
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
                                //Di chuyển hoặc ăn khi bước đi hợp lệ
                                boolean moveSuccessful = board.movePiece(selectedSquare, clickedSquare);
                                if (moveSuccessful) {
                                    if (currentTurn == Team.WHITE) { //Chi nguoi choi duoc chon promotion
                                        handlePawnPromotion(clickedSquare);
                                    }
                                    updateBoard();
                                    resetSquareColors();
                                    checkForCheckmate();
                                    selectedSquare = null;
                                    movableSquares = null;
                                    if (!gameOver) {
                                        isAIturn = true;
                                        currentTurn = Team.BLACK;
                                        // AI move trong thread mới
                                        new Thread(() -> {
                                            ai.makeAIMove(currentTurn); // lượt AI
                                            SwingUtilities.invokeLater(() -> {
                                                updateBoard();
                                                resetSquareColors();
                                                checkForCheckmate();
                                                if (!gameOver) {
                                                    currentTurn = Team.WHITE;
                                                    isAIturn = false;
                                                }
                                            });
                                        }).start();
                                    }
                                }
                            } else {
                                // Hủy chọn các ô khi bước đi không hợp lệ
                                selectedSquare = null;
                                movableSquares = null;
                                resetSquareColors();
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * Khởi tạo giao diện bàn cờ với các ô và quân cờ tương ứng.
     */
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

    /**
     * Hiển thị biểu tượng của quân cờ trên ô được chỉ định trên bàn cờ.
     *
     * @param square Ô trên bàn cờ chứa quân cờ cần hiển thị.
     */
    private void displayPiece(Square square) {
        if (square.isOccupied()) {
            Piece piece = square.getPiece();
            PieceIcon pieceIcon = new PieceIcon(piece);
            JPanel squarePanel = squarePanels[square.getRow()][square.getCol()];

            // Thiết lập layout và các ràng buộc cho ô chứa quân cờ
            squarePanel.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.CENTER;

            // Xóa các thành phần hiện có trên ô và thêm biểu tượng của quân cờ
            squarePanel.removeAll();
            squarePanel.add(pieceIcon);
            squarePanel.revalidate();
            squarePanel.repaint();
        }
    }

    /**
     * Tô màu các ô có thể di chuyển được trên bàn cờ.
     * Các ô có thể di chuyển được sẽ được tô màu xanh.
     * Các ô chứa quân cờ địch có thể bị tấn công sẽ được tô màu cam.
     * Các ô en passant có thể bị tấn công cũng được tô màu cam.
     */
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

    /**
     * Kiểm tra xem một nước đi có phải là bắt quân en passant hay không.
     *
     * @param endSquare Ô kết thúc của nước đi
     * @return true nếu nước đi là bắt quân en passant, ngược lại trả về false
     */
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

    /**
     * Thiết lập lại màu của các ô trên bàn cờ.
     * Các ô có màu trắng và xám xen kẽ.
     * Các ô được giữ màu đỏ nếu có vua bị chiếu.
     */
    private void resetSquareColors() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squarePanels[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
            }
        }
        // Sau khi reset vẫn giữ lại màu những ô chiếu
        checkForChecks();
    }

    /**
     * Cập nhật giao diện của bàn cờ sau mỗi nước đi.
     * Gọi lại phương thức để vẽ lại các quân cờ trên bàn cờ và kiểm tra tình trạng chiếu.
     */
    public void updateBoard() {
        removeAll();
        initializeBoardUI();
        revalidate();
        repaint();
        checkForChecks();
    }

    /**
     * Kiểm tra và cập nhật các ô trên bàn cờ nếu vua đang bị chiếu.
     * Các ô có quân tấn công vua sẽ được làm nổi bật (màu đỏ).
     */
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

    /**
     * Kiểm tra xem có chiếu hết hay không và hiển thị thông báo nếu có.
     * Nếu vua không thể di chuyển và không thể chặn quân tấn công, hiển thị thông báo chiếu hết và kết thúc trò chơi.
     */
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
                JOptionPane.showMessageDialog(this, winner + " Thắng!");
                gameOver = true;
            }
        }
    }

    /**
     * Phong tốt
     *
     * @param endSquare Ô mà con tốt sẽ đến khi hoàn thành việc cải biến.
     */
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