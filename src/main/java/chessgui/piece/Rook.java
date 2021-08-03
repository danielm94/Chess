package chessgui.piece;

import chessgui.board.AttackerMap;
import chessgui.board.AttackerSquare;
import chessgui.board.Board;
import chessgui.board.Helper;

import java.util.Set;

public class Rook implements PinPiece {
    private int row;
    private int col;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private boolean canCastle;
    private Piece pieceThisIsPinnedBy;
    private Piece pieceThisIsPinning;

    public Rook(int row, int col, boolean isWhite, String FILE_PATH) {
        this.canCastle = false;
        this.IS_WHITE = isWhite;
        this.row = row;
        this.col = col;
        this.FILE_PATH = FILE_PATH;
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
        if (Board.get().inStateOfCheck()) {
            Set<AttackerSquare> squaresToBlockOrCapture = Helper.getSquaresToBlockOrCapture(IS_WHITE, Board.get());
            AttackerSquare destinationSquare = AttackerMap.get().getSquare(destRow, destCol);
            return !this.isPinned()
                    && destinationSquare.containsAttacker(this)
                    && squaresToBlockOrCapture.contains(destinationSquare);
        } else {
            if (AttackerMap.get().getSquare(destRow, destCol).containsAttacker(this) &&
                    Helper.isNotOccupiedByFriendly(this, destRow, destCol)) {
                if (this.isPinned()) {
                    if (pieceThisIsPinnedBy.getRow() == row)
                        return destRow == pieceThisIsPinnedBy.getRow();
                    else if (pieceThisIsPinnedBy.getCol() == col)
                        return destCol == pieceThisIsPinnedBy.getCol();
                } else return true;
            }
            return false;
        }
    }

    @Override
    public void setPieceThisIsPinnedBy(Piece piece) {
        pieceThisIsPinnedBy = piece;
    }

    @Override
    public void clearPieceThisIsPinnedBy() {
        pieceThisIsPinnedBy = null;
    }

    @Override
    public boolean isPinnedBy(Piece piece) {
        return pieceThisIsPinnedBy == piece;
    }

    @Override
    public boolean isPinned() {
        return pieceThisIsPinnedBy != null;
    }

    public boolean canCastle() {
        return canCastle;
    }

    public void setCanCastle(boolean canCastle) {
        this.canCastle = canCastle;
    }


    @Override
    public void setPieceThisIsPinning(Piece piece) {
        pieceThisIsPinning = piece;
    }

    @Override
    public void clearPieceThisIsPinning() {
        pieceThisIsPinning = null;
    }

    public boolean isPinning(Piece piece) {
        return pieceThisIsPinning == piece;
    }

    @Override
    public boolean isPinningAnyPiece() {
        return pieceThisIsPinning != null;
    }

    @Override
    public String toString() {
        return (IS_WHITE ? "White " : "Black ") + "Rook @ " + (char) ('A' + col) + (row + 1);
    }

    @Override
    public void mapAttackSquares() {
        AttackerMap.get().markVerticalHorizontalAttackSquares(this);
    }
}
