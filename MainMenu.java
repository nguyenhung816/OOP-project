import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    } 
    public MainMenu() {
        JFrame frame = new JFrame("Memory Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1440, 800);
        frame.setResizable(false);
        
        playMusic("src/theme_song/game_theme.wav");

        

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/Image for background/background.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);

                g.setFont(new Font("Snap ITC", Font.BOLD, 80));
                g.setColor(Color.BLACK);
                g.drawString("Memory Game", getWidth() / 2-580, 125);
            }
        };
        panel.setLayout(null);

        JButton playButton = new JButton("Play", new ImageIcon("src/Screenshot 2025-05-11 163826.png")); 
        JButton howToPlayButton = new JButton("How To Play", new ImageIcon("src/Images for buttons/How to Play.jpg")); 
        JButton collectionButton = new JButton("Collection", new ImageIcon("src/Images for buttons/Collection.jpg")); 
        JButton quitButton = new JButton("Quit", new ImageIcon("src/Images for buttons/Quit.jpg"));
        
        //load and scale icon
        ImageIcon shareIcon = new ImageIcon(MainMenu.class.getResource("src/Icons/share_icon.png"));
        ImageIcon aboutUsIcon = new ImageIcon(MainMenu.class.getResource("src/Icons/aboutus_icon.png"));

        //resize icon to 65x65
        int iconSize = 65;
        shareIcon = new ImageIcon(shareIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        aboutUsIcon = new ImageIcon(aboutUsIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        
        //create buttons
        JButton shareButton = new JButton(shareIcon);
        JButton aboutUsButton = new JButton(aboutUsIcon);
        
        //mute button
        JButton muteButton = new JButton("Mute");
        muteButton.setBounds(20, 620, 70, 70);
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
        
        playButton.setBounds(250, 200, 275, 55);
        howToPlayButton.setBounds(250, 300, 275, 55);
        collectionButton.setBounds(250, 400, 275, 55);
        quitButton.setBounds(250, 500, 275, 55);
        shareButton.setBounds(120, 620, 70, 70);
        aboutUsButton.setBounds(220, 620, 70, 70); 

        playButton.setHorizontalTextPosition(SwingConstants.CENTER);
        playButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        playButton.setFont(new Font("Snap ITC", Font.BOLD, 30));
        playButton.setForeground(Color.BLACK);

        howToPlayButton.setHorizontalTextPosition(SwingConstants.CENTER);
        howToPlayButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        howToPlayButton.setFont(new Font("Snap ITC", Font.BOLD, 30));
        howToPlayButton.setForeground(Color.BLACK);

        collectionButton.setHorizontalTextPosition(SwingConstants.CENTER);
        collectionButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        collectionButton.setFont(new Font("Snap ITC", Font.BOLD, 30));
        collectionButton.setForeground(Color.BLACK);

        quitButton.setHorizontalTextPosition(SwingConstants.CENTER);
        quitButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        quitButton.setFont(new Font("Snap ITC", Font.BOLD, 30));
        quitButton.setForeground(Color.BLACK);

        panel.add(playButton);
        panel.add(howToPlayButton);
        panel.add(collectionButton);
        panel.add(quitButton);
        panel.add(shareButton);
        panel.add(aboutUsButton);
        panel.add(muteButton);

        howToPlayButton.addActionListener(e -> HowToPlay.showHowToPlay());

        playButton.addActionListener(e -> {
            if (musicClip != null && musicClip.isRunning()) {
                musicClip.stop();
            }
            frame.dispose();

            //Launch the game
            SwingUtilities.invokeLater(() -> {
                new MatchCards();
            });
        });

        howToPlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HowToPlay();
            }
        });

        collectionButton.addActionListener(e -> {
            Collection.showCollectionWindow();
        });

        shareButton.addActionListener(e -> Share.showShareMenu());
        shareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Share();
            }
        });

        aboutUsButton.addActionListener(e -> AboutUs.showAboutUs());
        aboutUsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutUs();
            }
        });
        quitButton.addActionListener(e -> System.exit(0));

        frame.add(panel);
        frame.setVisible(true);
        
    }
}