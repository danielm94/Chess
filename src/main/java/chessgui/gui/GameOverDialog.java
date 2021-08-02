package chessgui.gui;

import chessgui.Main;
import chessgui.board.Board;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameOverDialog extends JDialog {
    private final JButton[] OPTIONS = new JButton[2];
    private Board board;
    private final ActionListener listener;

    public GameOverDialog(Board board, String title, String message) {
        this.setTitle(title);
        this.board = board;
        listener = new GameOverOptionListener(this);
        initializeOptions();
        JOptionPane gameOverPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION, null, OPTIONS, OPTIONS[0]);
        this.setContentPane(gameOverPane);
        this.pack();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                board.getGui().getParentFrame().dispose();
            }
        });
    }

    private void initializeOptions() {
        OPTIONS[0] = new JButton("New Game");
        OPTIONS[1] = new JButton("Quit");
        for (JButton button : OPTIONS)
            button.addActionListener(listener);

    }

    class GameOverOptionListener extends AbstractAction {
        private final JDialog PARENT_DIALOG;

        public GameOverOptionListener(JDialog parentDialog) {
            this.PARENT_DIALOG = parentDialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            switch (button.getText()) {
                case "New Game" -> {
                    board.getGui().getParentFrame().dispose();
                    Main.main(null);
                }
                case "Quit" -> board.getGui().getParentFrame().dispose();
            }
            PARENT_DIALOG.dispose();
        }
    }
}
