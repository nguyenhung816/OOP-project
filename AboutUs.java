import java.awt.*;
import javax.swing.*;

public class AboutUs {

    public static void showAboutUs() {
        // Lấy kích thước màn hình để scale cửa sổ
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screenSize.width * 0.5);   // 50% chiều rộng màn hình
        int height = (int)(screenSize.height * 0.5); // 50% chiều cao màn hình

        JFrame frame = new JFrame("About Us");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);

        // Panel chính với background custom
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/Background game/AboutUs.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BorderLayout(10, 10));
        panel.setOpaque(false);

        // Tiêu đề
        JLabel title = new JLabel("Meet the Developers", SwingConstants.CENTER);
        int titleFontSize = Math.max(24, width / 20);
        title.setFont(new Font("Snap ITC", Font.BOLD, titleFontSize));
        title.setForeground(Color.BLUE);
        panel.add(title, BorderLayout.NORTH);

        // Panel danh sách dev với BoxLayout để dễ dàng dàn hàng dọc
        JPanel devPanel = new JPanel();
        devPanel.setOpaque(false);
        devPanel.setLayout(new BoxLayout(devPanel, BoxLayout.Y_AXIS));

        Font devFont = new Font("Times New Roman", Font.PLAIN, Math.max(16, width / 40));
        Color devColor = Color.BLACK;

        String[] developers = {
            "1. Nguyễn Nhật Anh Huy - ITITWE23031",
            "2. Nguyễn Lê Ngọc Trân - ITDSIU23024",
            "3. Nguyễn Triều Hưng - ITCSIU24031",
            "4. Phan Thị Kim Ngân - ITCSIU24060"
        };

        for (String dev : developers) {
            JLabel devLabel = new JLabel(dev);
            devLabel.setFont(devFont);
            devLabel.setForeground(devColor);
            devLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            devLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            devPanel.add(devLabel);
        }

        // Đặt devPanel vào JScrollPane nếu danh sách dài
        JScrollPane scrollPane = new JScrollPane(devPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Nút đóng
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, Math.max(12, width / 60)));
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> frame.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AboutUs::showAboutUs);
    }
}
