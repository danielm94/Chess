package chessgui.piece;

import chessgui.gui.Board;

public interface Piece {
    String getFilePath();

    boolean isWhite();

    void setCol(int col);

    void setRow(int row);
    int getRow();
    int getCol();

    boolean canMove(int destinationX, int destinationY);

    Board getBoard();
}
