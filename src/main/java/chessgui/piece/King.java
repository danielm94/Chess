package chessgui.piece;

import chessgui.board.AttackerMap;
import chessgui.board.AttackerSquare;
import chessgui.board.Board;
import chessgui.board.Helper;

import java.util.List;
import java.util.Set;

public class King implements Piece {
    private int row;
    private int col;
    private final boolean IS_WHITE;
    private final String FILE_PATH;
    private boolean canCastle;
    private Piece pinPieceAttackingThis;

    public King(int row, int col, boolean IS_WHITE, String FILE_PATH) {
        this.canCastle = false;
        this.IS_WHITE = IS_WHITE;
        this.row = row;
        this.col = col;
        this.FILE_PATH = FILE_PATH;
    }

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    @Override
    public boolean isWhite() {
        return IS_WHITE;
    }

    @Override
    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public boolean canMove(int destRow, int destCol) {
        AttackerSquare destSquare = AttackerMap.get().getSquare(destRow, destCol);
        int enemiesAttackingDest = IS_WHITE ? destSquare.getBlackCount() : destSquare.getWhiteCount();

        if (isChecked() && Board.get().getPiece(destRow, destCol) != pinPieceAttackingThis) {
            if (pinPieceAttackingThis instanceof Rook)
                return !(pinPieceAttackingThis.getRow() == destRow || pinPieceAttackingThis.getCol() == destCol)
                        && destSquare.containsAttacker(this)
                        && enemiesAttackingDest == 0
                        && Helper.isNotOccupiedByFriendly(this, destRow, destCol);
            else if (pinPieceAttackingThis instanceof Bishop)
                return !Helper.isValidDiagonal(pinPieceAttackingThis, destRow, destCol)
                        && destSquare.containsAttacker(this)
                        && enemiesAttackingDest == 0
                        && Helper.isNotOccupiedByFriendly(this, destRow, destCol);
            else if (pinPieceAttackingThis instanceof Queen)
                return !(pinPieceAttackingThis.getRow() == destRow || pinPieceAttackingThis.getCol() == destCol)
                        && !Helper.isValidDiagonal(pinPieceAttackingThis, destRow, destCol)
                        && destSquare.containsAttacker(this)
                        && enemiesAttackingDest == 0
                        && Helper.isNotOccupiedByFriendly(this, destRow, destCol);

        }
        return destSquare.containsAttacker(this)
                && enemiesAttackingDest == 0
                && Helper.isNotOccupiedByFriendly(this, destRow, destCol);

    }

    @Override
    public void setPieceThisIsPinnedBy(Piece piece) {
        pinPieceAttackingThis = piece;
    }

    @Override
    public void clearPieceThisIsPinnedBy() {
        pinPieceAttackingThis = null;
    }

    @Override
    public boolean isPinnedBy(Piece piece) {
        return pinPieceAttackingThis == piece;
    }

    @Override
    public boolean isPinned() {
        return pinPieceAttackingThis != null;
    }

    @Override
    public void mapAttackSquares() {
        AttackerMap.get().markKingAttackSquares(this);
    }

    public Piece getPieceAttacking() {
        if (pinPieceAttackingThis != null)
            return pinPieceAttackingThis;
        else {
            AttackerMap attackerMap = AttackerMap.get();
            AttackerSquare kingSquare = attackerMap.getSquare(row, col);
            Set<Piece> attackingPieces = kingSquare.getAttackingPieces();
            for (Piece attackPiece : attackingPieces)
                if (attackPiece.isWhite() != IS_WHITE) return attackPiece;
        }
        return null;
    }

    @Override
    public String toString() {
        return (IS_WHITE ? "White " : "Black ") + "King @ " + (char) ('A' + col) + (row + 1);
    }


    public void setCanCastle(boolean canCastle) {
        this.canCastle = canCastle;
    }

    public boolean canCastle(int destRow, int destCol) {
        if (isChecked() || !canCastle) return false;
        if ((destCol == 1 || destCol == 2 || destCol == 6)
                && ((isWhite() && destRow == 0) || (!isWhite() && destRow == 7))) {
            int rookCol = destCol > col ? 7 : 0;
            Piece pieceToCastle = Board.get().getPiece(destRow, rookCol);

            if (pieceToCastle == null //If is an empty square
                    || !pieceToCastle.getClass().equals(Rook.class)) { //If the piece occupying the square is not a rook.
                return false;
            }
            //Now we know that a rook is occupying the square, we can cast the piece as a rook.
            Rook rookToCastle = (Rook) pieceToCastle;
            return rookToCastle.isWhite() == isWhite()
                    && rookToCastle.canCastle()
                    && Helper.isPathClearToCastle(this, destRow, rookCol);
        } else return false;
    }

    public boolean isChecked() {
        AttackerSquare kingSquare = AttackerMap.get().getSquare(row, col);
        int numKingAttackers = IS_WHITE ? kingSquare.getBlackCount() : kingSquare.getWhiteCount();
        return numKingAttackers > 0
                || pinPieceAttackingThis != null;
    }

    public boolean isMated() {
        AttackerSquare kingSquare = AttackerMap.get().getSquare(row, col);
        int numKingAttackers = IS_WHITE ? kingSquare.getBlackCount() : kingSquare.getWhiteCount();
        if (numKingAttackers == 0) {
            return false;
        } else if (numKingAttackers == 1) {
            if (hasLegalMove()) return false;
            else {
                //get the squares leading from the king all the way to the attacker. if any piece can move there (not pinned + legal move), then return false.
                List<Piece> friendlyPieces = IS_WHITE ? Board.get().getWhitePieces() : Board.get().getBlackPieces();
                if (friendlyPieces.size() == 1) return true;
                for (Piece piece : friendlyPieces) {
                    if (piece instanceof King) continue;
                    if (Helper.canBlockOrCapture(piece, Board.get())) return false;
                }
                return true;
            }
            //check every piece to see if they have a move to either kill the attacking piece or block its path.
        } else if (numKingAttackers > 1) {
            return !hasLegalMove();
        }
        return false;
    }

    public boolean hasLegalMove() {
        int startRow = Math.max(0, row - 1);
        int endRow = Math.min(7, row + 1);
        int startCol = Math.max(0, col - 1);
        int endCol = Math.min(7, col + 1);
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                AttackerSquare square = AttackerMap.get().getSquare(i, j);
                int enemiesOnSquare = IS_WHITE ? square.getBlackCount() : square.getWhiteCount();
                //If we find a square with no enemy pieces attacking it, then the king has a legal move.
                if ((row != i || col != j) && canMove(i, j))
                    return true;
            }
        }
        return false;
    }
}
