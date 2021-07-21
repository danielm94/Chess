package chessgui;

public enum ImagePaths {
    BLACK_PAWN("src/main/resources/images/black_pieces/Pawn.png"), BLACK_BISHOP("src/main/resources/images/black_pieces/Bishop.png"),
    BLACK_KNIGHT("src/main/resources/images/black_pieces/Knight.png"), BLACK_ROOK("src/main/resources/images/black_pieces/Rook.png"),
    BLACK_QUEEN("src/main/resources/images/black_pieces/Queen.png"), BLACK_KING("src/main/resources/images/black_pieces/King.png"),
    WHITE_PAWN("src/main/resources/images/white_pieces/Pawn.png"), WHITE_BISHOP("src/main/resources/images/white_pieces/Bishop.png"),
    WHITE_KNIGHT("src/main/resources/images/white_pieces/Knight.png"), WHITE_ROOK("src/main/resources/images/white_pieces/Rook.png"),
    WHITE_QUEEN("src/main/resources/images/white_pieces/Queen.png"), WHITE_KING("src/main/resources/images/white_pieces/King.png"),
    ACTIVE_SQUARE("src/main/resources/images/active_square.png"), CHESSBOARD("src/main/resources/images/board.png"),
    BLACK_PIECES_FOLDER("src/main/resources/images/black_pieces/"), WHITE_PIECES_FOLDER("src/main/resources/images/white_pieces/");

    private final String path;

    ImagePaths(String s) {
        this.path = s;
    }

    public String getPath() {
        return path;
    }
}
