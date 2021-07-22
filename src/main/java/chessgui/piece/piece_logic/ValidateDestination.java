package chessgui.piece.piece_logic;

import chessgui.Board;
import chessgui.piece.Piece;

public class ValidateDestination {
    public static boolean isValidDiagonal(Piece selected, int destX, int destY) {
        return Math.abs(destX - selected.getX()) == Math.abs(destY - selected.getY());
    }

    public static boolean isValidStraightLine(Piece selected, int destX, int destY) {
        return (selected.getX() == destX && Math.abs(destY - selected.getY()) > 0)
                || (selected.getY() == destY && Math.abs(destX - selected.getX()) > 0);
    }

    public static boolean isPathClear(Piece selected, int destX, int destY, Board board) {
        int x = selected.getX();
        int y = selected.getY();
        if (x < destX) x++;
        else if (x > destX) x--;
        if (y < destY) y++;
        else if (y > destY) y--;

        while (x != destX || y != destY) {
            Piece pieceOnTile = board.getPiece(x, y);
            if (pieceOnTile != null) return false;
            if (x < destX) x++;
            else if (x > destX) x--;
            if (y < destY) y++;
            else if (y > destY) y--;
        }
        return true;
    }

    public static boolean isNotOccupiedByFriendly(Piece selected, int destX, int destY, Board board) {
        Piece destPiece = board.getPiece(destX, destY);
        if (destPiece == null) return true;
        return selected.isWhite() != destPiece.isWhite();
    }
}
