package chessgui.board;

import chessgui.piece.Piece;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public class AttackerSquare {
    private final String SQUARE_NAME;
    private final Set<Piece> ATTACKING_PIECES;
    private final int ROW;
    private final int COL;
    private int blackCount;
    private int whiteCount;

    public AttackerSquare(int row, int col) {
        this.ROW = row;
        this.COL = col;
        this.SQUARE_NAME = String.valueOf((char) ('A' + col)) + (row + 1);
        this.ATTACKING_PIECES = new HashSet<>(32);
        this.blackCount = 0;
        this.whiteCount = 0;
    }

    public Set<Piece> getAttackingPieces() {
        return ATTACKING_PIECES;
    }

    public boolean addAttacker(Piece piece) {
        boolean pieceWasAdded = ATTACKING_PIECES.add(piece);
        if (pieceWasAdded) {
            if (piece.isWhite()) whiteCount++;
            else blackCount++;
        }
        return pieceWasAdded;
    }

    public boolean removeAttacker(Piece piece) {
        boolean pieceWasRemoved = ATTACKING_PIECES.remove(piece);
        if (pieceWasRemoved) {
            if (piece.isWhite()) whiteCount--;
            else blackCount--;
        }
        return pieceWasRemoved;
    }

    public boolean containsAttacker(Piece piece) {
        return ATTACKING_PIECES.contains(piece);
    }

    public String getSquareName() {
        return SQUARE_NAME;
    }

    public int getBlackCount() {
        return blackCount;
    }

    public int getWhiteCount() {
        return whiteCount;
    }

    public int getRow() {
        return ROW;
    }

    public int getCol() {
        return COL;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SQUARE_NAME + "[", "]")
                .add("Black Attackers: " + blackCount)
                .add("White Attackers: " + whiteCount)
                .toString();
    }
}
