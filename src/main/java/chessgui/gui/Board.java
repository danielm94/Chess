package chessgui.gui;

import chessgui.piece.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Board extends JComponent {
    private int turnCounter = 1;
    private static final Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    private final int SQUARE_WIDTH = 65;
    private final List<King> KINGS;
    public ArrayList<Piece> whitePieces;
    public ArrayList<Piece> blackPieces;
    public ArrayList<DrawingShape> staticShapes;
    public ArrayList<DrawingShape> pieceGraphics;
    private Piece activePiece;
    private final String BOARD_FILE_PATH = ImagePaths.CHESSBOARD.getPath();
    private boolean waitingForPromotion = false;
    private final AttackerMap ATTACKER_MAP;
    private final BoardComponent GUI;

    public void initGrid() {
        King whiteKing = new King(0, 4, true, ImagePaths.WHITE_KING.getPath(), this);
        whitePieces.add(whiteKing);
        KINGS.add(whiteKing);
        whitePieces.add(new Queen(0, 3, true, ImagePaths.WHITE_QUEEN.getPath(), this));
        whitePieces.add(new Bishop(0, 2, true, ImagePaths.WHITE_BISHOP.getPath(), this));
        whitePieces.add(new Bishop(0, 5, true, ImagePaths.WHITE_BISHOP.getPath(), this));
        whitePieces.add(new Knight(0, 1, true, ImagePaths.WHITE_KNIGHT.getPath(), this));
        whitePieces.add(new Knight(0, 6, true, ImagePaths.WHITE_KNIGHT.getPath(), this));
        whitePieces.add(new Rook(0, 0, true, ImagePaths.WHITE_ROOK.getPath(), this));
        whitePieces.add(new Rook(0, 7, true, ImagePaths.WHITE_ROOK.getPath(), this));
        whitePieces.add(new Pawn(1, 0, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(1, 1, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(1, 2, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(1, 3, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(1, 4, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(1, 5, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(1, 6, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(1, 7, true, ImagePaths.WHITE_PAWN.getPath(), this));

        King blackKing = new King(7, 4, false, ImagePaths.BLACK_KING.getPath(), this);
        blackPieces.add(blackKing);
        KINGS.add(blackKing);
        blackPieces.add(new Queen(7, 3, false, ImagePaths.BLACK_QUEEN.getPath(), this));
        blackPieces.add(new Bishop(7, 2, false, ImagePaths.BLACK_BISHOP.getPath(), this));
        blackPieces.add(new Bishop(7, 5, false, ImagePaths.BLACK_BISHOP.getPath(), this));
        blackPieces.add(new Knight(7, 1, false, ImagePaths.BLACK_KNIGHT.getPath(), this));
        blackPieces.add(new Knight(7, 6, false, ImagePaths.BLACK_KNIGHT.getPath(), this));
        blackPieces.add(new Rook(7, 0, false, ImagePaths.BLACK_ROOK.getPath(), this));
        blackPieces.add(new Rook(7, 7, false, ImagePaths.BLACK_ROOK.getPath(), this));
        blackPieces.add(new Pawn(6, 0, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(6, 1, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(6, 2, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(6, 3, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(6, 4, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(6, 5, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(6, 6, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(6, 7, false, ImagePaths.BLACK_PAWN.getPath(), this));
    }

    public Board(BoardComponent gui) {
        staticShapes = new ArrayList<>();
        pieceGraphics = new ArrayList<>();
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        KINGS = new ArrayList<>();
        initGrid();
        this.setBackground(new Color(37, 13, 84));
        this.setPreferredSize(new Dimension(520, 520));
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));
        this.addMouseListener(new ClickHandler(this));
        this.setVisible(true);
        this.requestFocus();
        ATTACKER_MAP = new AttackerMap(this);
        this.GUI = gui;
        drawBoard();
    }


    protected void drawBoard() {
        pieceGraphics.clear();
        staticShapes.clear();

        Image board = loadImage(BOARD_FILE_PATH);
        staticShapes.add(new DrawingImage(board, new Rectangle2D.Double(0, 0, board.getWidth(null), board.getHeight(null))));
        if (activePiece != null) {
            Image activeSquare = loadImage(ImagePaths.ACTIVE_SQUARE.getPath());
            staticShapes.add(new DrawingImage(activeSquare, new Rectangle2D.Double(SQUARE_WIDTH * activePiece.getCol(), SQUARE_WIDTH * activePiece.getRow(), activeSquare.getWidth(null), activeSquare.getHeight(null))));
            // paintValidMoves();
        }
        for (Piece white_piece : whitePieces) {
            int ROW = white_piece
                    .getRow();
            int COL = white_piece
                    .getCol();
            Image piece = loadImage(white_piece
                    .getFilePath());
            pieceGraphics.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL, SQUARE_WIDTH * ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        for (Piece black_piece : blackPieces) {
            int ROW = black_piece
                    .getRow();
            int COL = black_piece
                    .getCol();
            Image piece = loadImage(black_piece
                    .getFilePath());
            pieceGraphics.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL, SQUARE_WIDTH * ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        this.repaint();
    }

    private void paintValidMoves() {
        Image attackableSquare = loadImage(ImagePaths.ATTACKABLE_SQUARE.getPath());
        Image validEmpty = loadImage(ImagePaths.VALID_EMPTY_SQUARE.getPath());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                AttackerSquare square = ATTACKER_MAP.getSquare(i, j);
                Piece pieceOnSquare = getPiece(i, j);
                if (pieceOnSquare == null && activePiece.canMove(i, j)) {
                    staticShapes.add(new DrawingImage(validEmpty, new Rectangle2D.Double(
                            (SQUARE_WIDTH * j) + 1, SQUARE_WIDTH * i,
                            validEmpty.getWidth(null), validEmpty.getHeight(null))));
                } else if (square.containsAttacker(activePiece)
                        && pieceOnSquare != null
                        && pieceOnSquare.isWhite() != activePiece.isWhite()
                        && activePiece.getNumPiecesPinningThis() == 0) {
                    staticShapes.add(new DrawingImage(attackableSquare, new Rectangle2D.Double(
                            SQUARE_WIDTH * j, SQUARE_WIDTH * i,
                            attackableSquare.getWidth(null), attackableSquare.getHeight(null))));

                }
            }
        }
    }

    public Piece getPiece(int row, int col) {
        for (Piece p : whitePieces) {
            if (row == p.getRow() && col == p.getCol()) {
                return p;
            }
        }
        for (Piece p : blackPieces) {
            if (p.getRow() == row && p.getCol() == col) {
                return p;
            }
        }
        return null;
    }

    public void removePiece(Piece piece) {
        if (piece == null) return;
        if (piece.isWhite()) whitePieces.remove(piece);
        else blackPieces.remove(piece);
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public int getSquareWidth() {
        return SQUARE_WIDTH;
    }

    public Piece getActivePiece() {
        return activePiece;
    }

    public ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    public boolean isWaitingForPromotion() {
        return waitingForPromotion;
    }

    public void setWaitingForPromotion(boolean waitingForPromotion) {
        this.waitingForPromotion = waitingForPromotion;
    }

    public void setActivePiece(Piece activePiece) {
        this.activePiece = activePiece;
    }

    public void movePiece(Piece piece, int destRow, int destCol) {
        piece.setRow(destRow);
        piece.setCol(destCol);
        ATTACKER_MAP.updatePieceAttackSquares(piece);
        Set<Piece> piecesAttackingDest = ATTACKER_MAP.getSquare(destRow, destCol).getAttackingPieces();
        List<Piece> test = new ArrayList<>(piecesAttackingDest);
        for (Piece p : test)
            ATTACKER_MAP.updatePieceAttackSquares(p);
    }

    public King getBlackKing() {
        for (King king : KINGS) {
            if (!king.isWhite()) {
                return king;
            }
        }
        throw new IllegalStateException("Black king is not currently present on board. Something has gone horribly wrong.");
    }

    public King getWhiteKing() {
        for (King king : KINGS) {
            if (king.isWhite()) {
                return king;
            }
        }
        throw new IllegalStateException("White king is not currently present on board. Something has gone horribly wrong.");
    }

    public AttackerMap getAttackerMap() {
        return ATTACKER_MAP;
    }

    protected void advanceTurn() {
        activePiece = null;
        turnCounter++;
    }

    private void adjustShapePositions(double dx, double dy) {

        staticShapes.get(0)
                    .adjustPosition(dx, dy);
        this.repaint();

    }


    private Image loadImage(String imageFile) {
        try {
            //return ImageIO.read(new File(imageFile));
            return ImageIO.read(ClassLoader.getSystemResource(imageFile));
        } catch (IOException e) {
            return NULL_IMAGE;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawBackground(g2);
        drawShapes(g2);

    }

    private void drawBackground(Graphics2D g2) {
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
    }


    private void drawShapes(Graphics2D g2) {
        for (DrawingShape shape : staticShapes) {
            // g2.rotate(180, 0, 0);
            shape.draw(g2);
        }
        for (DrawingShape shape : pieceGraphics) {
            // g2.rotate(180, 0, 0);
            shape.draw(g2);
        }
    }
}



