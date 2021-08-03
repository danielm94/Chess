package chessgui.gui.dialog_windows;

import chessgui.Main;
import chessgui.board.Board;
import chessgui.gui.BoardComponent;
import chessgui.gui.resource_paths.ImagePaths;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameOverDialog extends JDialog {
    private final JButton[] OPTIONS = new JButton[2];

    private final ActionListener listener;

    public GameOverDialog(String title, String message) {
        try {
            this.setIconImage(ImageIO.read(ClassLoader.getSystemResource(ImagePaths.WHITE_KING.getPath())));
        } catch (IOException e) {
            this.setIconImage(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
        }
        this.setResizable(false);
        this.setTitle(title);
        BoardComponent.get().getParentFrame().disableInputs();
        listener = new GameOverOptionListener(this);
        initializeOptions();
        JOptionPane gameOverPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION, null, OPTIONS, OPTIONS[0]);
        this.setContentPane(gameOverPane);
        this.pack();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                BoardComponent.get().getParentFrame().dispose();
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
            if (button.getText().equals("New Game")) {
                Board.get().dispose();
                BoardComponent.get().getParentFrame().dispose();
                BoardComponent.get().dispose();
                Main.main(null);
            } else if (button.getText().equals("Quit")) {
                BoardComponent.get().getParentFrame().dispose();
            }
            PARENT_DIALOG.dispose();
        }
    }
}
