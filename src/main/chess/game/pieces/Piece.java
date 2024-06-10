package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

/**
 * Lớp trừu tượng đại diện cho một quân cờ trong trò chơi cờ vua.
 */
public abstract class Piece {
    private PieceType pieceType;
    private Team team;
    private boolean hasMoved;

    /**
     * Khởi tạo một quân cờ với loại và đội tương ứng.
     *
     * @param pieceType Loại quân cờ
     * @param team      Đội của quân cờ
     */
    public Piece(PieceType pieceType, Team team) {
        this.pieceType = pieceType;
        this.team = team;
        this.hasMoved = false;
    }

    /**
     * Trả về đội của quân cờ.
     *
     * @return Đội của quân cờ
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Đặt đội cho quân cờ.
     *
     * @param team Đội của quân cờ
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Trả về loại của quân cờ.
     *
     * @return Loại của quân cờ
     */
    public PieceType getType() {
        return pieceType;
    }

    /**
     * Kiểm tra xem quân cờ đã được di chuyển hay chưa.
     *
     * @return true nếu quân cờ đã được di chuyển, ngược lại trả về false
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Ghi nhận việc di chuyển của quân cờ.
     *
     * @param start Ô ban đầu của quân cờ
     * @param end   Ô kết thúc của quân cờ
     */
    public void move(Square start, Square end) {
        this.hasMoved = true;
    }

    /**
     * Trả về tên file biểu tượng của quân cờ.
     *
     * @return Tên file biểu tượng của quân cờ
     */
    public abstract String getIconFileName();

    /**
     * Kiểm tra xem quân cờ có thể di chuyển từ ô ban đầu đến ô kết thúc trên bàn cờ hay không.
     *
     * @param board Bàn cờ
     * @param start Ô ban đầu của quân cờ
     * @param end   Ô kết thúc của quân cờ
     * @return true nếu quân cờ có thể di chuyển, ngược lại trả về false
     */
    public abstract boolean canMove(Board board, Square start, Square end);
}
