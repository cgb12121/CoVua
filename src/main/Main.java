package main;

import main.chess.game.ChessGame;
import main.chess.ui.ChessBoardUI;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        ChessGame game = new ChessGame();

        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChessBoardUI chessBoardUI = new ChessBoardUI(game.getBoard());
        frame.add(chessBoardUI);

        frame.pack();
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
