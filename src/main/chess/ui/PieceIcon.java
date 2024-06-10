package main.chess.ui;

import main.chess.game.pieces.Piece;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;

/**
 * Lớp PieceIcon đại diện cho một biểu tượng hiển thị một quân cờ.
 */
public class PieceIcon extends JLabel {
    private static final int SQUARE_SIZE = 100;

    /**
     * Constructor của lớp PieceIcon.
     *
     * @param piece Quân cờ mà biểu tượng sẽ hiển thị.
     */
    public PieceIcon(Piece piece) {
        if (piece != null) {
            String iconFileName = piece.getIconFileName();
            ImageIcon originalIcon = new ImageIcon(iconFileName);
            Image originalImage = originalIcon.getImage();

            Image scaledImage = originalImage.getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);

            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            setIcon(scaledIcon);
        }
    }
}
