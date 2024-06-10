package main.chess.ui;

import main.chess.game.Team;
import main.chess.game.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Lớp PromotionDialog đại diện cho một hộp thoại để chọn quân cờ thăng cấp khi một quân tốt đạt đến cuối bàn cờ.
 */
public class PromotionDialog extends JDialog {
    private Piece chosenPiece;

    /**
     * Constructor của lớp PromotionDialog.
     *
     * @param parent Cửa sổ cha của hộp thoại.
     * @param team   Đội của người chơi cần thăng cấp quân cờ.
     */
    public PromotionDialog(JFrame parent, Team team) {
        super(parent, "Choose Promotion", true);
        setLayout(new GridLayout(1, 4));

        JButton queenButton = createPieceButton("Queen", new Queen(team));
        JButton rookButton = createPieceButton("Rook", new Rook(team));
        JButton bishopButton = createPieceButton("Bishop", new Bishop(team));
        JButton knightButton = createPieceButton("Knight", new Knight(team));

        add(queenButton);
        add(rookButton);
        add(bishopButton);
        add(knightButton);

        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Phương thức tạo nút cho một loại quân cờ.
     *
     * @param pieceName Tên của loại quân cờ.
     * @param piece     Quân cờ tương ứng.
     * @return Nút được tạo.
     */
    private JButton createPieceButton(String pieceName, Piece piece) {
        JButton button = new JButton(pieceName);
        button.addActionListener(e -> {
            chosenPiece = piece;
            setVisible(false);
        });
        return button;
    }

    /**
     * Phương thức lấy quân cờ được chọn cho thăng cấp.
     *
     * @return Quân cờ được chọn cho thăng cấp.
     */
    public Piece getChosenPiece() {
        return chosenPiece;
    }
}
