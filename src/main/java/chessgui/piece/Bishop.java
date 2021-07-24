package chessgui.piece;

import chessgui.gui.Board;
import chessgui.piece.piece_logic.ValidateDestination;

public class Bishop implements Piece {
    private int x;
    private int y;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;

    public Bishop(int x, int y, boolean isWhite, String FILE_PATH, Board board) {
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
    public void setCol(int col) {
        this.x = col;
    }

    @Override
    public void setRow(int row) {
        this.y = row;
    }

    @Override
    public int getCol() {
        return x;
    }

    @Override
    public int getRow() {
        return y;
    }

    @Override
    public boolean canMove(int destinationX, int destinationY) {
        return ValidateDestination.isValidDiagonal(this, destinationX, destinationY)
                && ValidateDestination.isNotOccupiedByFriendly(this, destinationX, destinationY, BOARD)
                && ValidateDestination.isPathClear(this, destinationX, destinationY, BOARD);
    }

    @Override
    public Board getBoard() {
        return BOARD;
    }
}
