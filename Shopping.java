import java.awt.*;
import javax.swing.*;

public class Shopping {

    private static int playerScore = 1500;
    private static JFrame frame;

    public static void showShop(JButton[][] cardButtons, int rows, int cols, int[] movesLeftWrapper) {
        // Lấy kích thước màn hình
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.4);  // 40% chiều rộng
        int height = (int) (screenSize.height * 0.5); // 50% chiều cao

        frame = new JFrame("Shopping");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);

        // Tạo panel chính
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        int fontTitle = Math.max(24, width / 25);
        int fontBody = Math.max(16, width / 40);

        JLabel title = new JLabel("Game Shop");
        title.setFont(new Font("Snap ITC", Font.BOLD, fontTitle));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Your Score: " + playerScore);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, fontBody));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nút Show All Cards
        JButton showAllButton = new JButton("Show All Cards (200 points)");
        showAllButton.setFont(new Font("Arial", Font.PLAIN, fontBody));
        showAllButton.addActionListener(e -> {
            if (playerScore >= 200) {
                playerScore -= 200;
                revealAllCards(cardButtons, rows, cols);
                scoreLabel.setText("Your Score: " + playerScore);
            } else {
                showError("Not enough points!");
            }
        });

        // Nút Show 2 matching cards (chưa làm logic)
        JButton showTwoButton = new JButton("Show 2 Matching Cards (400 points)");
        showTwoButton.setFont(new Font("Arial", Font.PLAIN, fontBody));
        showTwoButton.addActionListener(e -> {
            if (playerScore >= 400) {
                playerScore -= 400;
                scoreLabel.setText("Your Score: " + playerScore);
                // TODO: logic hiện 2 lá giống nhau
            } else {
                showError("Not enough points!");
            }
        });

        // Nút tăng lượt chơi
        JButton extendMovesButton = new JButton("Extend Moves (1000 points)");
        extendMovesButton.setFont(new Font("Arial", Font.PLAIN, fontBody));
        extendMovesButton.addActionListener(e -> {
            if (playerScore >= 1000) {
                playerScore -= 1000;
                movesLeftWrapper[0] += 5;
                JOptionPane.showMessageDialog(frame, "Moves extended by 5!");
                scoreLabel.setText("Your Score: " + playerScore);
            } else {
                showError("Not enough points!");
            }
        });

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(scoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(showAllButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(showTwoButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(extendMovesButton);

        frame.add(panel);
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

    private static void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Insufficient Points", JOptionPane.ERROR_MESSAGE);
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
