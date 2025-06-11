// Ifdianilina Chaicaren & Nur Zahidah
package Controller;

import Model.*;
import View.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class ChessController {

    private ChessModel model;
    private Chessboard board;

    // Initialize the ChessController with the model and board 
    public ChessController(ChessModel model, Chessboard board) {
        System.out.println("Loading ChessController..");
        this.model = model;
        this.board = board;
        model.initializeChesspiece();
        board.initializeBoard(model);
        board.setVisible(true);
        InputHandler inputHandler = new InputHandler(board, model);
        board.addMouseListener(inputHandler);
        board.addMouseMotionListener(inputHandler);
        addSaveGameListener();
        addResetGameListener();
    }
    
    

    // Save game button listener(IFDIANILINA CHAICAREN)
    private void addSaveGameListener() {
        JMenuItem saveGameItem = board.getSaveGameMenuItem();
        saveGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });
    }

    // Reset game listener(IFDIANILINA CHAICAREN)
    private void addResetGameListener() {
        JMenuItem resetGameButton = board.getResetGameItem();
        resetGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }

        
        });
    }

    // Reset game function (NURZAHIDAH)
    private void resetGame() {
        model.clearChessPiece();
        model.initializeChesspiece();
        model.setRound(0);
        model.setCurrentPlayer(Color.BLUE);
        if (board.isFlipped()) {
            board.flipBoard(model);
            board.resetPieceImage(model);
        }
        model.clearLogs();
        board.refreshBoard(model);
    }

    // Save game function(NURZAHIDAH)
    private void saveGame() {
        // Ask user to enter a name for the saved game
        String gameName = JOptionPane.showInputDialog(board, "Name:");
        if (gameName == null || gameName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(board, "Game name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        

        // Generate the file and save board state (NURZAHIDAH)
        String fileName = "saved_games/" + gameName.toLowerCase().replace(" ", "_") + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
            String dimensionState = model.getDimensionAsString();
            char[] gameState = model.getGameStateAsString();
            writer.write(dimensionState);
            writer.write(gameState);
            System.out.println("Game saved successfully to " + fileName);
            JOptionPane.showMessageDialog(board, "Game saved as: " + gameName, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(board, "Error saving the game.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}