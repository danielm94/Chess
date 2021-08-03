package chessgui.piece;

import chessgui.board.AttackerMap;
import chessgui.board.AttackerSquare;
import chessgui.board.Board;
import chessgui.board.Helper;

import java.util.Set;

public class Pawn implements Piece {
    private int row;
    private int col;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private boolean hasMoved;
    private int turnMoved;
    private boolean movedTwoSpaces;
    private Piece pieceThisIsPinnedBy;

    public Pawn(int row, int col, boolean isWhite, String FILE_PATH) {
        this.IS_WHITE = isWhite;
        this.row = row;
        this.col = col;
        this.FILE_PATH = FILE_PATH;
        this.hasMoved = false;
        this.turnMoved = -1;
        this.movedTwoSpaces = false;
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
            King king = Board.get().getKing(IS_WHITE);
            Piece pieceAttackingKing = king.getPieceAttacking();
            Set<AttackerSquare> squaresToBlockOrCapture = Helper.getSquaresToBlockOrCapture(IS_WHITE, Board.get());
            if (pieceAttackingKing instanceof Pawn) {
                if (canEncroissant(IS_WHITE ? destRow - 1 : destRow + 1, destCol))
                    squaresToBlockOrCapture.add(AttackerMap.get().getSquare(destRow, destCol));
            }
            AttackerSquare destinationSquare = AttackerMap.get().getSquare(destRow, destCol);
            Piece pieceOnSquare = Board.get().getPiece(destRow, destCol);
            if (pieceOnSquare == null) {
                return !this.isPinned()
                        && squaresToBlockOrCapture.contains(destinationSquare)
                        && (isValidEmptySquareForPawn(destRow, destCol) || canEncroissant(IS_WHITE ? destRow - 1 : destRow + 1, destCol));
            } else {
                return !this.isPinned()
                        && squaresToBlockOrCapture.contains(destinationSquare)
                        && canBeCapturedByPawn(destRow, destCol);
            }
        } else {
            Piece destinationPiece = Board.get().getPiece(destRow, destCol);

            if (this.isPinned())
                if (destinationPiece != null && isPinnedBy(Board.get().getPiece(destRow, destCol)))
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
    public void mapAttackSquares() {
        AttackerMap.get().markPawnAttackSquares(this);
    }


    public boolean isValidEmptySquareForPawn(int destRow, int destCol) {
        if (IS_WHITE) {
            if (hasMoved) {
                return destRow - row == 1 && destCol == col;
            } else {
                return Helper.isPathClear(this, destRow, destCol) && destRow - row > 0 && destRow - row <= 2 && destCol == col;
            }
        } else {
            if (hasMoved) {
                return row - destRow == 1 && destCol == col;
            } else {
                return Helper.isPathClear(this, destRow, destCol) && row - destRow > 0 && row - destRow <= 2 && destCol == col;
            }
        }
    }

    @Override
    public String toString() {
        return (IS_WHITE ? "White " : "Black ") + "Pawn @ " + (char) ('A' + col) + (row + 1);
    }

    public boolean canBeCapturedByPawn(int destRow, int destCol) {
        if (!Helper.isNotOccupiedByFriendly(this, destRow, destCol)) return false;
        return IS_WHITE ? destRow - row == 1 && (destCol - col == 1 || destCol - col == -1)
                : row - destRow == 1 && (destCol - col == 1 || destCol - col == -1);
    }

    public boolean canEncroissant(int destRow, int destCol) {
        Piece destPiece = Board.get().getPiece(destRow, destCol);
        if (destPiece == null || !destPiece.getClass()
                                           .equals(Pawn.class)) {
            return false;
        } else {
            return destRow == row
                    && Math.abs(destCol - col) == 1
                    && ((Pawn) destPiece).movedTwoSpaces
                    && Board.get().getTurnCounter() == ((Pawn) destPiece).turnMoved + 1;
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

    public boolean hasMovedTwoSpaces() {
        return movedTwoSpaces;
    }

    public void setMovedTwoSpaces(boolean movedTwoSpaces) {
        this.movedTwoSpaces = movedTwoSpaces;
    }
}
