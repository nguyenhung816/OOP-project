import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class MainMenu {
    private static Clip musicClip;

    public static void playMusic(String filePath) {
        try {
            File musicFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Memory Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Kích thước theo 70% màn hình
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) (screenSize.width * 0.7);
            int height = (int) (screenSize.height * 0.7);
            frame.setSize(width, height);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());

            playMusic("src/theme_song/game_theme.wav");

            // Panel chính với ảnh nền
            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon background = new ImageIcon("src/Image for background/Bg.gif");
                    g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            };
            mainPanel.setLayout(new BorderLayout());

            // Tiêu đề
            JLabel titleLabel = new JLabel("Memory Game", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Snap ITC", Font.BOLD, 60));
            titleLabel.setForeground(Color.BLACK);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
            mainPanel.add(titleLabel, BorderLayout.NORTH);

            // Panel nút chính
            JPanel buttonPanel = new JPanel(new GridLayout(4,11,20,20));
            buttonPanel.setOpaque(false);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 100, 20, 100));

            Font buttonFont = new Font("Snap ITC", Font.BOLD, 28);

            JButton playButton = new JButton("Play");
            JButton howToPlayButton = new JButton("How To Play");
            JButton collectionButton = new JButton("Collection");
            JButton quitButton = new JButton("Quit");

            for (JButton btn : new JButton[]{playButton, howToPlayButton, collectionButton, quitButton}) {
                btn.setFont(buttonFont);
                btn.setFocusPainted(false);
                btn.setBackground(Color.WHITE);
                buttonPanel.add(btn);
            }

            mainPanel.add(buttonPanel, BorderLayout.CENTER);

            // Panel icon nhỏ phía dưới
            JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
            iconPanel.setOpaque(false);

            int iconSize = 50;
            JButton shareButton = new JButton(new ImageIcon(new ImageIcon("src/Icons/share_icon.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH)));
            JButton aboutUsButton = new JButton(new ImageIcon(new ImageIcon("src/Icons/aboutus_icon.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH)));
            JButton shoppingButton = new JButton(new ImageIcon(new ImageIcon("src/Icons/shopping_icon.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH)));

            JButton muteButton = new JButton("Mute");

            iconPanel.add(muteButton);
            iconPanel.add(shareButton);
            iconPanel.add(shoppingButton);
            iconPanel.add(aboutUsButton);
            mainPanel.add(iconPanel, BorderLayout.SOUTH);

            // Thêm hành động nút
            playButton.addActionListener(e -> {
                if (musicClip != null) musicClip.stop();
                frame.dispose();
                new MatchCards();
            });

            howToPlayButton.addActionListener(e -> new HowToPlay());
            collectionButton.addActionListener(e -> new Collection());
            quitButton.addActionListener(e -> System.exit(0));

            muteButton.addActionListener(e -> {
                if (musicClip != null && musicClip.isRunning()) {
                    musicClip.stop();
                    muteButton.setText("Unmute");
                } else {
                    if (musicClip != null) {
                        musicClip.start();
                        muteButton.setText("Mute");
                    }
                }
            });

            shareButton.addActionListener(e -> new Share());
            aboutUsButton.addActionListener(e -> new AboutUs());
            shoppingButton.addActionListener(e -> new Shopping());

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
}
