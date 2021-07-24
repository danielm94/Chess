package chessgui.gui;

import chessgui.piece.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ClickHandler extends MouseAdapter {
    private Board board;
    private final List<Piece> WHITE_PIECES;
    private final List<Piece> BLACK_PIECES;

    public ClickHandler(Board board) {
        this.board = board;
        this.WHITE_PIECES = board.getWhitePieces();
        this.BLACK_PIECES = board.getBlackPieces();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int dX = e.getX();
        int dY = e.getY();
        int clickedRow = dY / board.getSquareWidth();
        int clickedColumn = dX / board.getSquareWidth();
        System.out.println("X = " + clickedColumn + " Y = " + clickedRow);
        boolean isWhitesTurn = board.getTurnCounter() % 2 == 1;
        Piece activePiece = board.getActivePiece();
        Piece clicked_piece = board.getPiece(clickedColumn, clickedRow);


        if (activePiece == null && clicked_piece != null &&
                ((isWhitesTurn && clicked_piece.isWhite()) || (!isWhitesTurn && !clicked_piece.isWhite()))) {
            board.setActivePiece(clicked_piece);
        } else if (activePiece != null && activePiece.getCol() == clickedColumn && activePiece.getRow() == clickedRow) {
            board.setActivePiece(null);
        } else if (activePiece != null && activePiece.getClass().equals(King.class) && Math.abs(clickedColumn - activePiece.getCol()) > 1) { // If player is trying to castle.
            King king = (King) activePiece;
            if (king.canCastle(clickedColumn, clickedRow)) {
                int rookX = clickedColumn > king.getCol() ? 7 : 0;
                int rookDestination = clickedColumn == 1 ? 2 : 4;
                Piece rook = board.getPiece(rookX, clickedRow);
                king.setCol(clickedColumn);
                king.setRow(clickedRow);
                king.setHasMoved(true);
                rook.setCol(rookDestination);
                rook.setRow(clickedRow);
                board.advanceTurn();
            }
        } else if (activePiece != null && activePiece.canMove(clickedColumn, clickedRow)
                && ((isWhitesTurn && activePiece.isWhite()) || (!isWhitesTurn && !activePiece.isWhite()))) {
            // if piece is there, remove it so we can be there
            if (clicked_piece != null) {
                board.removePiece(clicked_piece);
            }
            // do move
            int prevX = activePiece.getCol();
            int prevY = activePiece.getRow();
            activePiece.setCol(clickedColumn);
            activePiece.setRow(clickedRow);

            // if piece is a pawn set has_moved to true
            if (activePiece.getClass().equals(Pawn.class)) {
                Pawn castedPawn = (Pawn) activePiece;
                if (!castedPawn.getHasMoved()) {
                    castedPawn.setHasMoved(true);
                    castedPawn.setTurnMoved(board.getTurnCounter());
                    castedPawn.setMovedTwoSpaces(Math.abs(prevY - clickedRow) == 2);
                }
                if ((castedPawn.getRow() == 7 && castedPawn.isWhite()) || (castedPawn.getRow() == 0 && !castedPawn.isWhite())) {
                    JButton[] options = new JButton[4];
                    options[0] = new JButton("Queen");
                    options[1] = new JButton("Rook");
                    options[2] = new JButton("Bishop");
                    options[3] = new JButton("Knight");

                    JOptionPane promotionPane = new JOptionPane("Promote pawn to which piece?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION, null, options, options[0]);
                    JDialog dialog = new JDialog(JOptionPane.getRootFrame(), "Promotion");
                    dialog.setContentPane(promotionPane);
                    dialog.pack();
                    dialog.setVisible(true);
                    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    ActionListener actionListener = new AbstractAction() {
                        Piece activePieceAction = board.getActivePiece();

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JButton button = (JButton) (e.getSource());
                            Piece promotedPawn = switch (button.getText()) {
                                case "Queen" -> new Queen(activePieceAction.getCol(), activePieceAction.getRow(), activePieceAction.isWhite(),
                                        activePieceAction.isWhite() ? ImagePaths.WHITE_QUEEN.getPath() : ImagePaths.BLACK_QUEEN.getPath(), activePieceAction.getBoard());
                                case "Bishop" -> new Bishop(activePieceAction.getCol(), activePieceAction.getRow(), activePieceAction.isWhite(),
                                        activePieceAction.isWhite() ? ImagePaths.WHITE_BISHOP.getPath() : ImagePaths.BLACK_BISHOP.getPath(), activePieceAction.getBoard());
                                case "Rook" -> new Rook(activePieceAction.getCol(), activePieceAction.getRow(), activePieceAction.isWhite(),
                                        activePieceAction.isWhite() ? ImagePaths.WHITE_ROOK.getPath() : ImagePaths.BLACK_ROOK.getPath(), activePieceAction.getBoard());
                                case "Knight" -> new Knight(activePieceAction.getCol(), activePieceAction.getRow(), activePieceAction.isWhite(),
                                        activePieceAction.isWhite() ? ImagePaths.WHITE_KNIGHT.getPath() : ImagePaths.BLACK_KNIGHT.getPath(), activePieceAction.getBoard());
                                default -> throw new IllegalStateException("Unexpected selection: " + button.getText());
                            };
                            if (promotedPawn.isWhite()) {
                                WHITE_PIECES.set(WHITE_PIECES.indexOf(activePieceAction), promotedPawn);
                            } else {
                                BLACK_PIECES.set(BLACK_PIECES.indexOf(activePieceAction), promotedPawn);
                            }
                            board.setWaitingForPromotion(false);
                            board.advanceTurn();
                            board.drawBoard();
                            dialog.dispose();
                        }
                    };
                    for (JButton option : options) {
                        option.addActionListener(actionListener);
                    }
                    board.setWaitingForPromotion(true);
                }
            } else if (activePiece.getClass().equals(King.class)) {
                King castedKing = (King) activePiece;
                castedKing.setHasMoved(true);
            } else if (activePiece.getClass().equals(Rook.class)) {
                Rook castedRook = (Rook) activePiece;
                castedRook.setHasMoved(true);
            }
            if (!board.isWaitingForPromotion()) {
                board.advanceTurn();
            }
        }

        board.drawBoard();
    }
}
