package chessgui.piece.piece_logic;

import chessgui.gui.Board;
import chessgui.piece.Piece;

public class ValidateDestination {
    public static boolean isValidDiagonal(Piece selected, int destRow, int destCol) {
        return Math.abs(destRow - selected.getRow()) == Math.abs(destCol - selected.getCol());
    }

    public static boolean isValidStraightLine(Piece selected, int destRow, int destCol) {
        return (selected.getCol() == destCol && Math.abs(destRow - selected.getRow()) > 0)
                || (selected.getRow() == destRow && Math.abs(destCol - selected.getCol()) > 0);
    }

    public static boolean isPathClear(Piece selected, int destRow, int destCol, Board board) {
        int row = selected.getRow();
        int col = selected.getCol();

        if (row < destRow) row++;
        else if (row > destRow) row--;
        if (col < destCol) col++;
        else if (col > destCol) col--;

        while (row != destRow || col != destCol) {
            Piece pieceOnTile = board.getPiece(row, col);
            if (pieceOnTile != null) return false;
            if (row < destRow) row++;
            else if (row > destRow) row--;
            if (col < destCol) col++;
            else if (col > destCol) col--;

        }
        return true;
    }

    public static boolean isNotOccupiedByFriendly(Piece selected, int destRow, int destCol, Board board) {
        Piece destPiece = board.getPiece(destRow, destCol);
        if (destPiece == null) return true;
        return selected.isWhite() != destPiece.isWhite();
    }
}
