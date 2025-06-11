///  CHIA KOK ANG & WANG KUANG WEI
package Model;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Biz extends Chesspiece {

    protected String name = "Biz";

    // Constructor 
    public Biz(Color color, String imagePath, Position pos) {
        super(color, imagePath, pos);
    }

    @Override
    public Set<Position> ifValidMove(ChessModel model) {
        Set<Position> validMoves = new HashSet<>();
        int currentX = position.getX();
        int currentY = position.getY();
    
        // Define all possible movement combinations for Biz
        int[][] movement = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, // Horizontal moves with vertical offsets
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}  // Vertical moves with horizontal offsets
        };
    
        for (int[] move : movement) {
            int nextX = currentX + move[0];
            int nextY = currentY + move[1];
    
            // Ensure the next position is within the board boundaries
            if (nextX >= 0 && nextX < model.getBoardWidth() &&
                nextY >= 0 && nextY < model.getBoardHeight()) {
                
                Chesspiece targetPiece = model.getPiece(nextX, nextY);
    
                // Add the position if it's unoccupied or contains an opponent's piece
                if (targetPiece == null || !targetPiece.getColor().equals(this.getColor())) {
                    validMoves.add(new Position(nextX, nextY));
                }
            }
        }
    
        return validMoves;
    }
}
