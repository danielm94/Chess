package chessgui.gui;

import chessgui.board.Board;
import chessgui.piece.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PromotionDialog extends JDialog {
    private final JButton[] OPTIONS = new JButton[4];
    private final Board BOARD;
    private final ActionListener ACTION_LISTENER;
    private JOptionPane promotionPane;
    private final Piece PROMOTION_PIECE;

    public PromotionDialog(Board board, String title) {
        super(JOptionPane.getRootFrame(), title);
        this.BOARD = board;
        PROMOTION_PIECE = BOARD.getActivePiece();
        ACTION_LISTENER = new PromotionActionListener(BOARD, this);
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
        private final Board BOARD;
        private final JDialog DIALOG;

        public PromotionActionListener(Board board, JDialog dialog) {
            this.BOARD = board;
            this.DIALOG = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) (e.getSource());
            Piece promotedPawn = switch (button.getText()) {
                case "Queen" -> new Queen(PROMOTION_PIECE.getRow(), PROMOTION_PIECE.getCol(), PROMOTION_PIECE.isWhite(),
                        PROMOTION_PIECE.isWhite() ? ImagePaths.WHITE_QUEEN.getPath() : ImagePaths.BLACK_QUEEN.getPath(), BOARD);
                case "Bishop" -> new Bishop(PROMOTION_PIECE.getRow(), PROMOTION_PIECE.getCol(), PROMOTION_PIECE.isWhite(),
                        PROMOTION_PIECE.isWhite() ? ImagePaths.WHITE_BISHOP.getPath() : ImagePaths.BLACK_BISHOP.getPath(), BOARD);
                case "Rook" -> new Rook(PROMOTION_PIECE.getRow(), PROMOTION_PIECE.getCol(), PROMOTION_PIECE.isWhite(),
                        PROMOTION_PIECE.isWhite() ? ImagePaths.WHITE_ROOK.getPath() : ImagePaths.BLACK_ROOK.getPath(), BOARD);
                case "Knight" -> new Knight(PROMOTION_PIECE.getRow(), PROMOTION_PIECE.getCol(), PROMOTION_PIECE.isWhite(),
                        PROMOTION_PIECE.isWhite() ? ImagePaths.WHITE_KNIGHT.getPath() : ImagePaths.BLACK_KNIGHT.getPath(), BOARD);
                default -> throw new IllegalStateException("Unexpected selection: " + button.getText());
            };
            if (promotedPawn.isWhite()) {
                List<Piece> whitePieces = BOARD.getWhitePieces();
                whitePieces.set(whitePieces.indexOf(PROMOTION_PIECE), promotedPawn);
            } else {
                List<Piece> blackPieces = BOARD.getBlackPieces();
                blackPieces.set(blackPieces.indexOf(PROMOTION_PIECE), promotedPawn);
            }
            BOARD.getAttackerMap().updatePieceAttackSquares(promotedPawn);
            BOARD.setWaitingForDialogExit(false);
            BOARD.advanceTurn();
            BOARD.getGui().drawBoard();
            DIALOG.dispose();
        }
    }
}


