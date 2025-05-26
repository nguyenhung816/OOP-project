import javax.swing.*;
import java.awt.*;

public class HowToPlay {
    public static void showHowToPlay() {
        
        JFrame frame = new JFrame("How To Play");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 475);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/Background game/HowToPlay (2).jpg"); 
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null); 

        JLabel title = new JLabel("How To Play");
        title.setFont(new Font("Snap ITC", Font.BOLD, 30)); 
        title.setForeground(Color.BLUE); 
        title.setBounds(100, 50, 400, 50);
        title.setHorizontalAlignment(SwingConstants.CENTER); 
        panel.add(title);

        JLabel instructions = new JLabel("<html>"
                + "<b>👉 Press Play to start the game.<b><br>"
                + "👉 Memorize the positions of the cards.<br>"
                + "👉 Match all pairs to win.<br>"
                + "👉 Use <b>Collection</b> to view unlocked items.<br>"
                + "</html>");

        instructions.setFont(new Font("Times New Roman", Font.PLAIN, 30)); 
        instructions.setForeground(Color.BLACK); 
        instructions.setBounds(50, 100, 500, 200);
        instructions.setHorizontalAlignment(SwingConstants.LEFT); 
        panel.add(instructions);

        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        HowToPlay.showHowToPlay();
    }
}