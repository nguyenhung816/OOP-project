import java.awt.*;
import javax.swing.*;

public class HowToPlay {
    public static void showHowToPlay() {
        // Lấy kích thước màn hình
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.5);
        int height = (int) (screenSize.height * 0.6);

        JFrame frame = new JFrame("How To Play");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/Background game/HowToPlay (2).jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel title = new JLabel("How To Play", SwingConstants.CENTER);
        title.setFont(new Font("Snap ITC", Font.BOLD, Math.max(24, width / 30)));
        title.setForeground(Color.BLUE);
        panel.add(title, BorderLayout.NORTH);

        // Instructions
        JLabel instructions = new JLabel(
                "<html><div style='text-align: center;'>"
                        + "<b>1. Press Play to start the game.</b><br>"
                        + "2. Memorize the positions of the cards.<br>"
                        + "3. Match all pairs to win.<br>"
                        + "4. Use <b>Collection</b> to view unlocked items."
                        + "</div></html>", SwingConstants.CENTER);

        instructions.setFont(new Font("Times New Roman", Font.PLAIN, Math.max(16, width / 50)));
        instructions.setForeground(Color.BLACK);
        panel.add(instructions, BorderLayout.CENTER);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        showHowToPlay();
    }
}
