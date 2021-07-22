package chessgui.piece;

import chessgui.Board;

public interface Piece {
    String getFilePath();

    boolean isWhite();

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    boolean canMove(int destinationX, int destinationY);

    Board getBoard();
}
