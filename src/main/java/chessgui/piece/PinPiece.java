package chessgui.piece;

public interface PinPiece extends Piece {
    void setPieceThisIsPinning(Piece piece);

    void clearPieceThisIsPinning();

    boolean isPinning(Piece piece);

    boolean isPinningAnyPiece();

}
