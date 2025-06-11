//  CHIA KOK ANG
package Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Ram extends Chesspiece {

    protected String name = "Ram";
    private int moveDirection;

    // Constructor 
    public Ram(Color color, String imagePath, Position pos) {
        super(color, imagePath, pos);
        this.moveDirection = (color == Color.BLUE) ? -1 : 1; // Set initial move direction based on color
    }

    // Getter and setter for moveDirection
    public int getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;
    }

    
    @Override
    public Set<Position> ifValidMove(ChessModel model) {
        Set<Position> validMoves = new HashSet<>();
        int currentX = position.getX();
        int currentY = position.getY();
        int nextY = currentY + moveDirection; // Calculate the next Y position
        Chesspiece targetPiece = model.getPiece(currentX, nextY);

        // Check if the move is within bounds
        if (nextY >= 0 && nextY < model.getBoardHeight()) {
            if (targetPiece != null) {
                // If there's a piece, check if it's an opponent's piece
                if (!targetPiece.getColor().equals(getColor()))
                    validMoves.add(new Position(currentX, nextY)); // Valid capture move
            } else {
                validMoves.add(new Position(currentX, nextY)); // No piece, valid move
            }
        } else {
            // If the move is out of bounds, reverse the direction and check again
            moveDirection *= -1;
            nextY = currentY + moveDirection;
            if (nextY >= 0 && nextY < model.getBoardHeight()) {
                if (model.getPiece(currentX, nextY) == null) {
                    validMoves.add(new Position(currentX, nextY)); // Valid move if no piece is present
                }
            }
        }
        return validMoves;
    }

    public String getName() {
        return name;
    }
}
