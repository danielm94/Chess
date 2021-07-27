package chessgui.piece;

import chessgui.gui.Board;
import chessgui.piece.piece_logic.ValidateDestination;

import java.util.HashSet;
import java.util.Set;

public class Pawn implements Piece {
    private int row;
    private int col;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;
    private boolean hasMoved;
    private int turnMoved;
    private boolean movedTwoSpaces;
    private final Set<Piece> PIECES_PINNED_BY;

    public Pawn(int row, int col, boolean isWhite, String FILE_PATH, Board board) {
        this.IS_WHITE = isWhite;
        this.row = row;
        this.col = col;
        this.FILE_PATH = FILE_PATH;
        this.BOARD = board;
        this.hasMoved = false;
        this.turnMoved = -1;
        this.movedTwoSpaces = false;
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
        Piece destinationPiece = BOARD.getPiece(destRow, destCol);

        if (getNumPiecesPinningThis() > 0)
            if (getNumPiecesPinningThis() == 1 && destinationPiece != null && isPinnedBy(BOARD.getPiece(destRow, destCol)))
                return canBeCapturedByPawn(destRow, destCol);
            else
                return false;

        if (destinationPiece == null) {
            if (IS_WHITE) {
                return canEncroissant(destRow - 1, destCol) ||
                        isValidEmptySquareForPawn(destRow, destCol);
            } else {
                return canEncroissant(destRow + 1, destCol)
                        || isValidEmptySquareForPawn(destRow, destCol);
            }
        } else if (destinationPiece.isWhite() != IS_WHITE) {
            return canBeCapturedByPawn(destRow, destCol);
        } else {
            return false;
        }
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

    public boolean isValidEmptySquareForPawn(int destRow, int destCol) {
        if (IS_WHITE) {
            if (hasMoved) {
                return destRow - row == 1 && destCol == col;
            } else {
                return destRow - row > 0 && destRow - row <= 2 && destCol == col;
            }
        } else {
            if (hasMoved) {
                return row - destRow == 1 && destCol == col;
            } else {
                return row - destRow > 0 && row - destRow <= 2 && destCol == col;
            }
        }
    }

    public boolean canBeCapturedByPawn(int destRow, int destCol) {
        if (!ValidateDestination.isNotOccupiedByFriendly(this, destRow, destCol, BOARD)) return false;
        return IS_WHITE ? destRow - row == 1 && (destCol - col == 1 || destCol - col == -1)
                : row - destRow == 1 && (destCol - col == 1 || destCol - col == -1);
    }

    public boolean canEncroissant(int destRow, int destCol) {
        Piece destPiece = BOARD.getPiece(destRow, destCol);
        if (destPiece == null || !destPiece.getClass()
                                           .equals(Pawn.class)) {
            return false;
        } else {
            return destRow == row
                    && Math.abs(destCol - col) == 1
                    && ((Pawn) destPiece).movedTwoSpaces
                    && BOARD.getTurnCounter() == ((Pawn) destPiece).turnMoved + 1;
        }
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public int getTurnMoved() {
        return turnMoved;
    }

    public void setTurnMoved(int turnMoved) {
        this.turnMoved = turnMoved;
    }

    public boolean isMovedTwoSpaces() {
        return movedTwoSpaces;
    }

    public void setMovedTwoSpaces(boolean movedTwoSpaces) {
        this.movedTwoSpaces = movedTwoSpaces;
    }
}
