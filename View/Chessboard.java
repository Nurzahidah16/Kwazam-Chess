//  CHIA KOK ANG & WANG KUANG WEI
package View;

import Model.*;
import java.awt.*;
import javax.swing.*;

public class Chessboard extends JFrame {

    public JLabel[][] boardLabels;
    private int height = 8; // Number of rows
    private int width = 5; //col
    private boolean isFlipped = false; // Track if the board is flipped
    public Chesspiece selectedPiece;
    private JMenuItem saveGameItem;
    private JMenuItem resetGameItem;

    // Constructor - (WANG KUANG WEI)
    public Chessboard() {
        this.boardLabels = new JLabel[height][width];
        setTitle("Kwazam Chess");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int squareSize = 95;
        setSize(width * squareSize, height * squareSize);
        setLayout(new GridLayout(height, width));
        setResizable(false);
        setLocationByPlatform(true);

        addMenuBar();
    }

    // Top menu bar config 
    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Create the "File" menu
        JMenu fileMenu = new JMenu("File");
        saveGameItem = new JMenuItem("Save Game");
        resetGameItem = new JMenuItem("Reset Game");
        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.addActionListener(e -> {
            System.out.println("Exit clicked");
            System.exit(0); // Exit the application
        });

        // Add items to the "File" menu
        fileMenu.add(saveGameItem);
        fileMenu.add(resetGameItem);
        fileMenu.addSeparator(); // Add a separator line
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    // Initialize checkered design - (WANG KUANG WEI)
    public void initializeBoard(ChessModel model) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);

                // Add a checkerboard background
                if ((row + col) % 2 == 0) {
                    label.setBackground(Color.WHITE);
                } else {
                    label.setBackground(Color.CYAN);
                }

                label.setOpaque(true);
                Chesspiece piece = model.getPiece(col, row);
                if (piece != null) {
                    label.setIcon(piece.getImagePath());
                }

                boardLabels[row][col] = label;
                add(label);
            }
        }
    }

    // Refresh GUI evry move - (CHIA KOK ANG)
    public void refreshBoard(ChessModel model) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int displayRow = isFlipped ? height - 1 - row : row;
                int displayCol = isFlipped ? width - 1 - col : col;

                Chesspiece piece = model.getPiece(col, row);
                JLabel label = boardLabels[displayRow][displayCol];
                if (piece != null) {
                    label.setIcon(piece.getImagePath());
                } else {
                    label.setIcon(null);
                }
            }
        }
    }

    // Flip the board when a player moves (CHIA KOK ANG)
    public void flipBoard(ChessModel model) {
        isFlipped = !isFlipped;
        rotatePieceImages(model);
        refreshBoard(model);
    }

    // Check if board needs to be flipped (CHIA KOK ANG)
    public boolean isFlipped() {
        return isFlipped;
    }

    // Rotate piece images when flipping the board (CHIA KOK ANG)
    private void rotatePieceImages(ChessModel model) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Chesspiece piece = model.getPiece(col, row);
                if (piece != null) {
                    ImageIcon currentIcon = piece.getImagePath();
                    ImageIcon rotatedIcon = piece.rotateImageIcon(currentIcon);
                    piece.setImageIcon(rotatedIcon);
                }
            }
        }
    }

    //rotate piece image back when you reset board manually (CHIA KOK ANG)
    public void resetPieceImage(ChessModel model) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Chesspiece piece = model.getPiece(col, row);
                if (piece != null) {
                    ImageIcon currentIcon = piece.getImagePath();
                    ImageIcon rotatedIcon = piece.rotateImageIcon(currentIcon);
                    piece.setImageIcon(rotatedIcon);
                }
            }
        }
    }

    // Menu button for save game in menu bar(WANG KUANG WEI)
    public JMenuItem getSaveGameMenuItem() {
        return saveGameItem;
    }

    public JMenuItem getResetGameItem() {
        return resetGameItem;
    }

    // Keep track of whose turn it is(WANG KUANG WEI)
    public void updateTitle(String turn, int round) {
        round = (round + 2) / 2;
        setTitle("Kwazam Chess - " + turn + "'s Turn (Turn " + round + ")");
    }
}
