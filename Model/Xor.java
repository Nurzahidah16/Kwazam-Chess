/// WANG KUANG WEI
package Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Xor extends Chesspiece {

    protected String name = "Xor";

    public Xor(Color color, String imagePath, Position pos) {
        super(color, imagePath, pos);
    }

    
    @Override
    public Set<Position> ifValidMove(ChessModel model) {
        Set<Position> validMoves = new HashSet<>();
        int currentX = position.getX();
        int currentY = position.getY();
    
        int[][] movement = {
            {1, 1},  // Move southeast
            {-1, 1}, // Move southwest
            {-1, -1},  // Move northwest
            {1, -1}  // Move northeast
        };
    
        for (int[] moves : movement) {
            int tempX = moves[0];
            int tempY = moves[1];
            int nextX = currentX + tempX;
            int nextY = currentY + tempY;
    
            // Continue moving in the direction until the edge or a piece is encountered
            while (nextX >= 0 && nextX < model.getBoardWidth() &&
                   nextY >= 0 && nextY < model.getBoardHeight()) {
    
                Chesspiece targetPiece = model.getPiece(nextX, nextY);
    
                if (targetPiece != null) { 
                    // If it's an opponent's piece, it can be captured
                    if (!targetPiece.getColor().equals(this.getColor())) {
                        validMoves.add(new Position(nextX, nextY));
                    }
                    break; // Stop after hitting a piece
                }
    
                // If no piece, add the move and continue
                validMoves.add(new Position(nextX, nextY));
                nextX += tempX;
                nextY += tempY;
            }
        }
    
        return validMoves;
    }
}
