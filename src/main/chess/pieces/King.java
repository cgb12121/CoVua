package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Checkmate;
import main.chess.game.Team;

// Lớp King đại diện cho quân Vua trong trò chơi cờ vua
public class King extends Piece {
    private boolean castlingDone = false; // Biến kiểm tra xem có thực hiện castling không
    private boolean kingHasMoved = false; // Biến kiểm tra xem Vua đã di chuyển chưa
    private boolean rookHasMoved = false; // Biến kiểm tra xem Xe đã di chuyển chưa

    // Constructor để khởi tạo một quân Vua với đội của nó
    public King(Team team) {
        super(team);
    }

    // Phương thức kiểm tra xem Xe đã di chuyển chưa
    public boolean isRookMoved() {
        return this.rookHasMoved;
    }

    // Phương thức để đặt trạng thái Xe đã di chuyển hay chưa
    public void setRookMoved(boolean rookHasMoved) {
        this.rookHasMoved = rookHasMoved;
    }

    // Phương thức kiểm tra xem castling đã thực hiện chưa
    public boolean isCastlingDone() {
        return this.castlingDone;
    }

    // Phương thức để đặt trạng thái castling
    public void setCastlingDone(boolean castlingDone) {
        this.castlingDone = castlingDone;
    }

    @Override
    // Phương thức kiểm tra xem quân Vua có thể di chuyển từ ô bắt đầu đến ô kết thúc không
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Kiểm tra xem ô kết thúc có chứa quân cờ của cùng đội không
        if (end.getPiece() != null && end.getPiece().getTeam() == this.getTeam()) {
            return false;
        }

        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        // Kiểm tra xem quân Vua có di chuyển đúng 1 ô không
        if ((rowDiff <= 1 && colDiff <= 1) && !(rowDiff == 0 && colDiff == 0)) {
            if (Checkmate.isCheckmate(board, getTeam())) {
                return false;
            }
            kingHasMoved = true; // Đặt trạng thái Vua đã di chuyển
            return true;
        }

        // Kiểm tra xem quân Vua có thực hiện castling không
        if (!kingHasMoved && !castlingDone &&
                startRow == endRow && Math.abs(startCol - endCol) == 2 && (startRow == 0 || startRow == 7)) {
            kingHasMoved = true; // Đặt trạng thái Vua đã di chuyển
            setCastlingDone(true); // Đặt trạng thái castling đã thực hiện
            return true;
        } else if (!kingHasMoved && !castlingDone &&
                startRow == endRow && Math.abs(startCol - endCol) == 3 && (startRow == 0 || startRow == 7)){
            kingHasMoved = true; // Đặt trạng thái Vua đã di chuyển
            setCastlingDone(true); // Đặt trạng thái castling đã thực hiện
            return true;
        }
        return false; // Trả về false nếu không thỏa mãn điều kiện nào
    }

    // Phương thức trả về tên file hình ảnh của quân Vua
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_King.png";
        } else {
            return "src/main/resources/Black/Black_King.png";
        }
    }
}
