package chessgui.piece;

public interface Piece {
    String getFilePath();

    boolean isWhite();

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    boolean canMove(int destination_x, int destination_y);
}
