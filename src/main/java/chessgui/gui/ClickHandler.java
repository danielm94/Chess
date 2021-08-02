package chessgui.gui;

import chessgui.board.Board;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickHandler extends MouseAdapter {
    private final Board BOARD;

    public ClickHandler(Board board) {
        this.BOARD = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (BOARD.isWaitingForDialogExit()) return;
        int dX = e.getX();
        int dY = e.getY();
        int clickedRow = dY / BoardComponent.getSquareWidth();
        int clickedColumn = dX / BoardComponent.getSquareWidth();
        System.out.println("Row = " + clickedRow + " Col = " + clickedColumn);
        BOARD.evaluateMove(clickedRow, clickedColumn);
    }
}
