import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;

public class Collection {
    private static final Set<String> unlockedPokemon = new LinkedHashSet<>();

    private static final String[] allPokemon = {
            "arbok", "beedrill", "blastoise", "butterfree",
            "charizard", "dugtrio", "nidoqueen", "machamp",
            "venusaur", "jigglypuff"
    };

    public static void unlockRandomPokemon() {
        ArrayList<String> remaining = new ArrayList<>();
        for (String p : allPokemon) {
            if (!unlockedPokemon.contains(p)) {
                remaining.add(p);
            }
        }

        if (!remaining.isEmpty()) {
            Random random = new Random();
            String randomPokemon = remaining.get(random.nextInt(remaining.size()));
            unlockedPokemon.add(randomPokemon);

            //Firework and sound
            SwingUtilities.invokeLater(() -> {
                showFireworkEffect();
                playCelebrationSound();
            });

            //Announcement dialog
            JOptionPane.showMessageDialog(null,
                    "You unlocked a new Pokémon: " + capitalize(randomPokemon) + "!",
                    "New Pokémon Unlocked!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void showCollectionWindow() {
        JFrame frame = new JFrame("My Pokémon Collection");
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(0, 4, 10, 10));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        for (String pokemon : unlockedPokemon) {
            JPanel cardPanel = new JPanel(new BorderLayout());
            cardPanel.setBackground(Color.WHITE);

            // IImage path
            ImageIcon icon = new ImageIcon(Collection.class.getResource("src/img/" + pokemon + ".jpg"));
            Image scaledImage = icon.getImage().getScaledInstance(120, 150, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel nameLabel = new JLabel(capitalize(pokemon));
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

            cardPanel.add(imageLabel, BorderLayout.CENTER);
            cardPanel.add(nameLabel, BorderLayout.SOUTH);
            grid.add(cardPanel);
        }

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private static void showFireworkEffect() {
        JWindow window = new JWindow();
        window.setSize(400, 400);
        window.setLocationRelativeTo(null);
        window.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Random rand = new Random();
                for (int i = 0; i < 100; i++) {
                    int x = rand.nextInt(getWidth());
                    int y = rand.nextInt(getHeight());
                    int size = rand.nextInt(10) + 5;
                    Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
                    g2.setColor(color);
                    g2.fillOval(x, y, size, size);
                }
            }
        };

        panel.setOpaque(false);
        window.add(panel);
        window.setVisible(true);

        Timer repaintTimer = new Timer(200, e -> panel.repaint());
        repaintTimer.start();

        new Timer(3000, e -> {
            window.setVisible(false);
            window.dispose();
            repaintTimer.stop();
        }).start();
    }

    private static void playCelebrationSound() {
        try {
            File soundFile = new File("src/theme_song/goodresult.wav");

            if (!soundFile.exists()) {
                System.err.println("Sound file not found: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}