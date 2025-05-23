import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Setting {
    private static Clip musicClip;
    private static float musicVolume = 0.5f; // Default music volume (50%)
    private static float soundVolume = 0.5f; 

    public static void showSettings() {
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/Background game/HowtoPlay.jpg"); 
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Snap ITC", Font.BOLD, 30));
        title.setForeground(Color.BLUE);
        title.setBounds(150, 20, 200, 40);
        panel.add(title);

        JLabel musicLabel = new JLabel("Music Volume:");
        musicLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
        musicLabel.setForeground(Color.BLACK);
        musicLabel.setBounds(50, 75, 200, 30);
        panel.add(musicLabel);

        JSlider musicSlider = new JSlider(0, 100, (int) (musicVolume * 100));
        musicSlider.setBounds(225, 75, 200, 30);
        musicSlider.addChangeListener(e -> adjustMusicVolume(musicSlider.getValue() / 100f));
        panel.add(musicSlider);

        JLabel soundLabel = new JLabel("Sound Volume:");
        soundLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
        soundLabel.setForeground(Color.BLACK);
        soundLabel.setBounds(50, 150, 200, 30);
        panel.add(soundLabel);

        JSlider soundSlider = new JSlider(0, 100, (int) (soundVolume * 100));
        soundSlider.setBounds(225, 150, 200, 30);
        soundSlider.addChangeListener(e -> adjustSoundVolume(soundSlider.getValue() / 100f));
        panel.add(soundSlider);

        JButton muteButton = new JButton("Mute");
        muteButton.setBounds(190, 225, 80, 30);
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
        panel.add(muteButton);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void adjustMusicVolume(float volume) {
        musicVolume = volume;
        if (musicClip != null) {
            FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
        }
    }

    private static void adjustSoundVolume(float volume) {
        soundVolume = volume;
    }

    public static void playMusic(String filePath) {
        try {
            File musicFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);
            adjustMusicVolume(musicVolume); 
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Setting.showSettings();
    }
}
