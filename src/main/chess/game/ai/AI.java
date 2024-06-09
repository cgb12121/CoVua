package main.chess.game.ai;

import main.chess.game.Team;
import main.chess.game.board.Board;
import main.chess.game.board.Move;
import main.chess.game.board.Square;
import main.chess.game.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static main.chess.game.ai.PiecePositionValue.*;
import static main.chess.game.ai.PieceValue.*;


public class AI {
    private Board board;
    private JPanel[][] squarePanels;

    public AI(Board board, JPanel[][] squarePanels) {
        this.board = board;
        this.squarePanels = squarePanels;
    }

    public List<Move> getAllValidMoves(Team team) {
        List<Move> validMoves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square startSquare = board.getSquare(row, col);
                if (startSquare.isOccupied() && startSquare.getPiece().getTeam() == team) {
                    List<Square> movableSquares = board.highlightMovableSquares(startSquare);
                    for (Square endSquare : movableSquares) {
                        validMoves.add(new Move(startSquare, endSquare));
                    }
                }
            }
        }

        return validMoves;
    }

    public Move findBestMove(Team team, int depth) {
        Move bestMove = null;
        int bestValue = team == Team.WHITE ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        List<Move> allPossibleMoves = getAllValidMoves(team);
        for (Move move : allPossibleMoves) {
            Square start = move.getStart();
            Square end = move.getEnd();
            Piece movedPiece = movePieceTemporarily(start, end);

            int boardValue = minimax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, team == Team.BLACK);

            undoMove(start, end, movedPiece);
            if (team == Team.WHITE && boardValue > bestValue) {
                bestValue = boardValue;
                bestMove = move;
            } else if (team == Team.BLACK && boardValue < bestValue) {
                bestValue = boardValue;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0) {
            return evaluateBoard();
        }

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : getAllValidMoves(Team.WHITE)) {
                Square start = move.getStart();
                Square end = move.getEnd();
                Piece movedPiece = movePieceTemporarily(start, end);

                int eval = minimax(depth - 1, alpha, beta, false);
                undoMove(start, end, movedPiece);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : getAllValidMoves(Team.BLACK)) {
                Square start = move.getStart();
                Square end = move.getEnd();
                Piece movedPiece = movePieceTemporarily(start, end);

                int eval = minimax(depth - 1, alpha, beta, true);
                undoMove(start, end, movedPiece);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }

    private int getPieceValue(Piece piece) {
        switch (piece.getType()) {
            case PAWN:
                return PAWN_VALUE;
            case KNIGHT:
                return KNIGHT_VALUE;
            case BISHOP:
                return BISHOP_VALUE;
            case ROOK:
                return ROOK_VALUE;
            case QUEEN:
                return QUEEN_VALUE;
            case KING:
                return KING_VALUE;
            default:
                throw new IllegalArgumentException("Unknown piece type: " + piece.getType());
        }
    }

    private int getPositionalValue(Piece piece, Square square) {
        int row = square.getRow();
        int col = square.getCol();
        switch (piece.getType()) {
            case PAWN:
                return piece.getTeam() == Team.WHITE ? PAWN_TABLE[row][col] : PAWN_TABLE[7 - row][col];
            case KNIGHT:
                return piece.getTeam() == Team.WHITE ? KNIGHT_TABLE[row][col] : KNIGHT_TABLE[7 - row][col];
            case BISHOP:
                return piece.getTeam() == Team.WHITE ? BISHOP_TABLE[row][col] : BISHOP_TABLE[7 - row][col];
            case ROOK:
                return piece.getTeam() == Team.WHITE ? ROOK_TABLE[row][col] : ROOK_TABLE[7 - row][col];
            case QUEEN:
                return piece.getTeam() == Team.WHITE ? QUEEN_TABLE[row][col] : QUEEN_TABLE[7 - row][col];
            case KING:
                return piece.getTeam() == Team.WHITE ? KING_TABLE[row][col] : KING_TABLE[7 - row][col];
            default:
                throw new IllegalArgumentException("Unknown piece type: " + piece.getType());
        }
    }

    private int evaluateBoard() {
        int evaluation = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = board.getSquare(row, col);
                if (square.isOccupied()) {
                    Piece piece = square.getPiece();
                    int pieceValue = getPieceValue(piece);
                    int positionalValue = getPositionalValue(piece, square);
                    int totalValue = pieceValue + positionalValue;
                    if (piece.getTeam() == Team.WHITE) {
                        evaluation += totalValue;
                    } else {
                        evaluation -= totalValue;
                    }
                }
            }
        }

        int whiteMobility = getMobility(Team.WHITE);
        int blackMobility = getMobility(Team.BLACK);
        evaluation += whiteMobility - blackMobility;

        int whiteControl = getControlOfKeySquares(Team.WHITE);
        int blackControl = getControlOfKeySquares(Team.BLACK);
        evaluation += whiteControl - blackControl;

        int whiteKingSafety = getKingSafety(Team.WHITE);
        int blackKingSafety = getKingSafety(Team.BLACK);
        evaluation += whiteKingSafety - blackKingSafety;

        return evaluation;
    }

    private int getMobility(Team team) {
        int mobility;
        List<Move> allMoves = getAllValidMoves(team);
        mobility = allMoves.size();
        return mobility;
    }

    private int getControlOfKeySquares(Team team) {
        int control = 0;
        int[][] keySquares = {
                {3, 3}, {3, 4}, {4, 3}, {4, 4}
        };
        for (int[] keySquare : keySquares) {
            Square square = board.getSquare(keySquare[0], keySquare[1]);
            if (square.isOccupied() && square.getPiece().getTeam() == team) {
                control += 10;
            }
        }
        return control;
    }

    private int getKingSafety(Team team) {
        int kingSafety = 0;
        Square kingSquare = board.findKingSquare(team);
        if (kingSquare != null) {
            int row = kingSquare.getRow();
            int col = kingSquare.getCol();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                        Square adjacentSquare = board.getSquare(newRow, newCol);
                        if (!adjacentSquare.isOccupied() || adjacentSquare.getPiece().getTeam() == team) {
                            kingSafety += 1;
                        }
                    }
                }
            }
        }
        return kingSafety;
    }


    private Piece movePieceTemporarily(Square start, Square end) {
        Piece piece = start.getPiece();
        Piece capturedPiece = end.getPiece();
        end.setPiece(piece);
        start.setPiece(null);
        return capturedPiece;
    }

    private void undoMove(Square start, Square end, Piece capturedPiece) {
        Piece piece = end.getPiece();
        start.setPiece(piece);
        end.setPiece(capturedPiece);
    }

    public void makeAIMove(Team team) {
        Move bestMove = findBestMove(team, 3);

        if (bestMove != null) {
            Square start = bestMove.getStart();
            Square end = bestMove.getEnd();
            board.movePiece(start, end);
            squarePanels[start.getRow()][start.getCol()].setBackground(Color.BLUE);
            squarePanels[end.getRow()][end.getCol()].setBackground(Color.BLUE);
        }
    }
}
