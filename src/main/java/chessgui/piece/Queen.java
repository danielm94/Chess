package chessgui.piece;

import chessgui.board.AttackerSquare;
import chessgui.board.Board;
import chessgui.board.Helper;

import java.util.Set;

public class Queen implements PinPiece {
    private int row;
    private int col;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;
    private Piece pieceThisIsPinnedBy;
    private Piece pieceThisIsPinning;

    public Queen(int row, int col, boolean isWhite, String FILE_PATH, Board board) {
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
        /*  If the attacker map object has this piece listed as an
        attacker in the destination square and it's not occupied by a friendly piece...*/
            if (BOARD.getAttackerMap().getSquare(destRow, destCol).containsAttacker(this)
                    && Helper.isNotOccupiedByFriendly(this, destRow, destCol, BOARD)) {
                //If this piece is pinned
                if (this.isPinned()) {
                /*If the row or column of the piece that is pinning us is the same as this piece's current row or column
                  then it's a vertical or horizontal pin. Verify that destination row matches the pin piece's row or the destination
                  column matches the piece's column.
                 */
                    if (pieceThisIsPinnedBy.getRow() == row)
                        return destRow == pieceThisIsPinnedBy.getRow();
                    else if (pieceThisIsPinnedBy.getCol() == col)
                        return destCol == pieceThisIsPinnedBy.getCol();
                    else
                    /*
                    Otherwise, it's a diagonal pin. Check if the destination square is on
                    the same diagonal as the pinning piece
                    and the destination square is within the boundary area covered by the
                    king and the piece that is pinning.

                    A B C D E F G H  Let's assume that the B on F5 is a black bishop, the Q on D7
                1   - - - - - - - -  is a white queen and the K on C8 is a white king. The squares
                2   - - - - - - - -  marked as "#" represent squares within the boundary of the
                3   - - - - - - - -  pinning piece and the king. The squares marked as "-" represent
                4   - - - - - - - -  the squares outside the boundary. The queen in this instance is only
                5   - - # # # B - -  allowed to move to squares within the boundary and on the same diagonal
                6   - - # # # # - -  as the pinning piece.
                7   - - # Q # # - -
                8   - - K # # # - -
                 */
                        return Helper.isValidDiagonal(pieceThisIsPinnedBy, destRow, destCol)
                                && destRow >= Math.min(BOARD.getKing(IS_WHITE).getRow(), pieceThisIsPinnedBy.getRow())
                                && destRow <= Math.max(BOARD.getKing(IS_WHITE).getRow(), pieceThisIsPinnedBy.getRow())
                                && destCol >= Math.min(BOARD.getKing(IS_WHITE).getCol(), pieceThisIsPinnedBy.getCol())
                                && destCol <= Math.max(BOARD.getKing(IS_WHITE).getCol(), pieceThisIsPinnedBy.getCol());
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


    @Override
    public void setPieceThisIsPinning(Piece piece) {
        pieceThisIsPinning = piece;
    }

    @Override
    public void clearPieceThisIsPinning() {
        pieceThisIsPinning = null;
    }

    @Override
    public boolean isPinning(Piece piece) {
        return pieceThisIsPinning == piece;
    }

    @Override
    public boolean isPinningAnyPiece() {
        return pieceThisIsPinning != null;
    }

    @Override
    public String toString() {
        return (IS_WHITE ? "White " : "Black ") + "Queen @ " + (char) ('A' + col) + (row + 1);
    }
}
