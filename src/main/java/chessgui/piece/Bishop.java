package chessgui.piece;

import chessgui.board.AttackerSquare;
import chessgui.board.Board;
import chessgui.board.Helper;

import java.util.Set;

public class Bishop implements PinPiece {
    private int row;
    private int col;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;
    private Piece pieceThisIsPinnedBy;
    private Piece pieceThisIsPinning;

    public Bishop(int row, int col, boolean isWhite, String FILE_PATH, Board board) {
        this.IS_WHITE = isWhite;
        this.row = row;
        this.col = col;
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
        if (BOARD.inStateOfCheck()) {
            Set<AttackerSquare> squaresToBlockOrCapture = Helper.getSquaresToBlockOrCapture(IS_WHITE, BOARD);
            AttackerSquare destinationSquare = BOARD.getAttackerMap().getSquare(destRow, destCol);
            return !this.isPinned()
                    && destinationSquare.containsAttacker(this)
                    && squaresToBlockOrCapture.contains(destinationSquare);
        } else {
            /* If the attacker map object has this piece listed
              as attacking the specified row & column and it's not occupied by a friendly...*/
            if (BOARD.getAttackerMap().getSquare(destRow, destCol).containsAttacker(this)
                    && Helper.isNotOccupiedByFriendly(this, destRow, destCol, BOARD))
                //If we're being pinned
                if (this.isPinned()) {
                /* The only way a bishop can legally move while pinned is if it's being pinned diagonally
                and the destination square is on the same attacking diagonal as the piece pinning it,
                so it can continue to protect the king from the attacker or outright kill the
                attacker.
                    Check if we're being pinned by a bishop or a queen and that the destination square is on the same
                attacking diagonal as the piece that is pinning this bishop. */
                    return (pieceThisIsPinnedBy instanceof Queen || pieceThisIsPinnedBy instanceof Bishop)
                            && Helper.isValidDiagonal(pieceThisIsPinnedBy, destRow, destCol);
                } else return true; //If we're not pinned then the bishop is free to move there.
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

    @Override
    public String toString() {
        return (IS_WHITE ? "White " : "Black ") + "Bishop @ " + (char) ('A' + col) + (row + 1);
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
}
