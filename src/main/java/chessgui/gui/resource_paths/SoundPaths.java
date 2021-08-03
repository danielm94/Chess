package chessgui.gui.resource_paths;

public enum SoundPaths {
    CHECK("sound/check.wav"),
    PIECE_MOVE("sound/piece_move.wav"),
    INVALID_MOVE1("sound/invalid_move1.wav"),
    INVALID_MOVE2("sound/invalid_move2.wav"),
    INVALID_MOVE3("sound/invalid_move3.wav"),
    STALEMATE("sound/stalemate.wav"),
    STALEMATE_50("sound/stalemate_50.wav"),
    CHECKMATE("sound/checkmate.wav"),
    EASTER_EGG("sound/easter_egg.wav");

    private final String PATH;

    SoundPaths(String path) {
        this.PATH = path;
    }

    public String getPath() {
        return PATH;
    }
}
