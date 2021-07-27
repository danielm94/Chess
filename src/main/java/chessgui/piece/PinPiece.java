package chessgui.piece;

public interface PinPiece extends Piece {
    boolean addPieceThisIsPinning(Piece piece);

    boolean removePieceThisIsPinning(Piece piece);

    boolean isPinning(Piece piece);

    int getNumPiecesPinned();
}
