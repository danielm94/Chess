package chessgui.gui;

import chessgui.Main;
import chessgui.board.Board;
import chessgui.gui.dialog_windows.GameOverDialog;
import chessgui.gui.resource_paths.ImagePaths;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BoardFrame extends JFrame {
    private JPanel mainPanel;
    private BoardComponent component;
    private JLabel toMoveLabel;
    private JLabel turnLabel;
    private JPanel gameInfoPanel;
    private JButton stalemateIn50Button;
    private JLabel horizontalLabel1;
    private JLabel horizontalLabel2;
    private JLabel horizontalLabel3;
    private JLabel horizontalLabel4;
    private JLabel horizontalLabel5;
    private JLabel horizontalLabel6;
    private JLabel horizontalLabel7;
    private JLabel horizontalLabel8;
    private JLabel verticalLabel1;
    private JLabel verticalLabel2;
    private JLabel verticalLabel3;
    private JLabel verticalLabel4;
    private JLabel verticalLabel5;
    private JLabel verticalLabel6;
    private JLabel verticalLabel7;
    private JLabel verticalLabel8;
    private JMenu gameMenu;
    private JMenu settingsMenu;
    private boolean drawLegalMoves = true;
    private boolean playSoundEffects = true;


    public BoardFrame(String FEN) {
        $$$setupUI$$$();
        try {
            this.setIconImage(ImageIO.read(ClassLoader.getSystemResource(ImagePaths.WHITE_KNIGHT.getPath())));
        } catch (IOException e) {
            this.setIconImage(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
        }

        Board.get().setUpBoard(FEN);
        BoardComponent.get().setUpBoardComponent(this);
        if (Board.get().isWhitesTurn()) BoardComponent.get().getParentFrame().flipBoardLabels();
        this.setToMove(Board.get().isWhitesTurn());
        this.setTurn(Board.get().getTurnCounter());
        this.setFiftyTurnRuleCounter(0);
        stalemateIn50Button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableInputs();
                if (BoardComponent.get().getParentFrame().isSoundEnabled())
                    SoundEffects.playStalemateBy50MoveRuleSound();
                Board.get().setWaitingForDialogExit(true);
                JDialog stalemateDialog = new GameOverDialog("Stalemate!",
                        "Draw by fifty move rule.");
                stalemateDialog.setVisible(true);

            }
        });
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUpMenuBar();
        this.setTitle("Chess");
        this.setContentPane(mainPanel);
        this.setLocation(200, 200);
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        component.setParentFrame(this);

    }

    public void disableInputs() {
        stalemateIn50Button.setEnabled(false);
        gameMenu.setEnabled(false);
        settingsMenu.setEnabled(false);
    }

    public void enableInputs() {
        stalemateIn50Button.setEnabled(true);
        gameMenu.setEnabled(true);
        settingsMenu.setEnabled(true);
    }

    private void createUIComponents() {
        component = BoardComponent.get();
    }

    private void setUpMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Board.get().dispose();
                BoardComponent.get().dispose();
                dispose();
                Main.main(null);
            }
        });
        gameMenu.add(newGameItem);

        JMenuItem loadFromFENItem = new JMenuItem("Load From FEN");
        loadFromFENItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fen = JOptionPane.showInputDialog("Enter FEN");
                //25 is the length of the shortest possible FEN (an empty board)
                if (fen != null && fen.length() >= 25) {
                    Board.get().dispose();
                    BoardComponent.get().dispose();
                    dispose();
                    Main.main(new String[]{fen});
                } else if (fen != null && fen.equalsIgnoreCase("fen")) {
                    SoundEffects.playEasterEgg();
                }
            }
        });
        gameMenu.add(loadFromFENItem);
        menuBar.add(gameMenu);

        settingsMenu = new JMenu("Settings");
        JCheckBoxMenuItem showLegalMoves = new JCheckBoxMenuItem("Show Legal Moves");
        showLegalMoves.setSelected(true);
        showLegalMoves.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
                drawLegalMoves = jCheckBoxMenuItem.isSelected();
            }
        });

        JCheckBoxMenuItem playSFX = new JCheckBoxMenuItem("Play Sound Effects");
        playSFX.setSelected(true);
        playSFX.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
                playSoundEffects = jCheckBoxMenuItem.isSelected();
            }
        });

        settingsMenu.add(showLegalMoves);
        settingsMenu.add(playSFX);
        menuBar.add(settingsMenu);
        this.setJMenuBar(menuBar);
    }

    public boolean isLegalMoveDrawingEnabled() {
        return drawLegalMoves;
    }

    public boolean isSoundEnabled() {
        return playSoundEffects;
    }

    public void setToMove(boolean isWhite) {
        toMoveLabel.setText(isWhite ? "White" : "Black");
    }

    public void setTurn(int turn) {
        turnLabel.setText(String.valueOf(turn));
    }

    public void setFiftyTurnRuleCounter(int count) {
        if (count >= 50) {
            stalemateIn50Button.setText("Request Draw");
            stalemateIn50Button.setEnabled(true);
        } else {
            stalemateIn50Button.setText("Stalemate in " + (50 - count));
            stalemateIn50Button.setEnabled(false);
        }
    }

    public void flipBoardLabels() {
        boolean isWhitesTurn = Board.get().isWhitesTurn();
        if (isWhitesTurn) {
            verticalLabel1.setText("8");
            verticalLabel2.setText("7");
            verticalLabel3.setText("6");
            verticalLabel4.setText("5");
            verticalLabel5.setText("4");
            verticalLabel6.setText("3");
            verticalLabel7.setText("2");
            verticalLabel8.setText("1");
            horizontalLabel1.setText("H");
            horizontalLabel2.setText("G");
            horizontalLabel3.setText("F");
            horizontalLabel4.setText("E");
            horizontalLabel5.setText("D");
            horizontalLabel6.setText("C");
            horizontalLabel7.setText("B");
            horizontalLabel8.setText("A");
        } else {
            verticalLabel1.setText("1");
            verticalLabel2.setText("2");
            verticalLabel3.setText("3");
            verticalLabel4.setText("4");
            verticalLabel5.setText("5");
            verticalLabel6.setText("6");
            verticalLabel7.setText("7");
            verticalLabel8.setText("8");
            horizontalLabel1.setText("A");
            horizontalLabel2.setText("B");
            horizontalLabel3.setText("C");
            horizontalLabel4.setText("D");
            horizontalLabel5.setText("E");
            horizontalLabel6.setText("F");
            horizontalLabel7.setText("G");
            horizontalLabel8.setText("H");
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(13, 10, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBackground(new Color(-8816263));
        mainPanel.setFocusCycleRoot(true);
        mainPanel.setMinimumSize(new Dimension(680, 555));
        mainPanel.setOpaque(false);
        mainPanel.setPreferredSize(new Dimension(680, 555));
        mainPanel.setVerifyInputWhenFocusTarget(false);
        component.setEnabled(true);
        mainPanel.add(component, new GridConstraints(0, 2, 11, 8, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(520, 520), new Dimension(520, 520), new Dimension(520, 520), 0, false));
        verticalLabel1 = new JLabel();
        verticalLabel1.setHorizontalTextPosition(0);
        verticalLabel1.setText("1");
        mainPanel.add(verticalLabel1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        horizontalLabel1 = new JLabel();
        horizontalLabel1.setText("A");
        horizontalLabel1.setVerticalAlignment(1);
        horizontalLabel1.setVerticalTextPosition(1);
        mainPanel.add(horizontalLabel1, new GridConstraints(11, 2, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        horizontalLabel2 = new JLabel();
        horizontalLabel2.setText("B");
        horizontalLabel2.setVerticalAlignment(1);
        horizontalLabel2.setVerticalTextPosition(1);
        mainPanel.add(horizontalLabel2, new GridConstraints(11, 3, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        horizontalLabel3 = new JLabel();
        horizontalLabel3.setText("C");
        horizontalLabel3.setVerticalAlignment(1);
        horizontalLabel3.setVerticalTextPosition(1);
        mainPanel.add(horizontalLabel3, new GridConstraints(11, 4, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        horizontalLabel4 = new JLabel();
        horizontalLabel4.setText("D");
        horizontalLabel4.setVerticalAlignment(1);
        horizontalLabel4.setVerticalTextPosition(1);
        mainPanel.add(horizontalLabel4, new GridConstraints(11, 5, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        horizontalLabel5 = new JLabel();
        horizontalLabel5.setText("E");
        horizontalLabel5.setVerticalAlignment(1);
        horizontalLabel5.setVerticalTextPosition(1);
        mainPanel.add(horizontalLabel5, new GridConstraints(11, 6, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        horizontalLabel6 = new JLabel();
        horizontalLabel6.setText("F");
        horizontalLabel6.setVerticalAlignment(1);
        horizontalLabel6.setVerticalTextPosition(1);
        mainPanel.add(horizontalLabel6, new GridConstraints(11, 7, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        horizontalLabel7 = new JLabel();
        horizontalLabel7.setText("G");
        horizontalLabel7.setVerticalAlignment(1);
        horizontalLabel7.setVerticalTextPosition(1);
        mainPanel.add(horizontalLabel7, new GridConstraints(11, 8, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        horizontalLabel8 = new JLabel();
        horizontalLabel8.setText("H");
        horizontalLabel8.setVerticalAlignment(1);
        horizontalLabel8.setVerticalTextPosition(1);
        mainPanel.add(horizontalLabel8, new GridConstraints(11, 9, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        verticalLabel2 = new JLabel();
        verticalLabel2.setText("2");
        mainPanel.add(verticalLabel2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        verticalLabel3 = new JLabel();
        verticalLabel3.setText("3");
        mainPanel.add(verticalLabel3, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        verticalLabel4 = new JLabel();
        verticalLabel4.setText("4");
        mainPanel.add(verticalLabel4, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        verticalLabel5 = new JLabel();
        verticalLabel5.setText("5");
        mainPanel.add(verticalLabel5, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        verticalLabel6 = new JLabel();
        verticalLabel6.setText("6");
        mainPanel.add(verticalLabel6, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        verticalLabel7 = new JLabel();
        verticalLabel7.setText("7");
        mainPanel.add(verticalLabel7, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        verticalLabel8 = new JLabel();
        verticalLabel8.setText("8");
        mainPanel.add(verticalLabel8, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(gameInfoPanel, new GridConstraints(0, 0, 12, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gameInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("To Move: ");
        gameInfoPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Turn: ");
        gameInfoPanel.add(label2, new GridConstraints(1, 0, 2, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        toMoveLabel = new JLabel();
        toMoveLabel.setHorizontalAlignment(11);
        toMoveLabel.setHorizontalTextPosition(2);
        toMoveLabel.setText("");
        gameInfoPanel.add(toMoveLabel, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(35, -1), new Dimension(35, -1), null, 0, false));
        turnLabel = new JLabel();
        turnLabel.setHorizontalAlignment(0);
        turnLabel.setHorizontalTextPosition(0);
        turnLabel.setText("999");
        gameInfoPanel.add(turnLabel, new GridConstraints(1, 2, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(35, -1), new Dimension(35, -1), null, 0, false));
        stalemateIn50Button = new JButton();
        stalemateIn50Button.setEnabled(false);
        stalemateIn50Button.setHorizontalAlignment(0);
        stalemateIn50Button.setMargin(new Insets(0, 0, 0, 0));
        stalemateIn50Button.setText("Stalemate in 50");
        gameInfoPanel.add(stalemateIn50Button, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(12, 2, 1, 8, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
