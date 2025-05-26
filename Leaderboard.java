import java.io.*;
import javax.swing.*;

public class Leaderboard extends JFrame {
    private DefaultListModel<String>model;

    public Leaderboard() {
        setTitle("Leaderboard");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        add(new JScrollPane(list));

        loadScores();
    }

    private void loadScores() {
        File file = new File("src/leaderboard data/leaderboard.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                model.addElement(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveScore(String player, int score) {
        try (FileWriter fw = new FileWriter("src/leaderboard data/leaderboard.txt", true)) {
            fw.write(player + " - " + score + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
