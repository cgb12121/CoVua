package main.chess.ui;

import javax.swing.JOptionPane;

public class EndGameUI {
    public static void displayEndGameMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
}
