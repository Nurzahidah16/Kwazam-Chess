package Controller;

import Model.*;
import View.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.Border;

public class InputHandler extends MouseAdapter {

    private ChessModel model;
    private Chessboard board;
    private Position selectedPos = null;
    private JLabel draggedPieceLabel = null;
    private ImageIcon draggedPieceIcon = null;
    private static final Border border = BorderFactory.createEmptyBorder();

    // Initializes the input handler with the board and game model
    public InputHandler(Chessboard board, ChessModel model) {
        this.board = board;
        this.model = model;
    }

    // Triggered when a part of the board is clicked (WANG KUANG WEI)
    @Override
    public void mousePressed(MouseEvent e) {
        resetHighlighting();
        int col = calculateBoardCoordinate(e.getX(), board.getWidth(), model.getBoardWidth());
        int row = calculateBoardCoordinate(e.getY(), board.getHeight(), model.getBoardHeight());

        if (board.isFlipped()) {
            col = model.getBoardWidth() - 1 - col;
            row = model.getBoardHeight() - 1 - row;
        }

        selectedPos = new Position(col, row);
        Chesspiece piece = model.getPiece(col, row);

        if (piece != null) {
            if (piece.getColor() == model.getCurrentColor()) {
                board.selectedPiece = piece;
                draggedPieceIcon = piece.getImagePath();
                draggedPieceLabel = new JLabel(draggedPieceIcon);
                draggedPieceLabel.setOpaque(false);
                board.getLayeredPane().add(draggedPieceLabel, JLayeredPane.DRAG_LAYER);
                Point cursorPosition = SwingUtilities.convertPoint(board, e.getX(), e.getY(), board.getLayeredPane());
                draggedPieceLabel.setBounds(cursorPosition.x - draggedPieceIcon.getIconWidth() / 2,
                        cursorPosition.y - draggedPieceIcon.getIconHeight() / 2,
                        draggedPieceIcon.getIconWidth(), draggedPieceIcon.getIconHeight());
                board.repaint();

                // Remove the original icon temporarily while dragging
                JLabel pieceOnBoard = board.boardLabels[row][col];
                pieceOnBoard.setIcon(null);
                pieceOnBoard.repaint();

                // Highlight valid moves for the piece
                indicateValidMoves(piece);
            } else {
                System.out.println("Cannot move opponent's piece.");
            }
        } else {
            System.out.println("No piece at the selected position.");
        }
    }

    // Highlights valid moves for the selected piece (CHIA KOK ANG)
    private void indicateValidMoves(Chesspiece piece) {
        if (piece != null) {
            Set<Position> validMoves = piece.ifValidMove(model);
            for (Position move : validMoves) {
                int row = move.getY();
                int col = move.getX();
                if (board.isFlipped()) {
                    row = model.getBoardHeight() - 1 - row;
                    col = model.getBoardWidth() - 1 - col;
                }
                Chesspiece targetPiece = model.getPiece(move.getX(), move.getY());
                if (targetPiece != null) {
                    board.boardLabels[row][col].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                } else {
                    board.boardLabels[row][col].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                }
            }
        }
    }

    // Resets highlighting on the board (CHIA KOK ANG)
    private void resetHighlighting() {
        for (int r = 0; r < model.getBoardHeight(); r++) {
            for (int c = 0; c < model.getBoardWidth(); c++) {
                board.boardLabels[r][c].setBorder(border);
            }
        }
    }

    // Handles dragging of the selected piece (CHIA KOK ANG)
    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedPieceLabel != null) {
            Point cursorPosition = SwingUtilities.convertPoint(board, e.getX(), e.getY(), board.getLayeredPane());
            draggedPieceLabel.setLocation(cursorPosition.x - draggedPieceIcon.getIconWidth() / 2,
                    cursorPosition.y - draggedPieceIcon.getIconHeight() / 2);
            board.getLayeredPane().repaint();
        }
    }

    // Handles the release of a piece on the board (WANG KUANG WEI)
    @Override
    public void mouseReleased(MouseEvent e) {
        resetHighlighting();
        if (board.selectedPiece != null && selectedPos != null) {
            int col = calculateBoardCoordinate(e.getX(), board.getWidth(), model.getBoardWidth());
            int row = calculateBoardCoordinate(e.getY(), board.getHeight(), model.getBoardHeight());

            if (board.isFlipped()) {
                col = model.getBoardWidth() - 1 - col;
                row = model.getBoardHeight() - 1 - row;
            }

            if (col != selectedPos.getX() || row != selectedPos.getY()) {
                if (model.movePiece(selectedPos.getX(), selectedPos.getY(), col, row)) {
                    board.refreshBoard(model);
                    
                    // Pass the current move and the board state to the isGameOver method
                    if (model.isGameOver(new Position(col, row), board)) {
                        JOptionPane.showMessageDialog(board, "Game Over! " + model.getCurrentPlayer() + " wins!");
                        model.closeGame(board);  // Close game and reset state
                    } else {
                        model.processRound(board.selectedPiece);
                        board.updateTitle(model.getCurrentPlayer(), model.getRound());
                        board.flipBoard(model);
                        board.refreshBoard(model);
                    }
                } else {
                    // Reset the piece's position if the move is invalid
                    board.boardLabels[selectedPos.getY()][selectedPos.getX()].setIcon(draggedPieceIcon);
                    board.boardLabels[selectedPos.getY()][selectedPos.getX()].repaint();
                    board.refreshBoard(model);
                }
            } else {
                board.boardLabels[selectedPos.getY()][selectedPos.getX()].setIcon(draggedPieceIcon);
                board.boardLabels[selectedPos.getY()][selectedPos.getX()].repaint();
                board.refreshBoard(model);
            }

            // Reset the drag-and-drop piece
            if (draggedPieceLabel != null) {
                board.getLayeredPane().remove(draggedPieceLabel);
                draggedPieceLabel = null;
                draggedPieceIcon = null;
                board.getLayeredPane().repaint();
            }

            board.selectedPiece = null;
            selectedPos = null;
        } else {
            if (draggedPieceLabel != null) {
                board.getLayeredPane().remove(draggedPieceLabel);
                draggedPieceLabel = null;
                draggedPieceIcon = null;
                board.getLayeredPane().repaint();
            }
            board.selectedPiece = null;
            selectedPos = null;
        }
    }

    // Converts pixel coordinates to board array indices
    private int calculateBoardCoordinate(int pixel, int dimension, int boardSize) {
        return pixel * boardSize / dimension;
    }
}
