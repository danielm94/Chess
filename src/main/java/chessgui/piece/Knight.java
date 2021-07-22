package chessgui.piece;

import chessgui.Board;
import chessgui.piece.piece_logic.ValidateDestination;

public class Knight implements Piece {
    private int x;
    private int y;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;

    public Knight(int x, int y, boolean isWhite, String FILE_PATH, Board board) {
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
    public boolean canMove(int destinationX, int destinationY) {
        return ValidateDestination.isNotOccupiedByFriendly(this, destinationX, destinationY, BOARD)
                && ((Math.abs(destinationX - x) == 2 && Math.abs(destinationY - y) == 1)
                || (Math.abs(destinationX - x) == 1 && Math.abs(destinationY - y) == 2));
    }

    @Override
    public Board getBoard() {
        return BOARD;
    }
}
