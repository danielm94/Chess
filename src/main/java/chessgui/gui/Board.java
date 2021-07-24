package chessgui.gui;

import chessgui.piece.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


public class Board extends JComponent {
    private int turnCounter = 1;
    private static final Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    private final int SQUARE_WIDTH = 65;
    public ArrayList<Piece> whitePieces;
    public ArrayList<Piece> blackPieces;
    public ArrayList<Piece> kings;
    public ArrayList<Piece> queens;
    public ArrayList<Piece> rooks;
    public ArrayList<Piece> bishops;
    public ArrayList<DrawingShape> staticShapes;
    public ArrayList<DrawingShape> pieceGraphics;
    private Piece activePiece;
    private final String BOARD_FILE_PATH = ImagePaths.CHESSBOARD.getPath();
    private boolean waitingForPromotion = false;
    private AttackerMap attackerMap;

    public void initGrid() {
        whitePieces.add(new King(3, 0, true, ImagePaths.WHITE_KING.getPath(), this));
        whitePieces.add(new Queen(4, 0, true, ImagePaths.WHITE_QUEEN.getPath(), this));
        whitePieces.add(new Bishop(2, 0, true, ImagePaths.WHITE_BISHOP.getPath(), this));
        whitePieces.add(new Bishop(5, 0, true, ImagePaths.WHITE_BISHOP.getPath(), this));
        whitePieces.add(new Knight(1, 0, true, ImagePaths.WHITE_KNIGHT.getPath(), this));
        whitePieces.add(new Knight(6, 0, true, ImagePaths.WHITE_KNIGHT.getPath(), this));
        whitePieces.add(new Rook(0, 0, true, ImagePaths.WHITE_ROOK.getPath(), this));
        whitePieces.add(new Rook(7, 0, true, ImagePaths.WHITE_ROOK.getPath(), this));
        whitePieces.add(new Pawn(0, 1, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(1, 1, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(2, 1, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(3, 1, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(4, 1, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(5, 1, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(6, 1, true, ImagePaths.WHITE_PAWN.getPath(), this));
        whitePieces.add(new Pawn(7, 1, true, ImagePaths.WHITE_PAWN.getPath(), this));

        blackPieces.add(new King(3, 7, false, ImagePaths.BLACK_KING.getPath(), this));
        blackPieces.add(new Queen(4, 7, false, ImagePaths.BLACK_QUEEN.getPath(), this));
        blackPieces.add(new Bishop(2, 7, false, ImagePaths.BLACK_BISHOP.getPath(), this));
        blackPieces.add(new Bishop(5, 7, false, ImagePaths.BLACK_BISHOP.getPath(), this));
        blackPieces.add(new Knight(1, 7, false, ImagePaths.BLACK_KNIGHT.getPath(), this));
        blackPieces.add(new Knight(6, 7, false, ImagePaths.BLACK_KNIGHT.getPath(), this));
        blackPieces.add(new Rook(0, 7, false, ImagePaths.BLACK_ROOK.getPath(), this));
        blackPieces.add(new Rook(7, 7, false, ImagePaths.BLACK_ROOK.getPath(), this));
        blackPieces.add(new Pawn(0, 6, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(1, 6, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(2, 6, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(3, 6, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(4, 6, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(5, 6, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(6, 6, false, ImagePaths.BLACK_PAWN.getPath(), this));
        blackPieces.add(new Pawn(7, 6, false, ImagePaths.BLACK_PAWN.getPath(), this));

    }

    public Board() {
        staticShapes = new ArrayList<>();
        pieceGraphics = new ArrayList<>();
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        initGrid();
        this.setBackground(new Color(37, 13, 84));
        this.setPreferredSize(new Dimension(520, 520));
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));
        this.addMouseListener(new ClickHandler(this));
        this.setVisible(true);
        this.requestFocus();
        attackerMap = new AttackerMap(this);
        drawBoard();
    }


    protected void drawBoard() {
        pieceGraphics.clear();
        staticShapes.clear();
        //initGrid();

        Image board = loadImage(BOARD_FILE_PATH);
        staticShapes.add(new DrawingImage(board, new Rectangle2D.Double(0, 0, board.getWidth(null), board.getHeight(null))));
        if (activePiece != null) {
            Image active_square = loadImage(ImagePaths.ACTIVE_SQUARE.getPath());
            staticShapes.add(new DrawingImage(active_square, new Rectangle2D.Double(SQUARE_WIDTH * activePiece.getCol(), SQUARE_WIDTH * activePiece.getRow(), active_square.getWidth(null), active_square.getHeight(null))));
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


    public Piece getPiece(int x, int y) {
        for (Piece p : whitePieces) {
            if (p.getCol() == x && p.getRow() == y) {
                return p;
            }
        }
        for (Piece p : blackPieces) {
            if (p.getCol() == x && p.getRow() == y) {
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



