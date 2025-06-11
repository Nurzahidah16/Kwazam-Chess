//Ifdianilina Chaicaren & Nur Zahidah

package Model;

import View.Chessboard;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ChessModel implements Serializable {

    protected Color currentPlayer;
    private int round = 0;
    private Chesspiece[][] board;
    private int rows = 8;
    private int cols = 5;
    private static final long serialVersionUID = 1L;

    // Constructor
    public ChessModel() {
        System.out.println("Loading Model..");
        board = new Chesspiece[8][5];
        currentPlayer = Color.blue;
        clearLogs();
    }

    // Getters and setters 
    public int getRound() {
        return round;
    }

    public Color getCurrentColor() {
        return currentPlayer;
    }

    public String getCurrentPlayer() {
        if (currentPlayer == Color.BLUE) {
            return "Blue";
        } else {
            return "Red";
        }
    }

    public Chesspiece[][] getBoard() {
        return board;
    }

    public int getBoardWidth() {
        return board[0].length; // Return columns
    }

    public int getBoardHeight() {
        return board.length; // Return rows
    }

    public String getDimensionAsString() {
        return "Dimensions:" + rows + "," + cols + "\n";
    }

    public Chesspiece getPiece(int col, int row) {
        if (col >= 0 && row >= 0 && row < board.length && col < board[row].length) {
            return board[row][col];
        }
        return null;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setCurrentPlayer(Color currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPiece(int col, int row, Chesspiece cp) {
        if (col >= 0 && row >= 0 && row < board.length && col < board[row].length) { // Corrected order
            board[row][col] = cp;
            if (cp != null) {
                cp.setPos(new Position(col, row));
            }
        }
    }

    // Increment round
    public void incRound() {
        round++;
    }

    // Initialize all chess pieces on the board
    public void initializeChesspiece() {
        for (int col = 0; col < 5; col++) {
            board[6][col] = new Ram(Color.BLUE, "/images/blueRAM.png", new Position(col, 6)); // Correct position
            switch (col) {
                case 0:
                    board[7][col] = new Xor(Color.BLUE, "/images/blueXOR.png", new Position(col, 7));
                    break;
                case 1:
                    board[7][col] = new Biz(Color.BLUE, "/images/blueBIZ.png", new Position(col, 7));
                    break;
                case 2:
                    board[7][col] = new Sau(Color.BLUE, "/images/blueSAU.png", new Position(col, 7));
                    break;
                case 3:
                    board[7][col] = new Biz(Color.BLUE, "/images/blueBIZ.png", new Position(col, 7));
                    break;
                case 4:
                    board[7][col] = new Tor(Color.BLUE, "/images/blueTOR.png", new Position(col, 7));
                    break;
            }
        }

        for (int col = 0; col < 5; col++) {
            board[1][col] = new Ram(Color.RED, "/images/redRAM.png", new Position(col, 1));
            switch (col) {
                case 0:
                    board[0][col] = new Tor(Color.RED, "/images/redTOR.png", new Position(col, 0));
                    break;
                case 1:
                    board[0][col] = new Biz(Color.RED, "/images/redBIZ.png", new Position(col, 0));
                    break;
                case 2:
                    board[0][col] = new Sau(Color.RED, "/images/redSAU.png", new Position(col, 0));
                    break;
                case 3:
                    board[0][col] = new Biz(Color.RED, "/images/redBIZ.png", new Position(col, 0));
                    break;
                case 4:
                    board[0][col] = new Xor(Color.RED, "/images/redXOR.png", new Position(col, 0));
                    break;
            }
        }
    }

    // Logic to move a piece on the board
    public boolean movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        if (!checkBorder(fromCol, fromRow) || !checkBorder(toCol, toRow)) {
            return false;
        }
        Chesspiece movingPiece = getPiece(fromCol, fromRow);
        Set<Position> validMoves = movingPiece.ifValidMove(this);
        boolean captured = false;

        if (!validMoves.contains(new Position(toCol, toRow))) {
            System.out.println("Invalid move for this piece.");
            return false;
        }

        Chesspiece targetPiece = getPiece(toCol, toRow);
        if (targetPiece != null) {
            if (targetPiece.getColor().equals(movingPiece.getColor())) {
                System.out.println("WARNING! Cannot move to a position occupied by your own piece.");
                return false;
            } else {
                captured = true;
                board[toRow][toCol] = null;
            }
        }
        board[toRow][toCol] = movingPiece;
        board[fromRow][fromCol] = null;
        movingPiece.setPos(new Position(toCol, toRow));
        if (captured) {
            System.out.println("Captured Piece: " + movingPiece + " (" + fromCol + ", " + fromRow + ") x "
                    + targetPiece + " (" + toCol + ", " + toRow + ")");
            logging(captured, movingPiece, targetPiece, fromCol, fromRow, toCol, toRow);
        } else {
            System.out.println("Moved Piece: " + movingPiece + " (" + fromCol + ", " + fromRow + ") -> (" + toCol
                    + ", " + toRow + ")");
            logging(captured, movingPiece, targetPiece, fromCol, fromRow, toCol, toRow);
        }
        return true;
    }

    // Check if position is out of bounds
    private boolean checkBorder(int col, int row) {
        return col >= 0 && row >= 0 && row < getBoardHeight() && col < getBoardWidth();
    }

    // Display round for debugging
    public void displayRound() {
        System.out.printf("Turn %d", (round / 2));
        System.out.println();
    }

    // Process round after a move is made
    public void processRound(Chesspiece piece) {
        incRound();
        if (round % 2 == 0) {
            setCurrentPlayer(Color.BLUE);
            displayRound();
            System.out.println(getCurrentPlayer() + "'s Turn");
        } else {
            setCurrentPlayer(Color.RED);
            System.out.println(getCurrentPlayer() + "'s Turn");
        }
        if (round % 4 == 0) {
            transPiece();
        }
        if (piece.getClass().getSimpleName().equals("Ram")) {
            int y = piece.getPos().getY();
            if (y == 0 || y == 7) {
                piece.setImageIcon(piece.rotateImageIcon(piece.getImagePath()));
            }
        }
    }

    // Clear all chess pieces
    public void clearChessPiece() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 5; x++) {
                board[y][x] = null;
            }
        }
    }

    // Close game and dispose chessboard UI
    public void closeGame(Chessboard board) {
        System.out.println("Closing game...");
        board.dispose();
    }

    // Transform Xor/Tor pieces into each other
    public void transPiece() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Chesspiece piece = board[row][col];
                if (piece != null) {
                    Color tempColor;
                    if (piece.getClass().getSimpleName().equals("Tor")) {
                        tempColor = piece.getColor();
                        board[row][col] = null;
                        if (tempColor == Color.RED) {
                            board[row][col] = new Xor(Color.RED, "/images/redXOR.png", new Position(col, row));
                        } else {
                            board[row][col] = new Xor(Color.BLUE, "/images/blueXOR.png", new Position(col, row));
                        }
                    } else if (piece.getClass().getSimpleName().equals("Xor")) {
                        tempColor = piece.getColor();
                        board[row][col] = null;
                        if (tempColor == Color.RED) {
                            board[row][col] = new Tor(Color.RED, "/images/redTOR.png", new Position(col, row));
                        } else {
                            board[row][col] = new Tor(Color.BLUE, "/images/blueTOR.png", new Position(col, row));
                        }
                    }
                }
            }
        }
    }

    // Check if Sau is captured
    public boolean isSauCaptured(Color color) {
        for (int y = 0; y < getBoardHeight(); y++) {
            for (int x = 0; x < getBoardWidth(); x++) {
                Chesspiece piece = getPiece(x, y);
                if (piece != null && piece.getColor() == color
                        && piece.getClass().getSimpleName().equals("Sau")) {
                    return false;
                }
            }
        }
        return true;
    }

    // Check if Ram is captured
    public boolean isRamCaptured(Color color) {
        for (int y = 0; y < getBoardHeight(); y++) {
            for (int x = 0; x < getBoardWidth(); x++) {
                Chesspiece piece = getPiece(x, y);
                if (piece != null && piece.getColor() == color
                        && piece.getClass().getSimpleName().equals("Ram")) {
                    return false;
                }
            }
        }
        return true;
    }

    // Print the status of the game for debugging
    public void printStatus() {
        System.out.println("\nPrinting Status of the Game:");
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                Chesspiece piece = board[y][x];
                if (piece != null) {
                    System.out.printf("Piece: " + piece + " Position: (%d, %d)\n", x, y);
                }
            }
        }
    }

    // Log game moves to file
    public void logging(boolean captured, Chesspiece movingPiece, Chesspiece targetPiece, int fromCol, int fromRow,
                        int toCol, int toRow) {
        try {
            FileWriter fw = new FileWriter("chess_logs.txt", true);
            BufferedWriter writer = new BufferedWriter(fw);
            if (captured) {
                writer.write("Captured: " + movingPiece + " (" + fromCol + ", " + fromRow + ") x "
                        + targetPiece + " (" + toCol + ", " + toRow + ")");
            } else {
                writer.write("Moved: " + movingPiece + " (" + fromCol + ", " + fromRow + ") -> (" + toCol + ", "
                        + toRow + ")");
            }
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clear all logs
    public void clearLogs() {
        try {
            FileWriter fw = new FileWriter("chess_logs.txt");
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isGameOver(Position position, Chessboard board2) {
        // Check if Blue's Sau or Ram is captured
        if (isSauCaptured(Color.BLUE) || isRamCaptured(Color.BLUE)) {
            System.out.println("Blue's game is over.");
            return true;  // Game over for Blue
        }
        
        // Check if Red's Sau or Ram is captured
        if (isSauCaptured(Color.RED) || isRamCaptured(Color.RED)) {
            System.out.println("Red's game is over.");
            return true;  // Game over for Red
        }
    
        return false; // Game is still ongoing
    }
    

    public char[] getGameStateAsString() {
        StringBuilder gameState = new StringBuilder();
    
        // Include the round and current player's turn
        gameState.append("Round: ").append(getRound()).append("\n");
        gameState.append("Current Player: ").append(getCurrentPlayer()).append("\n");
    
        // Include the state of each piece on the board
        for (int row = 0; row < getBoardHeight(); row++) {
            for (int col = 0; col < getBoardWidth(); col++) {
                Chesspiece piece = getPiece(col, row);
                if (piece != null) {
                    gameState.append("Piece: ").append(piece.getClass().getSimpleName())
                            .append(" Position: (").append(col).append(", ").append(row).append(")\n");
                }
            }
        }
    
        return gameState.toString().toCharArray(); // Convert the string to a char array
    }
    
}
