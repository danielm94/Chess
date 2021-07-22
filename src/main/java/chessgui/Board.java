package chessgui;

import chessgui.piece.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Board extends JComponent {
    private int turnCounter = 1;
    private static final Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

    private final int SQUARE_WIDTH = 65;
    public ArrayList<Piece> whitePieces;
    public ArrayList<Piece> blackPieces;


    public ArrayList<DrawingShape> staticShapes;
    public ArrayList<DrawingShape> pieceGraphics;

    public Piece activePieces;

    private final int ROWS = 8;
    private final int COLS = 8;
    private final Integer[][] BOARD_GRID;
    private final String BOARD_FILE_PATH = ImagePaths.CHESSBOARD.getPath();

    public void initGrid() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                BOARD_GRID[i][j] = 0;
            }
        }

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
        BOARD_GRID = new Integer[ROWS][COLS];
        staticShapes = new ArrayList<>();
        pieceGraphics = new ArrayList<>();
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();

        initGrid();

        this.setBackground(new Color(37, 13, 84));
        this.setPreferredSize(new Dimension(520, 520));
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));

        this.addMouseListener(mouseAdapter);
        this.addComponentListener(componentAdapter);
        this.addKeyListener(keyAdapter);


        this.setVisible(true);
        this.requestFocus();
        drawBoard();
    }


    private void drawBoard() {
        pieceGraphics.clear();
        staticShapes.clear();
        //initGrid();

        Image board = loadImage(BOARD_FILE_PATH);
        staticShapes.add(new DrawingImage(board, new Rectangle2D.Double(0, 0, board.getWidth(null), board.getHeight(null))));
        if (activePieces != null) {
            Image active_square = loadImage(ImagePaths.ACTIVE_SQUARE.getPath());
            staticShapes.add(new DrawingImage(active_square, new Rectangle2D.Double(SQUARE_WIDTH * activePieces.getX(), SQUARE_WIDTH * activePieces.getY(), active_square.getWidth(null), active_square.getHeight(null))));
        }
        for (Piece white_piece : whitePieces) {
            int COL = white_piece
                    .getX();
            int ROW = white_piece
                    .getY();
            Image piece = loadImage(white_piece
                    .getFilePath());
            pieceGraphics.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL, SQUARE_WIDTH * ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        for (Piece black_piece : blackPieces) {
            int COL = black_piece
                    .getX();
            int ROW = black_piece
                    .getY();
            Image piece = loadImage(black_piece
                    .getFilePath());
            pieceGraphics.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL, SQUARE_WIDTH * ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        this.repaint();
    }


    public Piece getPiece(int x, int y) {
        for (Piece p : whitePieces) {
            if (p.getX() == x && p.getY() == y) {
                return p;
            }
        }
        for (Piece p : blackPieces) {
            if (p.getX() == x && p.getY() == y) {
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

    private MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {


        }

        @Override
        public void mousePressed(MouseEvent e) {
            int dX = e.getX();
            int dY = e.getY();
            int clickedRow = dY / SQUARE_WIDTH;
            int clickedColumn = dX / SQUARE_WIDTH;
          //  System.out.println("X = " + clickedColumn + " Y = " + clickedRow);
            boolean isWhitesTurn = turnCounter % 2 == 1;

            Piece clicked_piece = getPiece(clickedColumn, clickedRow);


            if (activePieces == null && clicked_piece != null &&
                    ((isWhitesTurn && clicked_piece.isWhite()) || (!isWhitesTurn && !clicked_piece.isWhite()))) {
                activePieces = clicked_piece;
            } else if (activePieces != null && activePieces.getX() == clickedColumn && activePieces.getY() == clickedRow) {
                activePieces = null;
            } else if (activePieces != null && activePieces.getClass().equals(King.class) && Math.abs(clickedColumn - activePieces.getX()) > 1) { // If player is trying to castle.
                King king = (King) activePieces;
                if (king.canCastle(clickedColumn, clickedRow)) {
                    int rookX = clickedColumn > king.getX() ? 7 : 0;
                    int rookDestination = clickedColumn == 1 ? 2 : 4;
                    Piece rook = getPiece(rookX, clickedRow);
                    king.setX(clickedColumn);
                    king.setY(clickedRow);
                    king.setHasMoved(true);
                    rook.setX(rookDestination);
                    rook.setY(clickedRow);
                    advanceTurn();
                }
            } else if (activePieces != null && activePieces.canMove(clickedColumn, clickedRow)
                    && ((isWhitesTurn && activePieces.isWhite()) || (!isWhitesTurn && !activePieces.isWhite()))) {
                // if piece is there, remove it so we can be there
                if (clicked_piece != null) {
                    removePiece(clicked_piece);
                }
                // do move
                int prevX = activePieces.getX();
                int prevY = activePieces.getY();
                activePieces.setX(clickedColumn);
                activePieces.setY(clickedRow);

                // if piece is a pawn set has_moved to true
                if (activePieces.getClass().equals(Pawn.class)) {
                    Pawn castedPawn = (Pawn) activePieces;
                    castedPawn.setHasMoved(true);
                    castedPawn.setTurnMoved(turnCounter);
                    castedPawn.setMovedTwoSpaces(Math.abs(prevY - clickedRow) == 2);
                } else if (activePieces.getClass().equals(King.class)) {
                    King castedKing = (King) activePieces;
                    castedKing.setHasMoved(true);
                } else if (activePieces.getClass().equals(Rook.class)) {
                    Rook castedRook = (Rook) activePieces;
                    castedRook.setHasMoved(true);
                }
                advanceTurn();
            }

            drawBoard();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
        }


    };

    private void advanceTurn() {
        activePieces = null;
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

    private ComponentAdapter componentAdapter = new ComponentAdapter() {

        @Override
        public void componentHidden(ComponentEvent e) {

        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentResized(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }
    };

    private KeyAdapter keyAdapter = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }
    };

}


interface DrawingShape {
    boolean contains(Graphics2D g2, double x, double y);

    void adjustPosition(double dx, double dy);

    void draw(Graphics2D g2);
}


class DrawingImage implements DrawingShape {

    public Image image;
    public Rectangle2D rect;

    public DrawingImage(Image image, Rectangle2D rect) {
        this.image = image;
        this.rect = rect;
    }

    @Override
    public boolean contains(Graphics2D g2, double x, double y) {
        return rect.contains(x, y);
    }

    @Override
    public void adjustPosition(double dx, double dy) {
        rect.setRect(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());
    }

    @Override
    public void draw(Graphics2D g2) {
        Rectangle2D bounds = rect.getBounds2D();
        g2.drawImage(image, (int) bounds.getMinX(), (int) bounds.getMinY(), (int) bounds.getMaxX(), (int) bounds.getMaxY(),
                0, 0, image.getWidth(null), image.getHeight(null), null);
    }
}

