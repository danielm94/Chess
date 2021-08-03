package chessgui.gui;

import chessgui.board.Board;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickHandler extends MouseAdapter {

    @Override
    public void mousePressed(MouseEvent e) {
        if (Board.get().isWaitingForDialogExit()) return;
        boolean isWhitesTurn = Board.get().isWhitesTurn();
        int dX = e.getX();
        int dY = e.getY();
        int clickedRow = dY / BoardComponent.getSquareWidth();
        int clickedColumn = dX / BoardComponent.getSquareWidth();
        clickedRow = isWhitesTurn ? 7 - clickedRow : clickedRow;
        clickedColumn = isWhitesTurn ? 7 - clickedColumn : clickedColumn;
        System.out.println("Row = " + clickedRow + " Col = " + clickedColumn);
        Board.get().evaluateMove(clickedRow, clickedColumn);
    }
}
