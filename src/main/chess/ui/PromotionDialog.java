package main.chess.ui;

import main.chess.game.Team;
import main.chess.game.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PromotionDialog extends JDialog {
    private Piece chosenPiece;

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

    private JButton createPieceButton(String pieceName, Piece piece) {
        JButton button = new JButton(pieceName);
        button.addActionListener(e -> {
            chosenPiece = piece;
            setVisible(false);
        });
        return button;
    }

    public Piece getChosenPiece() {
        return chosenPiece;
    }
}
