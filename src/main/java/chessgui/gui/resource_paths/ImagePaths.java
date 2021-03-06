package chessgui.gui.resource_paths;

public enum ImagePaths {

    BLACK_PAWN("images/black_pieces/Pawn.png"), BLACK_BISHOP("images/black_pieces/Bishop.png"),
    BLACK_KNIGHT("images/black_pieces/Knight.png"), BLACK_ROOK("images/black_pieces/Rook.png"),
    BLACK_QUEEN("images/black_pieces/Queen.png"), BLACK_KING("images/black_pieces/King.png"),
    WHITE_PAWN("images/white_pieces/Pawn.png"), WHITE_BISHOP("images/white_pieces/Bishop.png"),
    WHITE_KNIGHT("images/white_pieces/Knight.png"), WHITE_ROOK("images/white_pieces/Rook.png"),
    WHITE_QUEEN("images/white_pieces/Queen.png"), WHITE_KING("images/white_pieces/King.png"),
    ACTIVE_SQUARE("images/active_square.png"), CHESSBOARD("images/board.png"),
    VALID_EMPTY_SQUARE("images/valid_empty.png"), ATTACKABLE_SQUARE("images/attackable_square.png"),
    LAST_MOVED_PIECE("images/last_moved_piece.png");


    private final String path;

    ImagePaths(String s) {
        this.path = s;
    }

    public String getPath() {
        return path;
    }
}
