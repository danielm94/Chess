package chessgui.piece;

import chessgui.gui.Board;

public interface Piece {
    String getFilePath();

    boolean isWhite();

    void setCol(int col);

    void setRow(int row);

    int getRow();

    int getCol();

    boolean canMove(int destRow, int destCol);

    boolean addPieceThisIsPinnedBy(Piece piece);

    boolean removePieceThisIsPinnedBy(Piece piece);

    boolean isPinnedBy(Piece piece);

    int getNumPiecesPinningThis();
}
