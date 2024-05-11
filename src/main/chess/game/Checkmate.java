package main.chess.game;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.pieces.PieceType;

public class Checkmate {

    public static boolean kingInCheck(Board board, Square start, Square end) {
        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        if (startX == endX && startY == endY) {

            if (checkByPawn(board, start, end)) {
                return true;
            }

            if (checkByKnight(board, start, end)) {
                return true;
            }

            if (checkByRook(board, start, end)) {
                return true;
            }

            if (checkByBishop(board, start, end)) {
                return true;
            }

            if (checkByQueen(board, start, end)) {
                return true;
            }

            if (checkByKing(board, start, end)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkByPawn(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();
        int numRows = 8;
        int numCols = 8;

        if (start.getPiece().getTeam() == Team.WHITE){
            int[][] possibleMoves = {{-1,1}, {-1,-1}};
            for (int[] move : possibleMoves){
                int newX = endX + move[0];
                int newY = endY + move[1];

                if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                    Square targetSquare = board.getSquare(newX, newY);
                    if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.PAWN &&
                            targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                        return true;
                    }
                }
            }
        }

        if (start.getPiece().getTeam() == Team.BLACK){
            int[][] possibleMoves = {{1,1}, {1,-1}};
            for (int[] move : possibleMoves){
                int newX = endX + move[0];
                int newY = endY + move[1];

                if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                    Square targetSquare = board.getSquare(newX, newY);
                    if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.PAWN &&
                            targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean checkByKnight(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();
        int numRows = 8;
        int numCols = 8;

        int[][] possibleMoves = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] move : possibleMoves) {
            int newX = endX + move[0];
            int newY = endY + move[1];

            if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                Square targetSquare = board.getSquare(newX, newY);
                if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.KNIGHT &&
                        targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean checkByRook(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getSquare(i, j) != null && board.getSquare(i, j).getPiece() != null &&
                        board.getSquare(i, j).getPiece().getType() == PieceType.ROOK &&
                        board.getSquare(i, j).getPiece().getTeam() != start.getPiece().getTeam()) {
                    if (i == endX || j == endY) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkByBishop(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getSquare(i, j) != null && board.getSquare(i, j).getPiece() != null &&
                        board.getSquare(i, j).getPiece().getType() == PieceType.BISHOP &&
                        board.getSquare(i, j).getPiece().getTeam() != start.getPiece().getTeam()) {
                    if (Math.abs(i - endX) == Math.abs(j - endY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkByQueen(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getSquare(i, j) != null && board.getSquare(i, j).getPiece() != null &&
                        board.getSquare(i, j).getPiece().getType() == PieceType.QUEEN &&
                        board.getSquare(i, j).getPiece().getTeam() != start.getPiece().getTeam()) {
                    if (i == endX || j == endY) {
                        return true;
                    }
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getSquare(i, j) != null && board.getSquare(i, j).getPiece() != null &&
                        board.getSquare(i, j).getPiece().getType() == PieceType.QUEEN &&
                        board.getSquare(i, j).getPiece().getTeam() != start.getPiece().getTeam()) {
                    if (Math.abs(i - endX) == Math.abs(j - endY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkByKing(Board board, Square start, Square end) {
        int endX = end.getRow();
        int endY = end.getCol();
        int numRows = 8;
        int numCols = 8;

        int[][] possibleMoves = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1}, {1, 0},  {1, 1}
        };

        for (int[] move : possibleMoves) {
            int newX = endX + move[0];
            int newY = endY + move[1];

            if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols) {
                Square targetSquare = board.getSquare(newX, newY);
                if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.KING &&
                        targetSquare.getPiece().getTeam() != start.getPiece().getTeam()) {
                    return true;
                }
            }
        }
        return false;
    }
}