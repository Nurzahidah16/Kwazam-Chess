//Ifdianilina Chaicaren & Nur Zahidah
package View;

import java.awt.*;
import javax.swing.*;

public class Launcher extends JFrame {

    private JButton launchButton;
    private JButton loadGameButton;

    // Inner class for custom background panel
    private class Background extends JPanel {
        private Image backgroundImage;

        public Background(String imagePath) {
            this.backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Constructor for Launcher
    public Launcher() {
        setTitle("Kwazam Chess Launcher");

        // Create the custom background panel
        Background bg = new Background("images/background.png"); // Replace with your image path
        bg.setLayout(new BoxLayout(bg, BoxLayout.PAGE_AXIS));
        setContentPane(bg); // Set the custom panel as the content pane

        setSize(600, 600); // Adjusted size for a larger main page
        setLocation(700, 200);
        setResizable(false);
        bg.setBorder(BorderFactory.createEmptyBorder(25, 100, 25, 100));

        ImageIcon logoIcon = new ImageIcon("images/Mainpage.png");
        Image image = logoIcon.getImage();
        Image resizedImage = image.getScaledInstance(250, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedLogo = new ImageIcon(resizedImage);

        bg.add(Box.createRigidArea(new Dimension(20, 60)));
        JLabel logoLabel = new JLabel(resizedLogo);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the image
        bg.add(logoLabel);
        bg.add(Box.createRigidArea(new Dimension(10, 50)));

        launchButton = new JButton("Play Kwazam Chess");
        launchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bg.add(launchButton);
        bg.add(Box.createRigidArea(new Dimension(10, 50)));

        loadGameButton = new JButton("Load Game");
        loadGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bg.add(loadGameButton);
        bg.add(Box.createRigidArea(new Dimension(10, 50)));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Getters
    public JButton getLaunchButton() {
        return launchButton;
    }

    public JButton getLoadButton() {
        return loadGameButton;
    }

   
}
