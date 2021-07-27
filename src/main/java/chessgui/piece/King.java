package chessgui.piece;

import chessgui.gui.AttackerSquare;
import chessgui.gui.Board;
import chessgui.piece.piece_logic.ValidateDestination;

import java.util.HashSet;
import java.util.Set;

public class King implements Piece {
    private int row;
    private int col;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;
    private boolean hasMoved;
    private final Set<Piece> PIECES_PINNED_BY;

    public King(int row, int col, boolean IS_WHITE, String FILE_PATH, Board board) {
        this.IS_WHITE = IS_WHITE;
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
      /*  return ValidateDestination.isNotOccupiedByFriendly(this, destRow, destCol, BOARD)
                && ((Math.abs(destRow - row) == 1 && destCol == col || Math.abs(destCol - col) == 1 && destRow == row) // is valid vertical/horizontal square (1 square distance)
                || (Math.abs(destRow - row) == 1 && Math.abs(destCol - col) == 1)); // is valid diagonal square (1 square distance)*/
        AttackerSquare destSquare = BOARD.getAttackerMap().getSquare(destRow, destCol);
        int enemiesAttackingDest = IS_WHITE ? destSquare.getBlackCount() : destSquare.getWhiteCount();
        return destSquare.containsAttacker(this)
                && enemiesAttackingDest == 0
                && ValidateDestination.isNotOccupiedByFriendly(this, destRow, destCol, BOARD);
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

    public boolean canCastle(int destRow, int destCol) {
        if (hasMoved) return false;
        if ((destCol == 1 || destCol == 2 || destCol == 6)
                && ((isWhite() && destRow == 0) || (!isWhite() && destRow == 7))) {
            int rookCol = destCol > col ? 7 : 0;
            Piece pieceToCastle = BOARD.getPiece(destRow, rookCol);

            if (pieceToCastle == null //If is an empty square
                    || !pieceToCastle.getClass().equals(Rook.class)) { //If the piece occupying the square is not a rook.
                return false;
            }
            //Now we know that a rook is occupying the square, we can cast the piece as a rook.
            //TODO:ADD IS KING UNDER CHECK VALIDATION
            Rook rookToCastle = (Rook) pieceToCastle;
            return rookToCastle.isWhite() == isWhite()
                    && !rookToCastle.hasMoved()
                    && ValidateDestination.isPathClear(this, destRow, rookCol, BOARD);
        } else return false;
    }

    public boolean isChecked() {
        AttackerSquare kingSquare = BOARD.getAttackerMap().getSquare(row, col);
        return (kingSquare.getWhiteCount() > 0 && !IS_WHITE)
                || (kingSquare.getBlackCount() > 0 && IS_WHITE)
                || PIECES_PINNED_BY.size() > 0;
    }
}
