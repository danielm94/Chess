package chessgui.board;

import chessgui.piece.King;
import chessgui.piece.Piece;
import chessgui.piece.PinPiece;

import java.util.HashSet;
import java.util.Set;

public class Helper {
    public static boolean isValidDiagonal(Piece selected, int destRow, int destCol) {
        return Math.abs(destRow - selected.getRow()) == Math.abs(destCol - selected.getCol());
    }

    public static boolean isPathClear(Piece selected, int destRow, int destCol) {
        int row = selected.getRow();
        int col = selected.getCol();

        if (row < destRow) row++;
        else if (row > destRow) row--;
        if (col < destCol) col++;
        else if (col > destCol) col--;

        while (row != destRow || col != destCol) {
            Piece pieceOnTile = Board.get().getPiece(row, col);
            if (pieceOnTile != null) return false;
            if (row < destRow) row++;
            else if (row > destRow) row--;
            if (col < destCol) col++;
            else if (col > destCol) col--;

        }
        return true;
    }

    public static boolean isPathClearToCastle(Piece selected, int destRow, int destCol) {
        int row = selected.getRow();
        int col = selected.getCol();

        if (row < destRow) row++;
        else if (row > destRow) row--;
        if (col < destCol) col++;
        else if (col > destCol) col--;

        while (row != destRow || col != destCol) {
            Piece pieceOnTile = Board.get().getPiece(row, col);
            AttackerSquare attackerSquare = AttackerMap.get().getSquare(row, col);
            if (pieceOnTile != null
                    || (selected.isWhite() && attackerSquare.getBlackCount() > 0)
                    || (!selected.isWhite() && attackerSquare.getWhiteCount() > 0)) return false;
            if (row < destRow) row++;
            else if (row > destRow) row--;
            if (col < destCol) col++;
            else if (col > destCol) col--;

        }
        return true;
    }

    public static boolean isNotOccupiedByFriendly(Piece selected, int destRow, int destCol) {
        Piece destPiece = Board.get().getPiece(destRow, destCol);
        if (destPiece == null) return true;
        return selected.isWhite() != destPiece.isWhite();
    }

    public static Set<AttackerSquare> getSquaresToBlockOrCapture(boolean forWhite, Board board) {
        King king = board.getKing(forWhite);
        AttackerMap attackerMap = AttackerMap.get();
        Piece pieceAttackingKing = king.getPieceAttacking();
        Set<AttackerSquare> squares = new HashSet<>();

        int destRow = pieceAttackingKing.getRow();
        int destCol = pieceAttackingKing.getCol();
        squares.add(attackerMap.getSquare(destRow, destCol));
        if (!(pieceAttackingKing instanceof PinPiece)) {
            return squares;
        }
        int tempRow = king.getRow();
        int tempCol = king.getCol();
        if (tempRow < destRow) tempRow++;
        else if (tempRow > destRow) tempRow--;
        if (tempCol < destCol) tempCol++;
        else if (tempCol > destCol) tempCol--;

        while (tempRow != destRow || tempCol != destCol) {
            squares.add(attackerMap.getSquare(tempRow, tempCol));
            if (tempRow < destRow) tempRow++;
            else if (tempRow > destRow) tempRow--;
            if (tempCol < destCol) tempCol++;
            else if (tempCol > destCol) tempCol--;
        }
        return squares;
    }

    public static boolean canBlockOrCapture(Piece piece, Board board) {
        King king = board.getKing(piece.isWhite());
        Piece pieceAttackingKing = king.getPieceAttacking();
        if (pieceAttackingKing instanceof PinPiece) {
            Set<AttackerSquare> squaresToBlockOrCapture = getSquaresToBlockOrCapture(piece.isWhite(), board);
            for (AttackerSquare square : squaresToBlockOrCapture) {
                if (piece.canMove(square.getRow(), square.getCol())) return true;
            }
        } else {
            return piece.canMove(pieceAttackingKing.getRow(), pieceAttackingKing.getCol());
        }

        return false;
    }
}
