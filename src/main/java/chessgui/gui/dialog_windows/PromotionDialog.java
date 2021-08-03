package chessgui.gui.dialog_windows;

import chessgui.board.AttackerMap;
import chessgui.board.Board;
import chessgui.gui.BoardComponent;
import chessgui.gui.resource_paths.ImagePaths;
import chessgui.piece.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class PromotionDialog extends JDialog {
    private final JButton[] OPTIONS = new JButton[4];
    private final ActionListener ACTION_LISTENER;
    private JOptionPane promotionPane;
    private final Piece PROMOTION_PIECE;

    public PromotionDialog(String title) {
        super(JOptionPane.getRootFrame(), title);
        try {
            this.setIconImage(ImageIO.read(ClassLoader.getSystemResource(ImagePaths.BLACK_PAWN.getPath())));
        } catch (IOException e) {
            this.setIconImage(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
        }
        this.setResizable(false);
        BoardComponent.get().getParentFrame().disableInputs();
        PROMOTION_PIECE = Board.get().getActivePiece();
        ACTION_LISTENER = new PromotionActionListener(this);
        initializeOptions();
        promotionPane = new JOptionPane("Promote pawn to which piece?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION, null, OPTIONS, OPTIONS[0]);
        this.setContentPane(promotionPane);
        this.pack();
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    private void initializeOptions() {
        OPTIONS[0] = new JButton("Queen");
        OPTIONS[1] = new JButton("Rook");
        OPTIONS[2] = new JButton("Bishop");
        OPTIONS[3] = new JButton("Knight");

        for (JButton button : OPTIONS) {
            button.addActionListener(ACTION_LISTENER);
        }
    }

    class PromotionActionListener extends AbstractAction {
        private final JDialog DIALOG;

        public PromotionActionListener(JDialog dialog) {
            this.DIALOG = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) (e.getSource());
            Piece promotedPawn;
            switch (button.getText()) {
                case "Queen":
                    promotedPawn = new Queen(PROMOTION_PIECE.getRow(), PROMOTION_PIECE.getCol(), PROMOTION_PIECE.isWhite(),
                            PROMOTION_PIECE.isWhite() ? ImagePaths.WHITE_QUEEN.getPath() : ImagePaths.BLACK_QUEEN.getPath());
                    break;
                case "Bishop":
                    promotedPawn = new Bishop(PROMOTION_PIECE.getRow(), PROMOTION_PIECE.getCol(), PROMOTION_PIECE.isWhite(),
                            PROMOTION_PIECE.isWhite() ? ImagePaths.WHITE_BISHOP.getPath() : ImagePaths.BLACK_BISHOP.getPath());
                    break;
                case "Rook":
                    promotedPawn = new Rook(PROMOTION_PIECE.getRow(), PROMOTION_PIECE.getCol(), PROMOTION_PIECE.isWhite(),
                            PROMOTION_PIECE.isWhite() ? ImagePaths.WHITE_ROOK.getPath() : ImagePaths.BLACK_ROOK.getPath());
                    break;
                case "Knight":
                    promotedPawn = new Knight(PROMOTION_PIECE.getRow(), PROMOTION_PIECE.getCol(), PROMOTION_PIECE.isWhite(),
                            PROMOTION_PIECE.isWhite() ? ImagePaths.WHITE_KNIGHT.getPath() : ImagePaths.BLACK_KNIGHT.getPath());
                    break;
                default:
                    throw new IllegalStateException("Unexpected selection: " + button.getText());
            }
            if (promotedPawn.isWhite()) {
                List<Piece> whitePieces = Board.get().getWhitePieces();
                whitePieces.set(whitePieces.indexOf(PROMOTION_PIECE), promotedPawn);
            } else {
                List<Piece> blackPieces = Board.get().getBlackPieces();
                blackPieces.set(blackPieces.indexOf(PROMOTION_PIECE), promotedPawn);
            }
            AttackerMap.get().updatePieceAttackSquares(promotedPawn);
            Board.get().setWaitingForDialogExit(false);
            BoardComponent.get().getParentFrame().enableInputs();
            Board.get().advanceTurn();
            BoardComponent.get().drawBoard();
            DIALOG.dispose();
        }
    }
}


