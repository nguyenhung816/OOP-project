import java.io.*;
import java.util.*;
import javax.swing.*;

public class LeaderBoard extends JFrame {
    private DefaultListModel<String> model;

    public LeaderBoard() {
        setTitle("Leaderboard");
        setSize(350, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        add(new JScrollPane(list));

        loadScores();
    }

    private void loadScores() {
        File file = new File("leaderboard.txt");
        if (!file.exists()) return;

        List<ScoreEntry> entries = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 3) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    String date = parts[2];
                    entries.add(new ScoreEntry(name, score, date));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        // Sắp xếp giảm dần theo điểm
        entries.sort((a, b) -> Integer.compare(b.score, a.score));

        model.clear();
        int rank = 1;
        for (ScoreEntry entry : entries.subList(0, Math.min(10, entries.size()))) {
            model.addElement(rank++ + ". " + entry.name + " - " + entry.score + " (" + entry.date + ")");
        }
    }

    public static void saveScore(String player, int score) {
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        try (FileWriter fw = new FileWriter("leaderboard.txt", true)) {
            fw.write(player + " - " + score + " - " + timestamp + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Store score entries
    static class ScoreEntry {
        String name;
        int score;
        String date;

        ScoreEntry(String name, int score, String date) {
            this.name = name;
            this.score = score;
            this.date = date;
        }
    }
}
