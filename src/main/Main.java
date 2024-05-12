package main;

import main.chess.game.ChessGame;
import main.chess.ui.ChessBoardUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        MemoryUsageReporter memoryReporter = new MemoryUsageReporter();

        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChessBoardUI chessBoardUI = new ChessBoardUI(game.getBoard());
        frame.add(chessBoardUI);

        frame.pack();
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while (game.isActive()) {

            memoryReporter.reportMemoryUsage();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
