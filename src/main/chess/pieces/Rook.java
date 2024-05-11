package main.chess.pieces;

import main.chess.board.Board;
import main.chess.board.Square;
import main.chess.game.Team;

// Lớp Rook đại diện cho quân Xe trong trò chơi cờ vua
public class Rook extends Piece {
    private boolean rookHasMoved = false; // Biến kiểm tra xem quân Xe đã di chuyển chưa

    // Constructor để khởi tạo một quân Xe với đội của nó
    public Rook(Team team) {
        super(team);
    }

    // Phương thức trả về trạng thái di chuyển của quân Xe
    public boolean rookHasMoved() {
        return this.rookHasMoved;
    }

    // Phương thức để đặt trạng thái di chuyển của quân Xe
    public void setRookHasMoved(boolean hasMoved) {
        this.rookHasMoved = hasMoved;
    }

    @Override
    // Phương thức kiểm tra xem quân Xe có thể di chuyển từ ô bắt đầu đến ô kết thúc không
    public boolean canMove(Board board, Square start, Square end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Nếu ô kết thúc đã được chiếm
        if (end.isOccupied()) {
            // Nếu quân cờ tại ô kết thúc thuộc đội đối phương
            if (start.getPiece().getTeam() != end.getPiece().getTeam()) {
                // Nếu quân Xe di chuyển theo hàng ngang
                if (startRow == endRow && startCol != endCol) {
                    int minCol = Math.min(startCol, endCol);
                    int maxCol = Math.max(startCol, endCol);
                    // Kiểm tra các ô giữa ô bắt đầu và ô kết thúc trên cùng một hàng ngang
                    for (int col = minCol + 1; col < maxCol; col++) {
                        if (board.getSquare(startRow, col).isOccupied()) {
                            return false;
                        }
                    }
                    return true;
                }
                // Nếu quân Xe di chuyển theo hàng dọc
                else if (startRow != endRow && startCol == endCol) {
                    int minRow = Math.min(startRow, endRow);
                    int maxRow = Math.max(startRow, endRow);
                    // Kiểm tra các ô giữa ô bắt đầu và ô kết thúc trên cùng một hàng dọc
                    for (int row = minRow + 1; row < maxRow; row++) {
                        if (board.getSquare(row, startCol).isOccupied()) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        // Nếu ô kết thúc chưa được chiếm
        else {
            // Nếu quân Xe di chuyển theo hàng ngang
            if (startRow == endRow && startCol != endCol) {
                int minCol = Math.min(startCol, endCol);
                int maxCol = Math.max(startCol, endCol);
                // Kiểm tra các ô giữa ô bắt đầu và ô kết thúc trên cùng một hàng ngang
                for (int col = minCol + 1; col < maxCol; col++) {
                    if (board.getSquare(startRow, col).isOccupied()) {
                        return false;
                    }
                }
                return true;
            }
            // Nếu quân Xe di chuyển theo hàng dọc
            else if (startRow != endRow && startCol == endCol) {
                int minRow = Math.min(startRow, endRow);
                int maxRow = Math.max(startRow, endRow);
                // Kiểm tra các ô giữa ô bắt đầu và ô kết thúc trên cùng một hàng dọc
                for (int row = minRow + 1; row < maxRow; row++) {
                    if (board.getSquare(row, startCol).isOccupied()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false; // Trả về false nếu không thỏa mãn điều kiện nào
    }

    // Phương thức trả về tên file hình ảnh của quân Xe
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.WHITE){
            return "src/main/resources/White/White_Rook.png";
        } else {
            return "src/main/resources/Black/Black_Rook.png";
        }
    }
}
