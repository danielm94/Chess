package chessgui.board;

import chessgui.gui.*;
import chessgui.piece.*;

import javax.swing.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 5";
    private int turnCounter = 1;
    private final List<King> kings;
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;
    private Piece activePiece;
    private final AttackerMap attackerMap;
    private final BoardComponent gui;
    private int fiftyMoveRuleCounter = 0;
    private boolean isWhitesTurn = true;
    private boolean waitingForDialogExit = false;
    private boolean stateOfCheck = false;

    public Board(BoardComponent gui) {
        this.gui = gui;
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        kings = new ArrayList<>();
        setUpBoard(null);
        attackerMap = new AttackerMap(this);
    }

    public Board(BoardComponent gui, String FEN) {
        this.gui = gui;
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        kings = new ArrayList<>();
        setUpBoard(FEN);
        attackerMap = new AttackerMap(this);
    }

    public void setUpBoard(String FEN) {
        String fen = FEN == null || FEN.equals("") ?
                STARTING_FEN : FEN;

        //get turn from FEN
        int turn = 0;
        int index = fen.length() - 1;
        int n = 0;
        while (Character.isDigit(fen.charAt(index))) {
            char c = fen.charAt(index--);
            turn += Character.getNumericValue(c) * Math.pow(10, n++);
        }
        turnCounter = turn;
        int postLayoutIndex = 0;
        char[] fenCharArr = fen.toCharArray();
        int row = 7;
        int col = 0;

        for (int i = 0; i < fenCharArr.length && fenCharArr[i] != ' '; i++, postLayoutIndex = i) {
            char c = fenCharArr[i];
            if (c == '/') {
                row--;
                col = 0;
            } else if (Character.isDigit(c)) {
                col += Character.getNumericValue(c);
            } else if (Character.isLetter(c)) {
                addPieceToBoard(c, row, col++);
            }
        }

        boolean playerToMoveFound = false;
        for (int i = postLayoutIndex; i < fenCharArr.length; i++) {
            char c = fenCharArr[i];
            if (Character.toLowerCase(c) == 'k' || Character.toLowerCase(c) == 'q') {
                setCastlingFlags(c);
            } else if ((c == 'b' || c == 'w') && !playerToMoveFound) {
                playerToMoveFound = true;
                isWhitesTurn = c == 'w';
            } else if (c >= 97 && c <= 104) {
                //If the character is an en passant notation.
                col = c - 'a';
                row = Character.getNumericValue(fenCharArr[i + 1]);
                Pawn pawn = (Pawn) getPiece(row, col);
                pawn.setTurnMoved(turnCounter - 1);
                pawn.setHasMoved(true);
                pawn.setMovedTwoSpaces(true);
                i++;
            } else if (Character.isDigit(c)) {
                StringBuilder fiftyMoveBuilder = new StringBuilder();
                int count = i;
                while (Character.isDigit(fenCharArr[count])) {
                    char ch = fenCharArr[count++];
                    fiftyMoveBuilder.append(ch);
                }
                fiftyMoveRuleCounter = Integer.parseInt(fiftyMoveBuilder.toString());

                return;
            }
        }

    }

    public void setCastlingFlags(char piece) {
        switch (piece) {
            case 'K' -> {
                Rook kingside = (Rook) getPiece(0, 7);
                kingside.setCanCastle(true);
                King whiteKing = getKing(true);
                whiteKing.setCanCastle(true);
            }
            case 'k' -> {
                Rook kingside = (Rook) getPiece(7, 7);
                kingside.setCanCastle(true);
                King blackKing = getKing(false);
                blackKing.setCanCastle(true);
            }
            case 'Q' -> {
                Rook queenside = (Rook) getPiece(0, 0);
                queenside.setCanCastle(true);
                King whiteKing = getKing(true);
                whiteKing.setCanCastle(true);
            }
            case 'q' -> {
                Rook queenside = (Rook) getPiece(7, 0);
                queenside.setCanCastle(true);
                King blackKing = getKing(false);
                blackKing.setCanCastle(true);
            }

        }
    }


    public void addPieceToBoard(char piece, int row, int col) {
        switch (piece) {
            case 'p' -> {
                Pawn pawn = new Pawn(row, col, false, ImagePaths.BLACK_PAWN.getPath(), this);
                if (row != 6) pawn.setHasMoved(true);
                blackPieces.add(pawn);
            }
            case 'P' -> {
                Pawn pawn = new Pawn(row, col, true, ImagePaths.WHITE_PAWN.getPath(), this);
                if (row != 1) pawn.setHasMoved(true);
                whitePieces.add(pawn);
            }
            case 'r' -> blackPieces.add(new Rook(row, col, false, ImagePaths.BLACK_ROOK.getPath(), this));
            case 'R' -> whitePieces.add(new Rook(row, col, true, ImagePaths.WHITE_ROOK.getPath(), this));
            case 'n' -> blackPieces.add(new Knight(row, col, false, ImagePaths.BLACK_KNIGHT.getPath(), this));
            case 'N' -> whitePieces.add(new Knight(row, col, true, ImagePaths.WHITE_KNIGHT.getPath(), this));
            case 'b' -> blackPieces.add(new Bishop(row, col, false, ImagePaths.BLACK_BISHOP.getPath(), this));
            case 'B' -> whitePieces.add(new Bishop(row, col, true, ImagePaths.WHITE_BISHOP.getPath(), this));
            case 'q' -> blackPieces.add(new Queen(row, col, false, ImagePaths.BLACK_QUEEN.getPath(), this));
            case 'Q' -> whitePieces.add(new Queen(row, col, true, ImagePaths.WHITE_QUEEN.getPath(), this));
            case 'k' -> {
                King blackKing = new King(row, col, false, ImagePaths.BLACK_KING.getPath(), this);
                blackPieces.add(blackKing);
                if (getKing(false) == null)
                    kings.add(blackKing);
            }
            case 'K' -> {
                King whiteKing = new King(row, col, true, ImagePaths.WHITE_KING.getPath(), this);
                whitePieces.add(whiteKing);
                if (getKing(true) == null)
                    kings.add(whiteKing);
            }
            default -> throw new InvalidParameterException(piece + " is not a valid character in FEN");
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

    public Piece getActivePiece() {
        return activePiece;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    public boolean isWaitingForDialogExit() {
        return waitingForDialogExit;
    }

    public void setWaitingForDialogExit(boolean waitingForDialogExit) {
        this.waitingForDialogExit = waitingForDialogExit;
    }

    public boolean inStateOfCheck() {
        return stateOfCheck;
    }


    public void setActivePiece(Piece activePiece) {
        this.activePiece = activePiece;
    }

    public void movePiece(Piece piece, int destRow, int destCol) {
        piece.setRow(destRow);
        piece.setCol(destCol);
        attackerMap.updatePieceAttackSquares(piece);
        for (Piece blackPiece : blackPieces)
            if ((blackPiece instanceof PinPiece || blackPiece instanceof King) && blackPiece != piece)
                attackerMap.updatePieceAttackSquares(blackPiece);
        for (Piece whitePiece : whitePieces)
            if ((whitePiece instanceof PinPiece || whitePiece instanceof King) && whitePiece != piece)
                attackerMap.updatePieceAttackSquares(whitePiece);
    }

    public King getKing(boolean isWhite) {
        for (King king : kings) {
            if (king.isWhite() == isWhite) {
                return king;
            }
        }
        return null;
    }

    public AttackerMap getAttackerMap() {
        return attackerMap;
    }

    public void advanceTurn() {
        isWhitesTurn = !isWhitesTurn;
        activePiece = null;
        turnCounter++;
        if (getKing(isWhitesTurn).isMated()) {
            setWaitingForDialogExit(true);
            JDialog checkMateDialog = new GameOverDialog(this, "Checkmate!", "Checkmate! " + (isWhitesTurn ? "Black " : "White ") + "wins!");
            checkMateDialog.setVisible(true);
        } else if (stalemateReached()) {
            setWaitingForDialogExit(true);
            JDialog stalemateDialog = new GameOverDialog(this, "Stalemate!",
                    "Stalemate! What were you thinking " + (isWhitesTurn ? "Black?" : "White?"));
            stalemateDialog.setVisible(true);
        }
    }

    public BoardComponent getGui() {
        return gui;
    }

    public void evaluateMove(int clickedRow, int clickedColumn) {
        Piece clickedPiece = getPiece(clickedRow, clickedColumn);

        if (activePiece == null && clickedPiece != null &&
                ((isWhitesTurn && clickedPiece.isWhite()) || (!isWhitesTurn && !clickedPiece.isWhite()))) {
            setActivePiece(clickedPiece);
        } else if (activePiece != null && activePiece.getCol() == clickedColumn && activePiece.getRow() == clickedRow) {
            setActivePiece(null);
        } else if (activePiece != null && activePiece.getClass().equals(King.class) && Math.abs(clickedColumn - activePiece.getCol()) > 1) { // If player is trying to castle.
            King king = (King) activePiece;
            if (king.canCastle(clickedRow, clickedColumn)) {
                int rookCol = clickedColumn > king.getCol() ? 7 : 0;
                int rookDestCol = clickedColumn <= 2 ? 3 : 5;
                /*If the player clicked on column 1, (0 indexed) set it the king's destination
                to column 2, which is the appropriate column when castling queen side.*/
                int kingCol = clickedColumn == 1 ? 2 : clickedColumn;
                Piece rook = getPiece(clickedRow, rookCol);
                movePiece(king, clickedRow, kingCol);
                king.setCanCastle(false);
                movePiece(rook, clickedRow, rookDestCol);
                if (gui.getParentFrame().isSoundEnabled()) SoundEffects.playMoveSound();
                advanceTurn();
            } else {
                if (gui.getParentFrame().isSoundEnabled()) SoundEffects.playInvalidMoveSound();
            }
        } else if (activePiece != null && activePiece.canMove(clickedRow, clickedColumn)
                && ((isWhitesTurn && activePiece.isWhite()) || (!isWhitesTurn && !activePiece.isWhite()))) {
            // if piece is there, remove it so we can be there
            if (clickedPiece != null) {
                attackerMap.clearPiecePins(clickedPiece);
                attackerMap.clearPieceAttackSquares(clickedPiece);
                removePiece(clickedPiece);
            } else {
                int enCroissantRow = activePiece.isWhite() ? clickedRow - 1 : clickedRow + 1;
                if (activePiece.getClass().equals(Pawn.class) && ((Pawn) activePiece).canEncroissant(enCroissantRow, clickedColumn)) {
                    clickedPiece = getPiece(enCroissantRow, clickedColumn);
                    attackerMap.clearPiecePins(clickedPiece);
                    attackerMap.clearPieceAttackSquares(clickedPiece);
                    removePiece(clickedPiece);
                }
            }
            // do move
            int prevRow = activePiece.getRow();
            movePiece(activePiece, clickedRow, clickedColumn);
            // if piece is a pawn set has_moved to true
            if (activePiece.getClass().equals(Pawn.class)) {
                Pawn castedPawn = (Pawn) activePiece;
                if (!castedPawn.getHasMoved()) {
                    castedPawn.setHasMoved(true);
                    castedPawn.setTurnMoved(turnCounter);
                    castedPawn.setMovedTwoSpaces(Math.abs(prevRow - clickedRow) == 2);
                }
                if ((castedPawn.getRow() == 7 && castedPawn.isWhite()) || (castedPawn.getRow() == 0 && !castedPawn.isWhite())) {
                    JDialog promotionDialog = new PromotionDialog(this, "Test");
                    promotionDialog.setVisible(true);
                    setWaitingForDialogExit(true);
                }
            } else if (activePiece.getClass().equals(King.class)) {
                King castedKing = (King) activePiece;
                castedKing.setCanCastle(false);
                if (castedKing.isWhite())
                    for (Piece piece : blackPieces) {
                        if (piece instanceof PinPiece)
                            attackerMap.updatePieceAttackSquares(piece);
                    }
                else
                    for (Piece piece : whitePieces)
                        if (piece instanceof PinPiece)
                            attackerMap.updatePieceAttackSquares(piece);

            } else if (activePiece.getClass().equals(Rook.class)) {
                Rook castedRook = (Rook) activePiece;
                castedRook.setCanCastle(false);
            }
            if ((isWhitesTurn && getKing(false).isChecked())
                    || !isWhitesTurn && getKing(true).isChecked()) {
                stateOfCheck = true;
                if (gui.getParentFrame().isSoundEnabled()) SoundEffects.playCheckSound();
            } else {
                stateOfCheck = false;
                if (gui.getParentFrame().isSoundEnabled()) SoundEffects.playMoveSound();
            }
            if (!isWaitingForDialogExit()) {
                advanceTurn();
            }
        } else if (activePiece != null) {
            if (gui.getParentFrame().isSoundEnabled()) SoundEffects.playInvalidMoveSound();
        }
        gui.drawBoard();
    }

    private boolean stalemateReached() {
        List<Piece> pieces = isWhitesTurn ? whitePieces : blackPieces;
        for (Piece piece : pieces) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (piece.canMove(i, j)) return false;
                }
            }
        }
        return true;
    }
}



