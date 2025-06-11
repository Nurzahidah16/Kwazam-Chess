///   WANG KUANG WEI
package Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Tor extends Chesspiece {

    protected String name = "Tor";

    public Tor(Color color, String imagePath, Position pos) {
        super(color, imagePath, pos);
    }

    // Movement logic only; actual movement or capturing is handled elsewhere
    @Override
    public Set<Position> ifValidMove(ChessModel model) {
        Set<Position> validMoves = new HashSet<>();
        int currentX = position.getX();
        int currentY = position.getY();
    
        int[][] movement = {
            {1, 0},  // Move East
            {-1, 0}, // Move West
            {0, 1},  // Move South
            {0, -1}  // Move North
        };
    
        for (int[] moves : movement) {
            int tempX = moves[0];
            int tempY = moves[1];
            int nextX = currentX + tempX;
            int nextY = currentY + tempY;
    
            // Keep moving in the direction until the edge of the board or a piece is encountered
            while (nextX >= 0 && nextX < model.getBoardWidth() &&
                   nextY >= 0 && nextY < model.getBoardHeight()) {
    
                Chesspiece targetPiece = model.getPiece(nextX, nextY);
    
                if (targetPiece != null) { 
                    // If it's an opponent's piece, it's a valid capture move
                    if (!targetPiece.getColor().equals(this.getColor())) {
                        validMoves.add(new Position(nextX, nextY));
                    }
                    break; // Stop once a piece is encountered
                }
    
                // No piece here, continue adding valid moves
                validMoves.add(new Position(nextX, nextY));
                nextX += tempX;
                nextY += tempY;
            }
        }
    
        return validMoves;
    }   
}
