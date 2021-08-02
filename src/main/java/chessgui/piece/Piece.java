package chessgui.piece;

public interface Piece {
    String getFilePath();

    boolean isWhite();

    void setCol(int col);

    void setRow(int row);

    int getRow();

    int getCol();

    boolean canMove(int destRow, int destCol);

    void setPieceThisIsPinnedBy(Piece piece);

    void clearPieceThisIsPinnedBy();

    boolean isPinnedBy(Piece piece);

    boolean isPinned();

}
