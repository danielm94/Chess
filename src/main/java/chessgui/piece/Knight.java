package chessgui.piece;

import chessgui.board.AttackerMap;
import chessgui.board.AttackerSquare;
import chessgui.board.Board;
import chessgui.board.Helper;

import java.util.Set;

public class Knight implements Piece {
    private int row;
    private int col;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private Piece pieceThisIsPinnedBy;

    public Knight(int row, int col, boolean isWhite, String FILE_PATH) {
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

        } else
            return !this.isPinned()
                    && AttackerMap.get().getSquare(destRow, destCol).containsAttacker(this)
                    && Helper.isNotOccupiedByFriendly(this, destRow, destCol);

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
    public String toString() {
        return (IS_WHITE ? "White " : "Black ") + "Knight @ " + (char) ('A' + col) + (row + 1);
    }


    @Override
    public boolean isPinnedBy(Piece piece) {
        return pieceThisIsPinnedBy == piece;
    }

    @Override
    public boolean isPinned() {
        return pieceThisIsPinnedBy != null;
    }

    @Override
    public void mapAttackSquares() {
        AttackerMap.get().markKnightAttackSquares(this);
    }

    public boolean isValidKnightSquare(int destRow, int destCol) {
        return ((Math.abs(destRow - row) == 2 && Math.abs(destCol - col) == 1)
                || (Math.abs(destRow - row) == 1 && Math.abs(destCol - col) == 2));
    }
}
