package chessgui.gui;

import chessgui.board.Board;
import chessgui.gui.resource_paths.ImagePaths;
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


public class BoardComponent extends JPanel {
    private static BoardComponent boardComponent;

    private static final int SQUARE_WIDTH = 65;
    private final String BOARD_FILE_PATH = ImagePaths.CHESSBOARD.getPath();
    private List<DrawingShape> staticShapes;
    private List<DrawingShape> pieceGraphics;
    private static final Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    private BoardFrame parentFrame;

    private BoardComponent() {
    }

    public static BoardComponent get() {
        if (boardComponent == null) boardComponent = new BoardComponent();
        return boardComponent;
    }

    public void setUpBoardComponent(BoardFrame parentFrame) {
        this.parentFrame = parentFrame;
        staticShapes = new ArrayList<>();
        pieceGraphics = new ArrayList<>();
        this.addMouseListener(new ClickHandler());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        drawBoard();
    }

    public void drawBoard() {
        boolean isWhitesTurn = Board.get().isWhitesTurn();
        pieceGraphics.clear();
        staticShapes.clear();

        Image boardImage = loadImage(BOARD_FILE_PATH);
        staticShapes.add(new DrawingImage(boardImage, new Rectangle2D.Double(0, 0, boardImage.getWidth(null), boardImage.getHeight(null))));
        Piece activePiece = Board.get().getActivePiece();
        Piece lastPieceMoved = Board.get().getLastPieceMoved();
        if (activePiece != null) {
            Image activeSquare = loadImage(ImagePaths.ACTIVE_SQUARE.getPath());
            int x = isWhitesTurn ? 7 - activePiece.getCol() : activePiece.getCol();
            int y = isWhitesTurn ? 7 - activePiece.getRow() : activePiece.getRow();
            staticShapes.add(new DrawingImage(activeSquare, new Rectangle2D.Double(SQUARE_WIDTH * x, SQUARE_WIDTH * y, activeSquare.getWidth(null), activeSquare.getHeight(null))));
            if (parentFrame.isLegalMoveDrawingEnabled()) paintValidMoves();
        }
        if (lastPieceMoved != null) {
            Image lastPieceSquare = loadImage(ImagePaths.LAST_MOVED_PIECE.getPath());
            int x = isWhitesTurn ? 7 - lastPieceMoved.getCol() : lastPieceMoved.getCol();
            int y = isWhitesTurn ? 7 - lastPieceMoved.getRow() : lastPieceMoved.getRow();
            staticShapes.add(new DrawingImage(lastPieceSquare, new Rectangle2D.Double(SQUARE_WIDTH * x, SQUARE_WIDTH * y, lastPieceSquare.getWidth(null), lastPieceSquare.getHeight(null))));
        }
        for (Piece white_piece : Board.get().getWhitePieces()) {
            int ROW = white_piece
                    .getRow();
            int COL = white_piece
                    .getCol();
            ROW = isWhitesTurn ? 7 - ROW : ROW;
            COL = isWhitesTurn ? 7 - COL : COL;
            Image piece = loadImage(white_piece
                    .getFilePath());
            pieceGraphics.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL, SQUARE_WIDTH * ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        for (Piece black_piece : Board.get().getBlackPieces()) {
            int ROW = black_piece
                    .getRow();
            int COL = black_piece
                    .getCol();
            ROW = isWhitesTurn ? 7 - ROW : ROW;
            COL = isWhitesTurn ? 7 - COL : COL;
            Image piece = loadImage(black_piece
                    .getFilePath());
            pieceGraphics.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL, SQUARE_WIDTH * ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        this.repaint();
    }

    private void paintValidMoves() {
        boolean isWhitesTurn = Board.get().isWhitesTurn();
        Image attackableSquare = loadImage(ImagePaths.ATTACKABLE_SQUARE.getPath());
        Image validEmpty = loadImage(ImagePaths.VALID_EMPTY_SQUARE.getPath());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece pieceOnSquare = Board.get().getPiece(i, j);
                Piece activePiece = Board.get().getActivePiece();
                if (activePiece.canMove(i, j) || activePiece instanceof King && ((King) activePiece).canCastle(i, j)) {
                    int x = isWhitesTurn ? 7 - j : j;
                    int y = isWhitesTurn ? 7 - i : i;
                    if (pieceOnSquare == null) {

                        staticShapes.add(new DrawingImage(validEmpty, new Rectangle2D.Double(
                                (SQUARE_WIDTH * x), SQUARE_WIDTH * y,
                                validEmpty.getWidth(null), validEmpty.getHeight(null))));
                    } else {
                        staticShapes.add(new DrawingImage(attackableSquare, new Rectangle2D.Double(
                                SQUARE_WIDTH * x, SQUARE_WIDTH * y,
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
        for (DrawingShape shape : staticShapes) {
            shape.draw(g2);
        }
        for (DrawingShape shape : pieceGraphics) {
            shape.draw(g2);
        }
    }

    public static int getSquareWidth() {
        return SQUARE_WIDTH;
    }

    public BoardFrame getParentFrame() {
        return parentFrame;
    }

    public void setParentFrame(BoardFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void dispose() {
        boardComponent = null;
    }
}
