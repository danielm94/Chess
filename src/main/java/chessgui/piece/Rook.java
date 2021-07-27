package chessgui.piece;

import chessgui.gui.Board;
import chessgui.piece.piece_logic.ValidateDestination;

import java.util.HashSet;
import java.util.Set;

public class Rook implements PinPiece {
    private int row;
    private int col;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;
    private boolean hasMoved;
    private final Set<Piece> PIECES_PINNED_BY;
    private final Set<Piece> PIECES_PINNING;

    public Rook(int row, int col, boolean isWhite, String FILE_PATH, Board board) {
        this.IS_WHITE = isWhite;
        this.row = row;
        this.col = col;
        this.FILE_PATH = FILE_PATH;
        this.BOARD = board;
        this.PIECES_PINNED_BY = new HashSet<>(16);
        this.PIECES_PINNING = new HashSet<>(16);
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
        return ValidateDestination.isValidStraightLine(this, destRow, destCol)
                && getNumPiecesPinningThis() == 0
                && ValidateDestination.isNotOccupiedByFriendly(this, destRow, destCol, BOARD)
                && ValidateDestination.isPathClear(this, destRow, destCol, BOARD);
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

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean addPieceThisIsPinning(Piece piece) {
        return PIECES_PINNING.add(piece);
    }

    public boolean removePieceThisIsPinning(Piece piece) {
        return PIECES_PINNING.remove(piece);
    }

    public boolean isPinning(Piece piece) {
        return PIECES_PINNING.contains(piece);
    }

    public int getNumPiecesPinned() {
        return PIECES_PINNING.size();
    }

}
