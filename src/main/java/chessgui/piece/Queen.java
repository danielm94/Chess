package chessgui.piece;

import chessgui.Board;

public class Queen implements Piece {
    private int x;
    private int y;
    private final boolean is_white;
    private final String file_path;
    public Board board;

    public Queen(int x, int y, boolean is_white, String file_path, Board board) {
        this.is_white = is_white;
        this.x = x;
        this.y = y;
        this.file_path = file_path;
        this.board = board;
    }

    @Override
    public String getFilePath() {
        return file_path;
    }

    @Override
    public boolean isWhite() {
        return is_white;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public boolean canMove(int destination_x, int destination_y) {
        return false;
    }
}
