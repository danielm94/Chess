package chessgui.gui;

import chessgui.board.Board;
import chessgui.piece.King;
import chessgui.piece.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BoardComponent extends JComponent {
    private final Board BOARD;
    private static final int SQUARE_WIDTH = 65;
    private final String BOARD_FILE_PATH = ImagePaths.CHESSBOARD.getPath();
    private final List<DrawingShape> STATIC_SHAPES;
    private final List<DrawingShape> PIECE_GRAPHICS;
    private static final Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    private BoardFrame PARENT_FRAME;

    public BoardComponent() {
        this.BOARD = new Board(this);
        STATIC_SHAPES = new ArrayList<>();
        PIECE_GRAPHICS = new ArrayList<>();
        this.setBackground(new Color(37, 13, 84));
        this.setPreferredSize(new Dimension(520, 520));
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));
        this.addMouseListener(new ClickHandler(BOARD));
        this.setVisible(true);
        this.requestFocus();
        drawBoard();
    }

    public BoardComponent(BoardFrame parent, String FEN) {
        this.PARENT_FRAME = parent;
        this.BOARD = new Board(this, FEN);
        STATIC_SHAPES = new ArrayList<>();
        PIECE_GRAPHICS = new ArrayList<>();
        this.setBackground(new Color(37, 13, 84));
        this.setPreferredSize(new Dimension(520 + PARENT_FRAME.getJMenuBar().getWidth(), 520 + PARENT_FRAME.getJMenuBar().getHeight()) );
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));
        this.addMouseListener(new ClickHandler(BOARD));
        this.setVisible(true);
        this.requestFocus();
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        drawBoard();
    }

    public void drawBoard() {
        PIECE_GRAPHICS.clear();
        STATIC_SHAPES.clear();

        Image boardImage = loadImage(BOARD_FILE_PATH);
        STATIC_SHAPES.add(new DrawingImage(boardImage, new Rectangle2D.Double(0, 0, boardImage.getWidth(null), boardImage.getHeight(null))));
        Piece activePiece = BOARD.getActivePiece();
        if (activePiece != null) {
            Image activeSquare = loadImage(ImagePaths.ACTIVE_SQUARE.getPath());
            STATIC_SHAPES.add(new DrawingImage(activeSquare, new Rectangle2D.Double(SQUARE_WIDTH * activePiece.getCol(), SQUARE_WIDTH * activePiece.getRow(), activeSquare.getWidth(null), activeSquare.getHeight(null))));
            if (PARENT_FRAME.isLegalMoveDrawingEnabled()) paintValidMoves();
        }
        for (Piece white_piece : BOARD.getWhitePieces()) {
            int ROW = white_piece
                    .getRow();
            int COL = white_piece
                    .getCol();
            Image piece = loadImage(white_piece
                    .getFilePath());
            PIECE_GRAPHICS.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL, SQUARE_WIDTH * ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        for (Piece black_piece : BOARD.getBlackPieces()) {
            int ROW = black_piece
                    .getRow();
            int COL = black_piece
                    .getCol();
            Image piece = loadImage(black_piece
                    .getFilePath());
            PIECE_GRAPHICS.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL, SQUARE_WIDTH * ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        this.repaint();
    }

    private void adjustShapePositions(double dx, double dy) {

        STATIC_SHAPES.get(0)
                     .adjustPosition(dx, dy);
        this.repaint();

    }

    private void paintValidMoves() {
        Image attackableSquare = loadImage(ImagePaths.ATTACKABLE_SQUARE.getPath());
        Image validEmpty = loadImage(ImagePaths.VALID_EMPTY_SQUARE.getPath());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece pieceOnSquare = BOARD.getPiece(i, j);
                Piece activePiece = BOARD.getActivePiece();
                if (activePiece.canMove(i, j) || activePiece instanceof King && ((King) activePiece).canCastle(i, j)) {
                    if (pieceOnSquare == null) {
                        STATIC_SHAPES.add(new DrawingImage(validEmpty, new Rectangle2D.Double(
                                (SQUARE_WIDTH * j), SQUARE_WIDTH * i,
                                validEmpty.getWidth(null), validEmpty.getHeight(null))));
                    } else {
                        STATIC_SHAPES.add(new DrawingImage(attackableSquare, new Rectangle2D.Double(
                                SQUARE_WIDTH * j, SQUARE_WIDTH * i,
                                attackableSquare.getWidth(null), attackableSquare.getHeight(null))));
                    }
                }
            }
        }
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
        for (DrawingShape shape : STATIC_SHAPES) {
            shape.draw(g2);
        }
        for (DrawingShape shape : PIECE_GRAPHICS) {
            shape.draw(g2);
        }
    }

    public static int getSquareWidth() {
        return SQUARE_WIDTH;
    }

    public BoardFrame getParentFrame() {
        return PARENT_FRAME;
    }
}
