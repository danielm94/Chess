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
        System.out.println("Row = " + clickedRow + " Col = " + clickedColumn);
        boolean isWhitesTurn = board.getTurnCounter() % 2 == 1;
        Piece activePiece = board.getActivePiece();
        Piece clickedPiece = board.getPiece(clickedRow, clickedColumn);


        if (activePiece == null && clickedPiece != null &&
                ((isWhitesTurn && clickedPiece.isWhite()) || (!isWhitesTurn && !clickedPiece.isWhite()))) {
            board.setActivePiece(clickedPiece);
        } else if (activePiece != null && activePiece.getCol() == clickedColumn && activePiece.getRow() == clickedRow) {
            board.setActivePiece(null);
        } else if (activePiece != null && activePiece.getClass().equals(King.class) && Math.abs(clickedColumn - activePiece.getCol()) > 1) { // If player is trying to castle.
            King king = (King) activePiece;
            if (king.canCastle(clickedRow, clickedColumn)) {
                int rookCol = clickedColumn > king.getCol() ? 7 : 0;
                int rookDestCol = clickedColumn <= 2 ? 3 : 5;
                /*If the player clicked on column 1, (0 indexed) set it the king's destination
                to column 2, which is the appropriate column when castling queen side.*/
                int kingCol = clickedColumn == 1 ? 2 : clickedColumn;
                Piece rook = board.getPiece(clickedRow, rookCol);
                board.movePiece(king, clickedRow, kingCol);
                king.setHasMoved(true);
                board.movePiece(rook, clickedRow, rookDestCol);
                board.advanceTurn();
            }
        } else if (activePiece != null && activePiece.canMove(clickedRow, clickedColumn)
                && ((isWhitesTurn && activePiece.isWhite()) || (!isWhitesTurn && !activePiece.isWhite()))) {
            // if piece is there, remove it so we can be there
            if (clickedPiece != null) {
                board.getAttackerMap().clearPiecePins(clickedPiece);
                board.getAttackerMap().clearPieceAttackSquares(clickedPiece);
                board.removePiece(clickedPiece);
            } else {
                int enCroissantRow = activePiece.isWhite() ? clickedRow - 1 : clickedRow + 1;
                if (activePiece.getClass().equals(Pawn.class) && ((Pawn) activePiece).canEncroissant(enCroissantRow, clickedColumn)) {
                    clickedPiece = board.getPiece(enCroissantRow, clickedColumn);
                    board.getAttackerMap().clearPiecePins(clickedPiece);
                    board.getAttackerMap().clearPieceAttackSquares(clickedPiece);
                    board.removePiece(clickedPiece);
                }
            }
            // do move
            int prevRow = activePiece.getRow();
            board.movePiece(activePiece, clickedRow, clickedColumn);

            // if piece is a pawn set has_moved to true
            if (activePiece.getClass().equals(Pawn.class)) {
                Pawn castedPawn = (Pawn) activePiece;
                if (!castedPawn.getHasMoved()) {
                    castedPawn.setHasMoved(true);
                    castedPawn.setTurnMoved(board.getTurnCounter());
                    castedPawn.setMovedTwoSpaces(Math.abs(prevRow - clickedRow) == 2);
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
                        final Piece activePieceAction = board.getActivePiece();

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JButton button = (JButton) (e.getSource());
                            Piece promotedPawn = switch (button.getText()) {
                                case "Queen" -> new Queen(activePieceAction.getRow(), activePieceAction.getCol(), activePieceAction.isWhite(),
                                        activePieceAction.isWhite() ? ImagePaths.WHITE_QUEEN.getPath() : ImagePaths.BLACK_QUEEN.getPath(), board);
                                case "Bishop" -> new Bishop(activePieceAction.getRow(), activePieceAction.getCol(), activePieceAction.isWhite(),
                                        activePieceAction.isWhite() ? ImagePaths.WHITE_BISHOP.getPath() : ImagePaths.BLACK_BISHOP.getPath(), board);
                                case "Rook" -> new Rook(activePieceAction.getRow(), activePieceAction.getCol(), activePieceAction.isWhite(),
                                        activePieceAction.isWhite() ? ImagePaths.WHITE_ROOK.getPath() : ImagePaths.BLACK_ROOK.getPath(), board);
                                case "Knight" -> new Knight(activePieceAction.getRow(), activePieceAction.getCol(), activePieceAction.isWhite(),
                                        activePieceAction.isWhite() ? ImagePaths.WHITE_KNIGHT.getPath() : ImagePaths.BLACK_KNIGHT.getPath(), board);
                                default -> throw new IllegalStateException("Unexpected selection: " + button.getText());
                            };
                            if (promotedPawn.isWhite()) {
                                WHITE_PIECES.set(WHITE_PIECES.indexOf(activePieceAction), promotedPawn);
                            } else {
                                BLACK_PIECES.set(BLACK_PIECES.indexOf(activePieceAction), promotedPawn);
                            }
                            board.getAttackerMap().updatePieceAttackSquares(promotedPawn);
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
                if (castedKing.isWhite())
                    for (Piece piece : board.getBlackPieces()) {
                        if (piece instanceof PinPiece)
                            board.getAttackerMap().updatePieceAttackSquares(piece);
                    }
                else
                    for (Piece piece : board.getWhitePieces())
                        if (piece instanceof PinPiece)
                            board.getAttackerMap().updatePieceAttackSquares(piece);

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
