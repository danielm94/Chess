package chessgui.piece;

import chessgui.gui.Board;
import chessgui.piece.piece_logic.ValidateDestination;

public class King implements Piece {
    private int x;
    private int y;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private final Board BOARD;
    private boolean hasMoved;

    public King(int x, int y, boolean IS_WHITE, String FILE_PATH, Board board) {
        this.IS_WHITE = IS_WHITE;
        this.x = x;
        this.y = y;
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
        return ValidateDestination.isNotOccupiedByFriendly(this, destinationX, destinationY, BOARD)
                && ((Math.abs(destinationX - x) == 1 && destinationY == y || Math.abs(destinationY - y) == 1 && destinationX == x) // is valid vertical/horizontal square (1 square distance)
                || (Math.abs(destinationX - x) == 1 && Math.abs(destinationY - y) == 1)); // is valid diagonal square (1 square distance)


    }

    @Override
    public Board getBoard() {
        return BOARD;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean canCastle(int destinationX, int destinationY) {
        if (hasMoved) return false;
        if ((destinationX == 1 || destinationX == 5)
                && ((isWhite() && destinationY == 0) || (!isWhite() && destinationY == 7))) {
            int rookX = destinationX > x ? 7 : 0;
            Piece pieceToCastle = BOARD.getPiece(rookX, destinationY);

            if (pieceToCastle == null //If is an empty square
                    || !pieceToCastle.getClass().equals(Rook.class)) { //If the piece occupying the square is not a rook.
                return false;
            }
            //Now we know that a rook is occupying the square, we can cast the piece as a rook.
            //TODO:ADD IS KING UNDER CHECK VALIDATION
            Rook rookToCastle = (Rook) pieceToCastle;
            return rookToCastle.isWhite() == isWhite()
                    && !rookToCastle.hasMoved()
                    && ValidateDestination.isPathClear(this, rookX, destinationY, BOARD);
        } else return false;
    }
}
