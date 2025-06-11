//Ifdianilina Chaicaren & Nur Zahidah
package Model;

import java.awt.*;
import java.net.URL;
import java.util.*;
import javax.swing.ImageIcon;

public abstract class Chesspiece {

    protected ImageIcon images;
    protected Position position;
    protected Color color;

    // Constructor
    public Chesspiece(Color color, String imagePath, Position pos) {
        this.color = color;
        
        // Try loading the image from resources
        URL imageURL = getClass().getResource(imagePath);
        
        if (imageURL != null) {
            // If image is found, resize and set the image icon
            this.images = resizeImageIcon(new ImageIcon(imageURL), 90, 90); // Resize to 90X90 pixels
        } else {
            // Handle the case where the image is not found
            System.err.println("Image not found: " + imagePath);
            // Optionally, set a default image if the image is not found
            this.images = resizeImageIcon(new ImageIcon(getClass().getResource("/default-image.png")), 90, 90); // Replace with your default image path
        }
        
        this.position = pos;
    }

    // Getters and setters
    public Color getColor() {
        return color;
    }

    private String getColorString() {
        return (color == Color.BLUE) ? "Blue" : "Red";
    }

    public ImageIcon getImagePath() {
        return images;
    }

    public Position getPos() {
        return position;
    }

    public void setPos(Position pos) {
        this.position = pos;
    }

    public void setImageIcon(ImageIcon icon) {
        this.images = icon;
    }

    // Get image icon to put it through rotateImage() 
    public ImageIcon rotateImageIcon(ImageIcon icon) {
        Image image = icon.getImage();
        Image rotatedImage = rotateImage(image, 180); // Rotate by 180 degrees
        return new ImageIcon(rotatedImage);
    }

    // Rotate the image from rotateImageIcon() 
    private Image rotateImage(Image image, double degrees) {
        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int newWidth = (int) Math.floor(width * cos + height * sin);
        int newHeight = (int) Math.floor(height * cos + width * sin);

        // Create a new buffered image
        java.awt.image.BufferedImage rotated = new java.awt.image.BufferedImage(newWidth, newHeight, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        g2d.translate((newWidth - width) / 2, (newHeight - height) / 2);
        g2d.rotate(Math.toRadians(degrees), width / 2, height / 2);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotated;
    }

    // Abstract function to calculate valid moves for the chesspiece
    public abstract Set<Position> ifValidMove(ChessModel cboard);

    // Resize image icon to make icon fit inside each space
    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int heightreal) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, heightreal, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    @Override
    public String toString() {
        Position pos = getPos(); // Get the position of the piece
        return getColorString() + " " + getClass().getSimpleName() + " at " + pos; // Include position in the string
    }
}
