import java.awt.*;
import java.io.File;
import java.util.HashSet;
import javax.swing.*;

public class Collection extends JFrame {
    private HashSet<String> unlockedImages;

    public Collection(HashSet<String> unlockedImages) {
        this.unlockedImages = unlockedImages;

        setTitle("Card Collection");
        setSize(600, 400);
        setLayout(new GridLayout(0, 4));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        loadImages();
    }

    private void loadImages() {
        File folder = new File("images/");
        if (!folder.exists()) {
            System.err.println("Image folder not found!");
            return;
        }

        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
                ImageIcon icon = new ImageIcon(file.getPath());
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                if (unlockedImages.contains(file.getName())) {
                    label.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                } else {
                    label.setText("Locked");
                }
                add(label);
            }
        }
    }
}