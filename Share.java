import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Share {

    public static void showShareMenu() {
        JFrame frame = new JFrame("Share");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/Background game/Background.jpg"); 
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        JLabel title = new JLabel("Share to Social Media");
        title.setFont(new Font("Snap ITC", Font.BOLD, 25));
        title.setForeground(Color.BLUE);
        title.setBounds(30, 20, 400, 40);
        panel.add(title);

        int iconSize = 55;

        // Facebook Button
        ImageIcon facebookIcon = new ImageIcon(MainMenu.class.getResource("src/Icons/facebook_icon.png"));
        facebookIcon = new ImageIcon(facebookIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        JButton facebookButton = new JButton(facebookIcon); 
        facebookButton.setBounds(50, 80, 60, 60);
        facebookButton.addActionListener(e -> openURL("https://www.facebook.com/sharer/sharer.php?u=https://example.com"));
        panel.add(facebookButton);

        // X (Twitter) Button
        ImageIcon twitterIcon = new ImageIcon(MainMenu.class.getResource("src/Icons/xtwitter_icon.png"));
        twitterIcon = new ImageIcon(twitterIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        JButton twitterButton = new JButton(twitterIcon);
        twitterButton.setBounds(150, 80, 60, 60);
        twitterButton.addActionListener(e -> openURL("https://twitter.com/intent/tweet?url=https://example.com&text=Check this out!"));
        panel.add(twitterButton);

        // Snapchat Button
        ImageIcon snapchatIcon = new ImageIcon(MainMenu.class.getResource("src/Icons/snapchat_icon.png"));
        snapchatIcon = new ImageIcon(snapchatIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        JButton snapchatButton = new JButton(snapchatIcon); 
        snapchatButton.setBounds(250, 80, 60, 60);
        snapchatButton.addActionListener(e -> openURL("https://www.snapchat.com/share?u=https://example.com"));
        panel.add(snapchatButton);

        // WhatsApp Button
        ImageIcon whatsappIcon = new ImageIcon(MainMenu.class.getResource("src/Icons/whatsapp_icon.png"));
        whatsappIcon = new ImageIcon(whatsappIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        JButton whatsappButton = new JButton(whatsappIcon); 
        whatsappButton.setBounds(50, 160, 60, 60);
        whatsappButton.addActionListener(e -> openURL("https://api.whatsapp.com/send?text=Check%20this%20out!%20https://example.com"));
        panel.add(whatsappButton);

        // Other platforms can be added similarly

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
        Share.showShareMenu();
    }
}
