package chessgui.gui;

import chessgui.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BoardFrame extends JFrame {
    BoardComponent component;
    private boolean drawLegalMoves = true;
    private boolean playSoundEffects = true;

    public BoardFrame(String FEN) {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUpMenuBar();
        this.setTitle("Chess");
        this.setResizable(false);
        component = new BoardComponent(this, FEN);
        this.add(component, BorderLayout.CENTER);
        this.setLocation(200, 200);
        this.pack();
        this.setVisible(true);
    }

    private void setUpMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menuBar.add(menu);

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Main.main(null);
            }
        });
        menu.add(newGameItem);

        JMenuItem loadFromFENItem = new JMenuItem("Load From FEN");
        loadFromFENItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fen = JOptionPane.showInputDialog("Enter FEN");
                //25 is the length of the shortest possible FEN (an empty board)
                if (fen.length() >= 25) {
                    dispose();
                    Main.main(new String[]{fen});
                }
            }
        });
        menu.add(loadFromFENItem);
        menuBar.add(menu);

        JMenu settings = new JMenu("Settings");
        JCheckBoxMenuItem showLegalMoves = new JCheckBoxMenuItem("Show Legal Moves");
        showLegalMoves.setSelected(true);
        showLegalMoves.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
                drawLegalMoves = jCheckBoxMenuItem.isSelected();
            }
        });

        /*JCheckBoxMenuItem playSFX = new JCheckBoxMenuItem("Play Sound Effects");
        playSFX.setSelected(true);
        playSFX.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
                playSoundEffects = jCheckBoxMenuItem.isSelected();
            }
        });*/

        settings.add(showLegalMoves);
//        settings.add(playSFX);
        menuBar.add(settings);
        this.setJMenuBar(menuBar);
    }

    public boolean isLegalMoveDrawingEnabled() {
        return drawLegalMoves;
    }

    public boolean isSoundEnabled() {
        return playSoundEffects;
    }
}
