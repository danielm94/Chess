package chessgui.gui;

import javax.swing.*;

public class BoardComponent extends JComponent {
    private final Board BOARD;

    public BoardComponent() {
        this.BOARD = new Board(this);
    }
}
