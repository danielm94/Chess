package chessgui.piece;

import chessgui.gui.Board;
import chessgui.piece.piece_logic.ValidateDestination;

public class Pawn implements Piece {
    private int x;
    private int y;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;
    private boolean hasMoved;
    private int turnMoved;
    private boolean movedTwoSpaces;

    public Pawn(int x, int y, boolean isWhite, String FILE_PATH, Board board) {
        this.IS_WHITE = isWhite;
        this.x = x;
        this.y = y;
        this.FILE_PATH = FILE_PATH;
        this.BOARD = board;
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
        this.x = col;
    }

    @Override
    public void setRow(int row) {
        this.y = row;
    }

    @Override
    public int getCol() {
        return x;
    }

    @Override
    public int getRow() {
        return y;
    }

    @Override
    public boolean canMove(int destinationX, int destinationY) {
        Piece destinationPiece = BOARD.getPiece(destinationX, destinationY);
        if (destinationPiece == null) {
            if (IS_WHITE) {
                return canEncroissant(destinationX, destinationY - 1) ||
                        isValidEmptySquareForPawn(destinationX, destinationY);
            } else {
                return canEncroissant(destinationX, destinationY + 1)
                        || isValidEmptySquareForPawn(destinationX, destinationY);
            }
        } else if (destinationPiece.isWhite() != IS_WHITE) {
            return canBeCapturedByPawn(destinationX, destinationY);
        } else {
            return false;
        }
    }

    @Override
    public Board getBoard() {
        return BOARD;
    }

    public boolean isValidEmptySquareForPawn(int destinationX, int destinationY) {
        if (IS_WHITE) {
            if (hasMoved) {
                return destinationY - y == 1 && destinationX == x;
            } else {
                return destinationY - y > 0 && destinationY - y <= 2 && destinationX == x;
            }
        } else {
            if (hasMoved) {
                return y - destinationY == 1 && destinationX == x;
            } else {
                return y - destinationY > 0 && y - destinationY <= 2 && destinationX == x;
            }
        }
    }

    public boolean canBeCapturedByPawn(int destinationX, int destinationY) {
        if (!ValidateDestination.isNotOccupiedByFriendly(this, destinationX, destinationY, BOARD)) return false;
        return IS_WHITE ? destinationY - y == 1 && (destinationX - x == 1 || destinationX - x == -1)
                : y - destinationY == 1 && (destinationX - x == 1 || destinationX - x == -1);
    }

    public boolean canEncroissant(int destinationX, int destinationY) {
        Piece destPiece = BOARD.getPiece(destinationX, destinationY);
        if (destPiece == null || !destPiece.getClass()
                                           .equals(Pawn.class)) {
            return false;
        } else {
            if (destinationY == y
                    && Math.abs(destinationX - x) == 1
                    && ((Pawn) destPiece).movedTwoSpaces
                    && BOARD.getTurnCounter() == ((Pawn) destPiece).turnMoved + 1) {
                BOARD.removePiece(destPiece);
                return true;
            } else return false;
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
