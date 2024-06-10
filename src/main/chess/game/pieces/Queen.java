package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

/**
 * Lớp đại diện cho quân Hậu trong trò chơi cờ vua.
 */
public class Queen extends Piece {
    private Rook rook;
    private Bishop bishop;

    /**
     * Tạo một quân hậu thuộc một đội nhất định.
     * Hậu kết hợp các khả năng di chuyển của xe và tượng.
     *
     * @param team Đội của quân hậu
     */    public Queen(Team team) {
        super(PieceType.QUEEN,team);
        this.rook = new Rook(team);
        this.bishop = new Bishop(team);
    }

    /**
     * Phương thức kiểm tra khả năng di chuyển của quân hậu trên bàn cờ.
     *
     * @param board Bàn cờ hiện tại.
     * @param start Ô bắt đầu của quân hậu.
     * @param end   Ô kết thúc của quân hậu.
     * @return true nếu quân hậu có thể di chuyển đến ô kết thúc, ngược lại trả về false.
     */    @Override
    public boolean canMove(Board board, Square start, Square end) {
        return rook.canMove(board, start, end) || bishop.canMove(board, start, end);
    }

    /**
     * Trả về tên file biểu tượng của quân Hậu.
     *
     * @return Tên file biểu tượng của quân Hậu
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Queen.png";
        } else {
            return "src/main/resources/Black/Black_Queen.png";
        }
    }
}
