package chessgui.board;

import chessgui.piece.*;

import java.util.ArrayList;
import java.util.List;

//TODO: If you find out there is no need for piece casting for pawns/kings/knights remove casts
public class AttackerMap {
    private final List<List<AttackerSquare>> ATTACKED_SQUARES_MAP;
    private final Board board;

    public AttackerMap(Board board) {
        this.board = board;
        this.ATTACKED_SQUARES_MAP = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ATTACKED_SQUARES_MAP.add(new ArrayList<>());
            for (int j = 0; j < 8; j++) {
                ATTACKED_SQUARES_MAP.get(i).add(new AttackerSquare(i, j));
            }
        }
        setUpAttackerMap();
    }

    private void setUpAttackerMap() {
        List<Piece> whitePieces = board.getWhitePieces();
        List<Piece> blackPieces = board.getBlackPieces();
        for (Piece piece : whitePieces)
            if (piece.getClass().equals(Pawn.class))
                markPawnAttackSquares((Pawn) piece);
            else if (piece.getClass().equals(King.class))
                markKingAttackSquares((King) piece);
            else if (piece.getClass().equals(Knight.class))
                markKnightAttackSquares((Knight) piece);
            else if (piece.getClass().equals(Rook.class))
                markVerticalHorizontalAttackSquares(piece);
            else if (piece.getClass().equals(Bishop.class))
                markDiagonalAttackSquares(piece);
            else if (piece.getClass().equals(Queen.class)) {
                markVerticalHorizontalAttackSquares(piece);
                markDiagonalAttackSquares(piece);
            }
        for (Piece piece : blackPieces)
            if (piece.getClass().equals(Pawn.class))
                markPawnAttackSquares((Pawn) piece);
            else if (piece.getClass().equals(King.class))
                markKingAttackSquares((King) piece);
            else if (piece.getClass().equals(Knight.class))
                markKnightAttackSquares((Knight) piece);
            else if (piece.getClass().equals(Rook.class))
                markVerticalHorizontalAttackSquares(piece);
            else if (piece.getClass().equals(Bishop.class))
                markDiagonalAttackSquares(piece);
            else if (piece.getClass().equals(Queen.class)) {
                markVerticalHorizontalAttackSquares(piece);
                markDiagonalAttackSquares(piece);
            }
    }

    public void clearPieceAttackSquares(Piece piece) {
        for (List<AttackerSquare> listOfSquares : ATTACKED_SQUARES_MAP)
            for (AttackerSquare square : listOfSquares) square.removeAttacker(piece);
    }

    public void clearPiecePins(Piece piece) {
        if (piece instanceof PinPiece)
            if (piece.isWhite()) {
                for (Piece blackPiece : board.getBlackPieces()) {
                    if (blackPiece.isPinnedBy(piece)) {
                        blackPiece.clearPieceThisIsPinnedBy();
                        ((PinPiece) piece).clearPieceThisIsPinning();
                    }
                }
            } else {
                for (Piece whitePiece : board.getWhitePieces()) {
                    if (whitePiece.isPinnedBy(piece)) {
                        whitePiece.clearPieceThisIsPinnedBy();
                        ((PinPiece) piece).clearPieceThisIsPinning();
                    }
                }
            }
    }

    public void updatePieceAttackSquares(Piece piece) {
        clearPieceAttackSquares(piece);
        clearPiecePins(piece);
        if (piece.getClass().equals(Pawn.class))
            markPawnAttackSquares((Pawn) piece);
        else if (piece.getClass().equals(King.class))
            markKingAttackSquares((King) piece);
        else if (piece.getClass().equals(Knight.class))
            markKnightAttackSquares((Knight) piece);
        else if (piece.getClass().equals(Rook.class))
            markVerticalHorizontalAttackSquares(piece);
        else if (piece.getClass().equals(Bishop.class))
            markDiagonalAttackSquares(piece);
        else if (piece.getClass().equals(Queen.class)) {
            markVerticalHorizontalAttackSquares(piece);
            markDiagonalAttackSquares(piece);
        }
    }

    public AttackerSquare getSquare(int row, int col) {
        return ATTACKED_SQUARES_MAP.get(row).get(col);
    }

    private void markPawnAttackSquares(Pawn pawn) {
        int row = pawn.getRow();
        int col = pawn.getCol();
        if (pawn.isWhite()) {
            if (row < 7) {
                if (col > 0) ATTACKED_SQUARES_MAP.get(row + 1).get(col - 1).addAttacker(pawn);
                if (col < 7) ATTACKED_SQUARES_MAP.get(row + 1).get(col + 1).addAttacker(pawn);
            }
        } else {
            if (row > 0) {
                if (col > 0) ATTACKED_SQUARES_MAP.get(row - 1).get(col - 1).addAttacker(pawn);
                if (col < 7) ATTACKED_SQUARES_MAP.get(row - 1).get(col + 1).addAttacker(pawn);
            }
        }
    }

    private void markKingAttackSquares(King king) {
        int row = king.getRow();
        int col = king.getCol();
        int startRow = Math.max(0, row - 1);
        int endRow = Math.min(7, row + 1);
        int startCol = Math.max(0, col - 1);
        int endCol = Math.min(7, col + 1);
        for (int i = startRow; i <= endRow; i++)
            for (int j = startCol; j <= endCol; j++) {
                AttackerSquare square = ATTACKED_SQUARES_MAP.get(i).get(j);
                if ((row != i || col != j))
                    square.addAttacker(king);
            }

    }

    private void markKnightAttackSquares(Knight knight) {
        int row = knight.getRow();
        int col = knight.getCol();
        int startRow = Math.max(row - 2, 0);
        int endRow = Math.min(row + 2, 7);
        int startCol = Math.max(col - 2, 0);
        int endCol = Math.min(col + 2, 7);
        //brute force valid square check.
        for (int i = startRow; i <= endRow; i++)
            for (int j = startCol; j <= endCol; j++)
                if (knight.isValidKnightSquare(i, j))
                    ATTACKED_SQUARES_MAP.get(i).get(j).addAttacker(knight);
    }

    private void markVerticalHorizontalAttackSquares(Piece piece) {
        int row = piece.getRow();
        int col = piece.getCol();

        //Check Vertical Squares
        //Check From Current Row To Row 0
        if (row > 0) markSquaresToDestination(piece, 0, col);
        //Check From Current Row To Row 7
        if (row < 7) markSquaresToDestination(piece, 7, col);

        //Check Horizontal Squares
        //Check From Current Col To Col 0
        if (col > 0) markSquaresToDestination(piece, row, 0);
        //Check from Current Col to Col 7
        if (col < 7) markSquaresToDestination(piece, row, 7);
    }

    private void markDiagonalAttackSquares(Piece piece) {
        int row = piece.getRow();
        int col = piece.getCol();
        int endRow = row;
        int endCol = col;

        //Get top left diagonal
        if (row > 0 && col > 0) {
            while (endRow > 0 && endCol > 0) {
                endRow--;
                endCol--;
            }
            markSquaresToDestination(piece, endRow, endCol);
        }

        //Get bottom left diagonal
        if (row < 7 && col > 0) {
            endRow = row;
            endCol = col;
            while (endRow < 7 && endCol > 0) {
                endRow++;
                endCol--;
            }
            markSquaresToDestination(piece, endRow, endCol);
        }

        //Get top right diagonal
        if (row > 0 && col < 7) {
            endRow = row;
            endCol = col;
            while (endRow > 0 && endCol < 7) {
                endRow--;
                endCol++;
            }
            markSquaresToDestination(piece, endRow, endCol);
        }

        //Get bottom right diagonal
        if (row < 7 && col < 7) {
            endRow = row;
            endCol = col;
            while (endRow < 7 && endCol < 7) {
                endRow++;
                endCol++;
            }
            markSquaresToDestination(piece, endRow, endCol);
        }
    }

    private void markSquaresToDestination(Piece piece, int endRow, int endCol) {
        int row = piece.getRow();
        int col = piece.getCol();

        if (endRow > row) row++;
        else if (endRow < row) row--;
        if (endCol > col) col++;
        else if (endCol < col) col--;

        while (row != endRow || col != endCol) {
            AttackerSquare square = ATTACKED_SQUARES_MAP.get(row).get(col);
            square.addAttacker(piece);
            Piece pieceOnSquare = board.getPiece(row, col);
            if (pieceOnSquare != null) {
                if (pieceOnSquare.isWhite() != piece.isWhite()) checkForPins(piece, row, col, endRow, endCol);
                break;
            }
            if (row < endRow) row++;
            else if (row > endRow) row--;
            if (col < endCol) col++;
            else if (col > endCol) col--;
        }
        AttackerSquare square = ATTACKED_SQUARES_MAP.get(row).get(col);
        square.addAttacker(piece);
        Piece pieceOnSquare = board.getPiece(row, col);
        if (pieceOnSquare != null) {
            if (pieceOnSquare.isWhite() != piece.isWhite())
                if (pieceOnSquare instanceof King)
                    pieceOnSquare.setPieceThisIsPinnedBy(piece);
                else checkForPins(piece, row, col, endRow, endCol);
        }
    }

    private void checkForPins(Piece piece, int startRow, int startCol, int endRow, int endCol) {
        /* endRow and endCol will always be the rows and columns for squares which represent the boundary squares.
        If the starting row equals the ending row and the starting column equals the ending column,
        it is not possible for there to be a pin, otherwise you'd be looking for pins outside of the 8x8 board.
         */
        if (startRow == endRow && startCol == endCol) return;
        Piece enemyPiece = board.getPiece(startRow, startCol);
        if (enemyPiece.getClass().equals(King.class)) {
            enemyPiece.setPieceThisIsPinnedBy(piece);
            return;
        }
        King enemyKing = board.getKing(!piece.isWhite());

        //Preliminary checks to see if it's even worth looking for pins.
        //If the king is neither on the same row or col as the rook then it is not possible for the rook to be pinning anything.
        if (piece.getClass().equals(Rook.class)) {
            if (enemyKing.getRow() != piece.getRow() && enemyKing.getCol() != piece.getCol())
                return;
        }
        //If the king is not on the same diagonal as the bishop then it's not possible for the bishop to be pinning anything.
        else if (piece.getClass().equals(Bishop.class)) {
            if (Math.abs(piece.getRow() - enemyKing.getRow()) != Math.abs(piece.getCol() - enemyKing.getCol()))
                return;
        }
        //If the above two conditions are true for the Queen, then it's not possible for it to be pinning anything.
        else if (piece.getClass().equals(Queen.class))
            if ((enemyKing.getRow() != piece.getRow() && enemyKing.getCol() != piece.getCol())
                    && (Math.abs(piece.getRow() - enemyKing.getRow()) != Math.abs(piece.getCol() - enemyKing.getCol())))
                return;

        //Begin looking for the enemy king.
        int row = startRow;
        int col = startCol;

        if (endRow > row) row++;
        else if (endRow < row) row--;
        if (endCol > col) col++;
        else if (endCol < col) col--;
        while (row != endRow || col != endCol) {
            Piece pieceOnSquare = board.getPiece(row, col);
            if (pieceOnSquare != null) if (pieceOnSquare == enemyKing) {
                enemyPiece.setPieceThisIsPinnedBy(piece);
                if (piece.getClass().equals(Rook.class)) {
                    Rook temp = (Rook) piece;
                    temp.setPieceThisIsPinning(enemyPiece);
                } else if (piece.getClass().equals(Bishop.class)) {
                    Bishop temp = (Bishop) piece;
                    temp.setPieceThisIsPinning(enemyPiece);
                } else if (piece.getClass().equals(Queen.class)) {
                    Queen temp = (Queen) piece;
                    temp.setPieceThisIsPinning(enemyPiece);
                }
            } else return;
            if (row < endRow) row++;
            else if (row > endRow) row--;
            if (col < endCol) col++;
            else if (col > endCol) col--;
        }
        Piece pieceOnSquare = board.getPiece(row, col);
        if (pieceOnSquare != null)
            if (pieceOnSquare == enemyKing) {
                enemyPiece.setPieceThisIsPinnedBy(piece);
                if (piece.getClass().equals(Rook.class)) {
                    Rook temp = (Rook) piece;
                    temp.setPieceThisIsPinning(enemyPiece);
                } else if (piece.getClass().equals(Bishop.class)) {
                    Bishop temp = (Bishop) piece;
                    temp.setPieceThisIsPinning(enemyPiece);
                } else if (piece.getClass().equals(Queen.class)) {
                    Queen temp = (Queen) piece;
                    temp.setPieceThisIsPinning(enemyPiece);
                }
            }
    }
}
