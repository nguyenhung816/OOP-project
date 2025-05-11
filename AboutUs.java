import javax.swing.*;
import java.awt.*;

public class AboutUs {

    public static void showAboutUs() {
        JFrame frame = new JFrame("About Us");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/Background game/AboutUs.jpg"); 
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        JLabel title = new JLabel("Meet the Developers");
        title.setFont(new Font("Snap ITC", Font.BOLD, 40)); 
        title.setForeground(Color.BLUE); 
        title.setBounds(5, 20, 600, 50);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title);

        JLabel dev1 = new JLabel("1. Nguyễn Nhật Anh Huy - ITITWE23031");
        JLabel dev2 = new JLabel("2. Nguyễn Lê Ngọc Trân - ITDSIU23024");
        JLabel dev3 = new JLabel("3. Nguyễn Triều Hưng - ITCSIU24031");
        JLabel dev4 = new JLabel("4. Phan Thị Kim Ngân - ITCSIU24060");

        Font devFont = new Font("Times New Roman", Font.PLAIN, 30);
        Color devColor = Color.BLACK;

        dev1.setFont(devFont);
        dev1.setForeground(devColor);
        dev1.setBounds(25, 100, 600, 40);

        dev2.setFont(devFont);
        dev2.setForeground(devColor);
        dev2.setBounds(25, 140, 600, 40);

        dev3.setFont(devFont);
        dev3.setForeground(devColor);
        dev3.setBounds(25, 180, 600, 40);

        dev4.setFont(devFont);
        dev4.setForeground(devColor);
        dev4.setBounds(25, 220, 600, 40);

        panel.add(dev1);
        panel.add(dev2);
        panel.add(dev3);
        panel.add(dev4);

        JButton closeButton = new JButton("Close");
        closeButton.setBounds(250, 300, 100, 30);
        closeButton.setFont(new Font("Arial", Font.BOLD, 15));
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> frame.dispose());
        panel.add(closeButton);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        showAboutUs();
    }
}
