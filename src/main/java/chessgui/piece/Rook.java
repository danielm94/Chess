package chessgui.piece;

import chessgui.Board;
import chessgui.piece.piece_logic.ValidateDestination;

public class Rook implements Piece {
    private int x;
    private int y;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;
    private boolean hasMoved;

    public Rook(int x, int y, boolean isWhite, String FILE_PATH, Board board) {
        this.IS_WHITE = isWhite;
        this.x = x;
        this.y = y;
        this.FILE_PATH = FILE_PATH;
        this.BOARD = board;
    }

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    @Override
    public boolean isWhite() {
        return IS_WHITE;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public Board getBoard() {
        return BOARD;
    }

    @Override
    public boolean canMove(int destinationX, int destinationY) {
        return ValidateDestination.isValidStraightLine(this, destinationX, destinationY)
                && ValidateDestination.isNotOccupiedByFriendly(this, destinationX, destinationY, BOARD)
                && ValidateDestination.isPathClear(this, destinationX, destinationY, BOARD);
    }


    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
