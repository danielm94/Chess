package chessgui.piece;

import chessgui.gui.Board;
import chessgui.piece.piece_logic.ValidateDestination;

import java.util.HashSet;
import java.util.Set;

public class Knight implements Piece {
    private int row;
    private int col;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;
    private final Set<Piece> PIECES_PINNED_BY;

    public Knight(int row, int col, boolean isWhite, String FILE_PATH, Board board) {
        this.IS_WHITE = isWhite;
        this.row = row;
        this.col = col;
        this.FILE_PATH = FILE_PATH;
        this.BOARD = board;
        this.PIECES_PINNED_BY = new HashSet<>(16);
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
        this.col = col;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public boolean canMove(int destRow, int destCol) {
        return ValidateDestination.isNotOccupiedByFriendly(this, destRow, destCol, BOARD)
                && isValidKnightSquare(destRow, destCol)
                && getNumPiecesPinningThis() == 0;
    }

    public boolean isValidKnightSquare(int destRow, int destCol) {
        return ((Math.abs(destRow - row) == 2 && Math.abs(destCol - col) == 1)
                || (Math.abs(destRow - row) == 1 && Math.abs(destCol - col) == 2));
    }

    @Override
    public boolean addPieceThisIsPinnedBy(Piece piece) {
        return PIECES_PINNED_BY.add(piece);
    }

    @Override
    public boolean removePieceThisIsPinnedBy(Piece piece) {
        return PIECES_PINNED_BY.remove(piece);
    }

    @Override
    public boolean isPinnedBy(Piece piece) {
        return PIECES_PINNED_BY.contains(piece);
    }

    @Override
    public int getNumPiecesPinningThis() {
        return PIECES_PINNED_BY.size();
    }
}
