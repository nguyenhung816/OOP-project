import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;

public class Share {

    public static void showShareMenu() {
        JFrame frame = new JFrame("Share");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Kích thước responsive theo màn hình
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.4);  // 40% chiều rộng màn hình
        int height = (int) (screenSize.height * 0.4); // 40% chiều cao màn hình
        frame.setSize(width, height);

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/Background game/Background.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        JLabel title = new JLabel("Share to Social Media", SwingConstants.CENTER);
        title.setFont(new Font("Snap ITC", Font.BOLD, 30));
        title.setForeground(Color.BLUE);
        panel.add(title, BorderLayout.NORTH);

        // Panel chứa các nút chia sẻ
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        int iconSize = Math.max(60, width / 15); // Scale icon theo màn hình

        // Load icon
        JButton fb = createIconButton("src/Icons/facebook_icon.png", iconSize,
                "https://www.facebook.com/sharer/sharer.php?u=https://example.com");

        JButton twitter = createIconButton("src/Icons/xtwitter_icon.png", iconSize,
                "https://twitter.com/intent/tweet?url=https://example.com&text=Check this out!");

        JButton snapchat = createIconButton("src/Icons/snapchat_icon.png", iconSize,
                "https://www.snapchat.com/share?u=https://example.com");

        JButton whatsapp = createIconButton("src/Icons/whatsapp_icon.png", iconSize,
                "https://api.whatsapp.com/send?text=Check%20this%20out!%20https://example.com");

        // Add buttons
        buttonPanel.add(fb);
        buttonPanel.add(twitter);
        buttonPanel.add(snapchat);
        buttonPanel.add(whatsapp);

        panel.add(buttonPanel, BorderLayout.CENTER);
        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JButton createIconButton(String iconPath, int size, String url) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(scaled));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> openURL(url));
        return button;
    }

    private static void openURL(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Unable to open the link.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Desktop operations are not supported on this system.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Share::showShareMenu);
    }
}
