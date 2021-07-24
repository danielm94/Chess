package chessgui.piece.piece_logic;

import chessgui.gui.Board;
import chessgui.piece.Piece;

public class ValidateDestination {
    public static boolean isValidDiagonal(Piece selected, int destX, int destY) {
        return Math.abs(destX - selected.getCol()) == Math.abs(destY - selected.getRow());
    }

    public static boolean isValidStraightLine(Piece selected, int destX, int destY) {
        return (selected.getCol() == destX && Math.abs(destY - selected.getRow()) > 0)
                || (selected.getRow() == destY && Math.abs(destX - selected.getCol()) > 0);
    }

    public static boolean isPathClear(Piece selected, int destX, int destY, Board board) {
        int x = selected.getCol();
        int y = selected.getRow();
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
