import javax.swing.*;
import java.awt.*;

public class Shopping {

    private static int playerScore = 1500; // Example score for testing
    private static JFrame frame;

    public static void showShop(JButton[][] cardButtons, int rows, int cols, int[] movesLeftWrapper) {
        frame = new JFrame("Shopping");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Game Shop");
        title.setFont(new Font("Snap ITC", Font.BOLD, 30));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Your Score: " + playerScore);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Option 1: Show all cards
        JButton showAllButton = new JButton("Show All Cards (200 points)");
        showAllButton.setFont(new Font("Arial", Font.PLAIN, 16));
        showAllButton.addActionListener(e -> {
            if (playerScore >= 200) {
                playerScore -= 200;
                revealAllCards(cardButtons, rows, cols);
                scoreLabel.setText("Your Score: " + playerScore);
            } else {
                JOptionPane.showMessageDialog(frame, "Not enough points!", "Insufficient Points", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Option 2: Show 2 matching cards
        JButton showTwoButton = new JButton("Show 2 Matching Cards (400 points)");
        showTwoButton.setFont(new Font("Arial", Font.PLAIN, 16));
        showTwoButton.addActionListener(e -> {
            if (playerScore >= 400) {
                playerScore -= 400;
                scoreLabel.setText("Your Score: " + playerScore);
            } else {
                JOptionPane.showMessageDialog(frame, "Not enough points!", "Insufficient Points", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Option 3: Extend limited play
        JButton extendMovesButton = new JButton("Extend Moves (1000 points)");
        extendMovesButton.setFont(new Font("Arial", Font.PLAIN, 16));
        extendMovesButton.addActionListener(e -> {
            if (playerScore >= 1000) {
                playerScore -= 1000;
                movesLeftWrapper[0] += 5;
                JOptionPane.showMessageDialog(frame, "Moves extended by 5!");
                scoreLabel.setText("Your Score: " + playerScore);
            } else {
                JOptionPane.showMessageDialog(frame, "Not enough points!", "Insufficient Points", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(showAllButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(showTwoButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(extendMovesButton);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void revealAllCards(JButton[][] cardButtons, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cardButtons[i][j].setIcon(new ImageIcon("path_to_card_front_image")); 
            }
        }

        Timer timer = new Timer(3000, e -> {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    cardButtons[i][j].setIcon(new ImageIcon("path_to_card_back_image")); 
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        int rows = 4;
        int cols = 4;
        JButton[][] cardButtons = new JButton[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cardButtons[i][j] = new JButton();
            }
        }

        int[] movesLeftWrapper = {10}; 
        showShop(cardButtons, rows, cols, movesLeftWrapper);
    }
}
//