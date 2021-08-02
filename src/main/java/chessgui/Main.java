package chessgui;

import chessgui.gui.BoardFrame;

/**
 * @author Daniel Martins
 */
public class Main {

    public BoardFrame boardframe;

    public static void main(String[] args) {
        Main gui = new Main();
        String FEN = args == null || args.length == 0 ? null : args[0];
        gui.boardframe = new BoardFrame(FEN);
        gui.boardframe.setVisible(true);
    }
}
