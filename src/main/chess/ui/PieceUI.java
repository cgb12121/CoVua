package main.chess.ui;

import main.chess.pieces.Piece;

import javax.swing.*;
import java.awt.*;


public class PieceUI extends JLabel {
    private static final int SQUARE_SIZE = 100;

    public PieceUI(Piece piece) {
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
