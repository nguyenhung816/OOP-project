import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Memory Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(10000, 10000);
        frame.setResizable(false);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/Image for background/background.png"); /
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
        JButton settingButton = new JButton(new ImageIcon("setting_icon.png")); 
        JButton shareButton = new JButton(new ImageIcon("share_icon.png")); 
        JButton aboutUsButton = new JButton(new ImageIcon("about_us_icon.png")); 
        JButton shoppingButton = new JButton(new ImageIcon("shopping_icon.png"));
        
        playButton.setBounds(250, 200, 275, 55);
        howToPlayButton.setBounds(250, 300, 275, 55);
        collectionButton.setBounds(250, 400, 275, 55);
        quitButton.setBounds(250, 500, 275, 55);

        settingButton.setBounds(20, 620, 70, 70); 
        shareButton.setBounds(120, 620, 70, 70);
        shoppingButton.setBounds(220, 620, 70, 70); 
        aboutUsButton.setBounds(700, 620, 70, 70); 

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
        panel.add(settingButton);
        panel.add(shareButton);
        panel.add(aboutUsButton);
        panel.add(shoppingButton);

        howToPlayButton.addActionListener(e -> HowToPlay.showHowToPlay());

        playButton.addActionListener(e -> Play.startGame());
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Play();
            }
        });

        howToPlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HowToPlay();
            }
        });
         settingButton.addActionListener(e -> Setting.showSettings());
        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Setting();
            }
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
