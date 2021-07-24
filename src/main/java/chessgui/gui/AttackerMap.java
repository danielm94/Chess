package chessgui.gui;

import chessgui.piece.King;
import chessgui.piece.Pawn;
import chessgui.piece.Piece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttackerMap {
    private final List<List<Set<Piece>>> ATTACKED_SQUARES_MAP;
    private final Board board;

    public AttackerMap(Board board) {
        this.board = board;
        this.ATTACKED_SQUARES_MAP = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ATTACKED_SQUARES_MAP.add(new ArrayList<>());
            for (int j = 0; j < 8; j++) {
                ATTACKED_SQUARES_MAP.get(i).add(new HashSet<>());
            }
        }
        setUpAttackerMap();
    }

    private void setUpAttackerMap() {
        List<Piece> whitePieces = board.getWhitePieces();
        List<Piece> blackPieces = board.getBlackPieces();
        for (Piece piece : whitePieces) {
            if (piece.getClass().equals(Pawn.class)) {
                markPawnAttackSquares((Pawn) piece);
            } else if (piece.getClass().equals(King.class)) {
                markKingAttackSquares((King) piece);
            }
        }
        for (Piece piece : blackPieces) {
            if (piece.getClass().equals(Pawn.class)) {
                markPawnAttackSquares((Pawn) piece);
            } else if (piece.getClass().equals(King.class)) {
                markKingAttackSquares((King) piece);
            }
        }
    }

    public void clearPieceAttackSquares(Piece piece) {
        for (List<Set<Piece>> listOfSets : ATTACKED_SQUARES_MAP) {
            for (Set<Piece> set : listOfSets) {
                set.remove(piece);
            }
        }
    }

    private void markPawnAttackSquares(Pawn pawn) {
        int x = pawn.getCol();
        int y = pawn.getRow();
        clearPieceAttackSquares(pawn);
        if (pawn.isWhite()) {
            if (x > 0) {
                ATTACKED_SQUARES_MAP.get(y + 1).get(x - 1).add(pawn);
            }
            if (x < 7) {
                ATTACKED_SQUARES_MAP.get(y + 1).get(x + 1).add(pawn);
            }
        } else {
            if (x > 0) {
                ATTACKED_SQUARES_MAP.get(y - 1).get(x - 1).add(pawn);
            }
            if (x < 7) {
                ATTACKED_SQUARES_MAP.get(y - 1).get(x + 1).add(pawn);
            }
        }
    }

    private void markKingAttackSquares(King king) {
        int x = king.getCol();
        int y = king.getRow();
        clearPieceAttackSquares(king);
        int startX = Math.max(x - 1, 0);
        int endX = Math.min(x + 1, 7);
        int startY = Math.max(y - 1, 0);
        int endY = Math.min(y + 1, 7);
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                ATTACKED_SQUARES_MAP.get(i).get(j).add(king);
            }
        }
    }
}
