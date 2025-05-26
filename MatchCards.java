import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator; // BỔ SUNG: Để sắp xếp leaderboard
import java.util.Scanner; // BỔ SUNG: Để đọc file
import javax.swing.*;
import java.io.PrintWriter; // BỔ SUNG: Để lưu file
import java.io.File; // BỔ SUNG: Để xử lý file

public class MatchCards {
    // BỔ SUNG: Lớp nội bộ để lưu thông tin người chơi
    private static class PlayerScore {
        String name;
        int score;
        long time; // milliseconds

        PlayerScore(String name, int score, long time) {
            this.name = name;
            this.score = score;
            this.time = time;
        }

        // Chuyển thành chuỗi để lưu file
        String toFileString() {
            return name + ", " + score + ", " + formatTime(time);
        }
    }

    // CLASS Card giữ nguyên
    class Card {
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString() {
            return cardName;
        }
    }

    // Tất cả biến giữ nguyên, chỉ thêm import
    String[] cardList = {
        "arbok", "beedrill", "blastoise", "butterfree", "charizard",
        "dugtrio", "nidoqueen", "machamp", "venusaur", "jigglypuff"
    };

    int rows = 2;
    int columns = 3;
    int cardWidth = 90;
    int cardHeight = 128;
    int level = 1;
    final int maxLevel = 3;
    int show2CardIndex1 = -1;
    int show2CardIndex2 = -1;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth;
    int boardHeight = rows * cardHeight;

    JFrame frame = new JFrame("Match Cards");
    JLabel textLabel = new JLabel();
    JPanel infoPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel bottomGamePanel = new JPanel();
    JPanel itemsPanel = new JPanel();
    JButton showAllCardsBtn = new JButton();
    JButton show2MatchCardsBtn = new JButton();
    JButton extendLivesBtn = new JButton();
    JButton restartButton = new JButton();
    JButton exitButton = new JButton();

    JLabel livesLabel = new JLabel();
    JLabel retriesLabel = new JLabel();
    JLabel levelLabel = new JLabel();
    JLabel scoreLabel = new JLabel();
    JLabel timeLabel = new JLabel();

    int showAllCardsUses = 3;
    int show2MatchCardsUses = 2;
    int extendLivesUses = 1;
    int lives = 5;
    int retries = 0;
    int score = 0;
    long startTime;
    ArrayList<JButton> board = new ArrayList<>();
    Timer hideCardTimer;
    Timer show2CardsTimer;
    Timer clockTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;

    MatchCards() {
        // Constructor giữ nguyên
        startTime = System.currentTimeMillis();
        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth + 300, boardHeight + 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        infoPanel.setPreferredSize(new Dimension(250, 80));
        infoPanel.setLayout(new GridLayout(5, 1));

        livesLabel = new JLabel("Lives: " + Integer.toString(lives));
        livesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        livesLabel.setHorizontalAlignment(JLabel.CENTER);
        retriesLabel = new JLabel("Retries: " + Integer.toString(retries));
        retriesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        retriesLabel.setHorizontalAlignment(JLabel.CENTER);
        levelLabel = new JLabel("Level: " + Integer.toString(level));
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        levelLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel = new JLabel("Score: " + Integer.toString(score));
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel = new JLabel("Time: 00:00");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);

        infoPanel.add(levelLabel);
        infoPanel.add(livesLabel);
        infoPanel.add(retriesLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(timeLabel);

        frame.add(infoPanel, BorderLayout.EAST);
        infoPanel.add(textLabel);

        clockTimer = new Timer(1000, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            timeLabel.setText("Time: " + formatTime(elapsed));
        });
        clockTimer.start();

        setupItemsPanel();
        frame.add(itemsPanel, BorderLayout.WEST);

        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        frame.add(boardPanel);

        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(e -> {
            if (!gameReady) {
                return;
            }
            gameReady = false;
            restartButton.setEnabled(false);
            card1Selected = null;
            card2Selected = null;
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardSet.get(i).cardImageIcon);
            }
            lives = 5;
            livesLabel.setText("Lives: " + Integer.toString(lives));
            retries += 1;
            retriesLabel.setText("Retries: " + Integer.toString(retries));
            scoreLabel.setText("Score: " + Integer.toString(score));
            setupNewLevel();
        });

        exitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        exitButton.setText("Exit");
        exitButton.setPreferredSize(new Dimension(120, 30));
        exitButton.setMargin(new Insets(5, 10, 5, 10));
        exitButton.setFocusable(false);
        exitButton.setEnabled(false);
        exitButton.addActionListener(e -> {
            if (!gameReady) {
                return;
            }
            Object[] options = {"Yes", "No"};
            int choice = JOptionPane.showOptionDialog(frame,
                    "Are you sure you want to exit to Main Menu?\nYour current score: " + score,
                    "Exit Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (choice == 0) {
                System.out.println("Attempting to open MainMenu from Exit");
                try {
                    clockTimer.stop();
                    frame.dispose();
                    SwingUtilities.invokeLater(MainMenu::new);
                } catch (Exception ex) {
                    System.err.println("Error opening MainMenu: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        bottomGamePanel.add(restartButton);
        bottomGamePanel.add(exitButton);
        frame.add(bottomGamePanel, BorderLayout.SOUTH);
        bottomGamePanel.revalidate();
        bottomGamePanel.repaint();
        frame.revalidate();
        frame.repaint();

        hideCardTimer = new Timer(3000, e -> hideCards());
        hideCardTimer.setRepeats(false);

        show2CardsTimer = new Timer(3000, e -> hideShow2Cards());

        configureLevel();
        setupNewLevel();

        frame.pack();
        frame.setVisible(true);
    }

    // Các hàm configureLevel, setupCards, shuffleCards, setupNewLevel, hideCards giữ nguyên
    void configureLevel() {
        switch (level) {
            case 1: rows = 2; columns = 3; break;
            case 2: rows = 3; columns = 4; break;
            case 3: rows = 4; columns = 5; break;
        }
        boardWidth = columns * cardWidth;
        boardHeight = rows * cardHeight;
        frame.setSize(boardWidth + 300, boardHeight + 200);
        boardPanel.setPreferredSize(new Dimension(boardWidth, boardHeight));
    }

    void setupCards() {
        cardSet = new ArrayList<Card>();
        for (String cardName : cardList) {
            Image cardImg = new ImageIcon(getClass().getResource("src/img/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet);
        while ((cardSet.size() * 2) < (rows * columns)) {
            cardSet.addAll(new ArrayList<>(cardSet));
        }
        cardSet = new ArrayList<>(cardSet.subList(0, (rows * columns) / 2));
        cardSet.addAll(new ArrayList<>(cardSet));
        Image cardBackImg = new ImageIcon(getClass().getResource("src/img/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        System.out.println(cardSet);
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size());
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
        System.out.println(cardSet);
    }

    void setupNewLevel() {
        configureLevel();
        setupCards();
        shuffleCards();
        levelLabel.setText("Level: " + Integer.toString(level));
        livesLabel.setText("Lives: " + Integer.toString(lives));
        retriesLabel.setText("Retries: " + Integer.toString(retries));
        scoreLabel.setText("Score: " + Integer.toString(score));
        timeLabel.setText("Time: " + formatTime(System.currentTimeMillis() - startTime));
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(rows, columns));
        board.clear();
        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(cardClickListener);
            board.add(tile);
            boardPanel.add(tile);
        }
        boardPanel.revalidate();
        boardPanel.repaint();
        frame.pack();
        gameReady = false;
        restartButton.setEnabled(false);
        hideCardTimer.start();
    }

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        } else {
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
            exitButton.setEnabled(true);
            bottomGamePanel.revalidate();
            bottomGamePanel.repaint();
            frame.revalidate();
            frame.repaint();
        }
    }

    // Hàm formatTime và calculateLevelBonus giữ nguyên
    private static String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    int calculateLevelBonus() {
        if (retries == 0) {
            return 30;
        } else if (retries >= 1 && retries <= 3) {
            return 15;
        } else {
            return 0;
        }
    }

    // Hàm showAllTimer giữ nguyên
    Timer showAllTimer = new Timer(3000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
        }
    });

    // Hàm cardClickListener giữ nguyên
    ActionListener cardClickListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameReady) return;
            JButton tile = (JButton) e.getSource();
            if (tile.getIcon() == cardBackImageIcon) {
                if (card1Selected == null) {
                    card1Selected = tile;
                    int index = board.indexOf(card1Selected);
                    card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                } else if (card2Selected == null && tile != card1Selected) {
                    card2Selected = tile;
                    int index2 = board.indexOf(card2Selected);
                    card2Selected.setIcon(cardSet.get(index2).cardImageIcon);
                    int index1 = board.indexOf(card1Selected);
                    if (!cardSet.get(index1).cardName.equals(cardSet.get(index2).cardName)) {
                        score -= 10;
                        scoreLabel.setText("Score: " + Integer.toString(score));
                        lives--;
                        livesLabel.setText("Lives: " + Integer.toString(lives));
                        if (lives == 0) {
                            retries++;
                            retriesLabel.setText("Retries: " + Integer.toString(retries));
                            lives = 5;
                            card1Selected = null;
                            card2Selected = null;
                            gameReady = false;
                            restartButton.setEnabled(false);
                            exitButton.setEnabled(false);
                            setupNewLevel();
                            hideCardTimer.start();
                        } else {
                            hideCardTimer.start();
                        }
                    } else {
                        score += 10;
                        scoreLabel.setText("Score: " + Integer.toString(score));
                        card1Selected = null;
                        card2Selected = null;
                        checkIfLevelCompleted();
                    }
                }
            }
        }
    };

    // SỬA checkIfLevelCompleted để thêm dialog và leaderboard
    void checkIfLevelCompleted() {
        for (JButton tile : board) {
            if (tile.getIcon() == cardBackImageIcon) {
                return;
            }
        }

        int levelBonus = calculateLevelBonus();
        score += levelBonus;

        if (level < maxLevel) {
            JOptionPane.showMessageDialog(frame,
                    "Level " + level + " completed!\nStarting Level " + (level + 1));
            level++;
            lives = 5;
            retries = 0;
            scoreLabel.setText("Score: " + Integer.toString(score));
            setupNewLevel();
        } else {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            String formattedTime = formatTime(elapsedTime);

            // BỔ SUNG: Dialog nhập tên
            String playerName = JOptionPane.showInputDialog(frame,
                    "Congratulations! You have beaten every level!\nEnter your name:",
                    "Enter Name",
                    JOptionPane.PLAIN_MESSAGE);
            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "Anonymous";
            } else {
                playerName = playerName.trim();
            }

            // BỔ SUNG: Lưu vào leaderboard
            ArrayList<PlayerScore> leaderboard = new ArrayList<>();
            // Đọc leaderboard hiện có
            try (Scanner scanner = new Scanner(new File("leaderboard.txt"))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(", ");
                    if (parts.length == 3) {
                        String name = parts[0];
                        int score = Integer.parseInt(parts[1]);
                        // Chuyển MM:SS về milliseconds
                        String[] timeParts = parts[2].split(":");
                        long time = (Long.parseLong(timeParts[0]) * 60 + Long.parseLong(timeParts[1])) * 1000;
                        leaderboard.add(new PlayerScore(name, score, time));
                    }
                }
            } catch (Exception e) {
                // File không tồn tại hoặc lỗi, bỏ qua
                System.err.println("Error reading leaderboard.txt: " + e.getMessage());
            }

            // Thêm bản ghi mới
            leaderboard.add(new PlayerScore(playerName, score, elapsedTime));

            // Sắp xếp: điểm giảm dần, thời gian tăng dần
            leaderboard.sort((a, b) -> {
                if (a.score != b.score) {
                    return b.score - a.score; // Điểm cao hơn
                }
                return Long.compare(a.time, b.time); // Thời gian ngắn hơn
            });

            // Giữ top 10
            if (leaderboard.size() > 10) {
                leaderboard.subList(10, leaderboard.size()).clear();
            }

            // Ghi lại leaderboard
            try (PrintWriter out = new PrintWriter("leaderboard.txt")) {
                for (PlayerScore ps : leaderboard) {
                    out.println(ps.toFileString());
                }
            } catch (Exception e) {
                System.err.println("Error writing to leaderboard.txt: " + e.getMessage());
                JOptionPane.showMessageDialog(frame, "Failed to save leaderboard.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // BỔ SUNG: Lưu vào highscore.txt (giữ cải tiến trước)
            try (PrintWriter out = new PrintWriter("highscore.txt")) {
                out.println("Score: " + score + ", Time: " + formattedTime);
            } catch (Exception e) {
                System.err.println("Error writing to highscore.txt: " + e.getMessage());
                JOptionPane.showMessageDialog(frame, "Failed to save highscore.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            Object[] options = {"Main Menu", "Restart"};
            int choice = JOptionPane.showOptionDialog(frame,
                    "Congratulations! You have beaten every level!\nLevel Bonus: " + levelBonus +
                    "\nTotal Score: " + score + "\nTime: " + formattedTime,
                    "Game Completed",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) { // Main Menu
                System.out.println("Attempting to open MainMenu from Level 3 completion");
                try {
                    clockTimer.stop();
                    frame.dispose();
                    SwingUtilities.invokeLater(MainMenu::new);
                } catch (Exception ex) {
                    System.err.println("Error opening MainMenu: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else if (choice == 1) { // Restart
                startTime = System.currentTimeMillis();
                timeLabel.setText("Time: 00:00");
                level = 1;
                lives = 5;
                retries = 0;
                score = 0;
                showAllCardsUses = 3;
                show2MatchCardsUses = 2;
                extendLivesUses = 1;
                updateItemButtons();
                scoreLabel.setText("Score: " + Integer.toString(score));
                setupNewLevel();
            }
        }
    }

    // Các hàm setupItemsPanel, useShowAllCards, useShow2MatchCards, useExtendLives giữ nguyên
    void setupItemsPanel() {
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Items"));
        itemsPanel.setPreferredSize(new Dimension(180, boardHeight));

        showAllCardsBtn.setText("<html><center>Show All Cards<br>(" + showAllCardsUses + " uses left)</center></html>");
        showAllCardsBtn.setPreferredSize(new Dimension(160, 50));
        showAllCardsBtn.addActionListener(e -> useShowAllCards());
        itemsPanel.add(showAllCardsBtn);
        itemsPanel.add(Box.createVerticalStrut(10));

        show2MatchCardsBtn.setText("<html><center>Show 2 Match Cards<br>(" + show2MatchCardsUses + " uses left)</center></html>");
        show2MatchCardsBtn.setPreferredSize(new Dimension(160, 50));
        show2MatchCardsBtn.addActionListener(e -> useShow2MatchCards());
        itemsPanel.add(show2MatchCardsBtn);
        itemsPanel.add(Box.createVerticalStrut(10));

        extendLivesBtn.setText("<html><center>Extend Lives<br>(" + extendLivesUses + " use left)</center></html>");
        extendLivesBtn.setPreferredSize(new Dimension(160, 50));
        extendLivesBtn.addActionListener(e -> useExtendLives());
        itemsPanel.add(extendLivesBtn);
    }

    void useShowAllCards() {
        ArrayList<Integer> originallyHidden = new ArrayList<>();
        if (showAllCardsUses <= 0 || !gameReady) return;

        showAllCardsUses--;
        score -= 5;
        scoreLabel.setText("Score: " + Integer.toString(score));
        showAllCardsBtn.setText("<html><center>Show All Cards<br>(" + showAllCardsUses + " uses left)</center></html>");
        if (showAllCardsUses == 0) {
            showAllCardsBtn.setEnabled(false);
        }

        for (int i = 0; i < board.size(); i++) {
            JButton tile = board.get(i);
            if (!tile.getIcon().equals(cardSet.get(i).cardImageIcon)) {
                tile.setIcon(cardSet.get(i).cardImageIcon);
                originallyHidden.add(i);
            }
        }
        Timer showAllTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int index : originallyHidden) {
                    board.get(index).setIcon(cardBackImageIcon);
                }
                ((Timer) e.getSource()).stop();
            }
        });
        showAllTimer.setRepeats(false);
        showAllTimer.start();
    }

    void useShow2MatchCards() {
        if (show2MatchCardsUses <= 0 || !gameReady) return;

        show2MatchCardsUses--;
        score -= 10;
        scoreLabel.setText("Score: " + Integer.toString(score));
        show2MatchCardsBtn.setText("<html><center>Show 2 Match Cards<br>(" + show2MatchCardsUses + " uses left)</center></html>");
        if (show2MatchCardsUses == 0) {
            show2MatchCardsBtn.setEnabled(false);
        }

        ArrayList<Integer> availableCards = new ArrayList<>();
        for (int i = 0; i < cardSet.size(); i++) {
            if (board.get(i).getIcon() == cardBackImageIcon) {
                availableCards.add(i);
            }
        }

        if (availableCards.size() >= 2) {
            int firstCardIndex = -1, secondCardIndex = -1;
            for (int i = 0; i < availableCards.size() - 1; i++) {
                int cardIndex1 = availableCards.get(i);
                for (int j = i + 1; j < availableCards.size(); j++) {
                    int cardIndex2 = availableCards.get(j);
                    if (cardSet.get(cardIndex1).cardName.equals(cardSet.get(cardIndex2).cardName)) {
                        firstCardIndex = cardIndex1;
                        secondCardIndex = cardIndex2;
                        break;
                    }
                }
                if (firstCardIndex != -1) break;
            }

            if (firstCardIndex != -1 && secondCardIndex != -1) {
                board.get(firstCardIndex).setIcon(cardSet.get(firstCardIndex).cardImageIcon);
                board.get(secondCardIndex).setIcon(cardSet.get(secondCardIndex).cardImageIcon);
                show2CardIndex1 = firstCardIndex;
                show2CardIndex2 = secondCardIndex;
                show2CardsTimer.setRepeats(false);
                show2CardsTimer.start();
            }
        }
    }

    void useExtendLives() {
        if (extendLivesUses <= 0) return;

        extendLivesUses--;
        score -= 20;
        scoreLabel.setText("Score: " + Integer.toString(score));
        extendLivesBtn.setText("<html><center>Extend Lives<br>(" + extendLivesUses + " use left)</center></html>");
        if (extendLivesUses == 0) {
            extendLivesBtn.setEnabled(false);
        }

        lives += 1;
        for (Component comp : infoPanel.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Lives:")) {
                ((JLabel) comp).setText("Lives: " + Integer.toString(lives));
                break;
            }
        }
    }

    // Hàm hideShow2Cards và updateItemButtons giữ nguyên
    void hideShow2Cards() {
        if (show2CardIndex1 != -1) {
            board.get(show2CardIndex1).setIcon(cardBackImageIcon);
            board.get(show2CardIndex2).setIcon(cardBackImageIcon);
            show2CardIndex1 = -1;
            show2CardIndex2 = -1;
        }
    }

    void updateItemButtons() {
        showAllCardsBtn.setText("<html><center>Show All Cards<br>(" + showAllCardsUses + " uses left)</center></html>");
        showAllCardsBtn.setEnabled(showAllCardsUses > 0);
        show2MatchCardsBtn.setText("<html><center>Show 2 Match Cards<br>(" + show2MatchCardsUses + " uses left)</center></html>");
        show2MatchCardsBtn.setEnabled(show2MatchCardsUses > 0);
        extendLivesBtn.setText("<html><center>Extend Lives<br>(" + extendLivesUses + " use left)</center></html>");
        extendLivesBtn.setEnabled(extendLivesUses > 0);
    }
}