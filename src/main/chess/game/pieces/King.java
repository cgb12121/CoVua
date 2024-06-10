package main.chess.game.pieces;

import main.chess.game.board.Board;
import main.chess.game.board.Square;
import main.chess.game.Team;

import main.chess.game.Checkmate;

/**
 * Lớp đại diện cho quân Vua trong trò chơi cờ vua.
 */
public class King extends Piece {
    private boolean hasMoved;

    /**
     * Khởi tạo một quân Vua với đội tương ứng.
     *
     * @param team Đội của quân Vua
     */
    public King(Team team) {
        super(PieceType.KING,team);
        this.hasMoved = false;
    }

    /**
     * Kiểm tra xem quân Vua có thể di chuyển từ ô ban đầu đến ô kết thúc trên bàn cờ hay không.
     *
     * @param board Bàn cờ
     * @param start Ô ban đầu của quân Vua
     * @param end   Ô kết thúc của quân Vua
     * @return true nếu quân Vua có thể di chuyển, ngược lại trả về false
     */
    @Override
    public boolean canMove(Board board, Square start, Square end) {
        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        // Nếu ô đến làm cho vua bị chiếu return false
        if (Checkmate.checkByRook(board, start, end) || Checkmate.checkByKnight(board, start, end) || Checkmate.checkByPawn(board, start, end) ||
                Checkmate.checkByBishop(board, start, end) || Checkmate.checkByQueen(board, start, end) || Checkmate.checkByKing(board, start, end)) {
            return false;
        }

        // Nhập thành
        if (!hasMoved && (Math.abs(endY - startY) == 2 || Math.abs(endY - startY) == 3) && startX == endX) {
            if (isCastlingMove(board, start, end)) {
                return true;
            }
        }

        // Di chuyển bình thường hoặc ăn quân đối phương
        if (Math.abs(endX - startX) <= 1 && Math.abs(endY - startY) <= 1 &&
                (start.getPiece() == null || end.getPiece() == null || start.getPiece().getTeam() != end.getPiece().getTeam() || !end.isOccupied())) {
            return true;
        }

        return false; // Không có ô di chuyển hợp lệ
    }

    /**
     * Kiểm tra xem nước đi có phải là nhập thành hay không.
     *
     * @param board Bàn cờ
     * @param start Ô ban đầu của quân Vua
     * @param end   Ô kết thúc của quân Vua
     * @return true nếu nước đi là nhập thành, ngược lại trả về false
     */
    private boolean isCastlingMove(Board board, Square start, Square end) {
        int row = start.getRow();
        int colDiff = end.getCol() - start.getCol();
        boolean isKingSideCastling = colDiff == 2;
        boolean isQueenSideCastling = colDiff == -2;

        if (!isKingSideCastling && !isQueenSideCastling) {
            return false;
        }

        // Kiểm tra điều kiện quân xe phù hợp để nhập thành
        // Hàng 7 cho trắng, 0 cho đen và chưa di chuyển
        int rookCol = isKingSideCastling ? 7 : 0;
        Square rookSquare = board.getSquare(row, rookCol);
        if (rookSquare == null || rookSquare.getPiece() == null || rookSquare.getPiece().getType() != PieceType.ROOK || rookSquare.getPiece().hasMoved()) {
            return false;
        }

        // Kiểm tra giữa vua và xe có bị chặn
        int colStep = isKingSideCastling ? 1 : -1;
        for (int col = start.getCol() + colStep; col != rookCol; col += colStep) {
            if (board.getSquare(row, col).isOccupied()) {
                return false;
            }
        }

        return true; // Đủ điều kiện tiến hành nhập thành
    }

    /**
     * Di chuyển quân Vua và cập nhật trạng thái đã di chuyển.
     *
     * @param start Ô ban đầu của quân Vua
     * @param end   Ô kết thúc của quân Vua
     */
    @Override
    public void move(Square start, Square end) {
        super.move(start, end);
        hasMoved = true;
    }
    /**
     * Trả về trạng thái di chuyển của quân Vua.
     *
     * @return true nếu quân Vua đã di chuyển, ngược lại trả về false
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Trả về tên file biểu tượng của quân Vua.
     *
     * @return Tên file biểu tượng của quân Vua
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_King.png";
        } else {
            return "src/main/resources/Black/Black_King.png";
        }
    }
}
