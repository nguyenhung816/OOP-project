import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;

public class MatchCards {
    
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
    
    String[] cardList = {
        "arbok",
        "beedrill",
        "blastoise",
        "butterfree",
        "charizard",
        "dugtrio",
        "nidoqueen",
        "machamp",
        "venusaur",
        "jigglypuff"
    };

    int rows = 2;
    int columns = 3;
    int cardWidth = 90;
    int cardHeight = 128;
    int level = 1;
    final int maxLevel = 3;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth;
    int boardHeight = rows * cardHeight;

    JFrame frame = new JFrame("Pokemon Match Cards");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();
    JButton exitButton = new JButton();

    JLabel livesLabel = new JLabel();
    JLabel retriesLabel = new JLabel();
    JLabel levelLabel = new JLabel();
    JLabel scoreLabel = new JLabel();
    // BỔ SUNG ĐỒNG HỒ: Label hiển thị thời gian thực
    JLabel timeLabel = new JLabel();

    int lives = 5;
    int retries = 0;
    int score = 0; // Điểm tích lũy qua các level

    // Biến để theo dõi thời gian
    long startTime;

    ArrayList<JButton> board = new ArrayList<>();
    Timer hideCardTimer;
    // BỔ SUNG ĐỒNG HỒ: Timer cập nhật thời gian thực
    Timer clockTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;
    
    MatchCards() {
        // Bắt đầu đếm thời gian
        startTime = System.currentTimeMillis();

        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Cập nhật textPanel để chứa timeLabel
        textPanel.setPreferredSize(new Dimension(200, 100));
        textPanel.setLayout(new GridLayout(5, 1)); // Thêm 1 hàng cho timeLabel

        levelLabel = new JLabel("Level: " + Integer.toString(level));
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        levelLabel.setHorizontalAlignment(JLabel.CENTER);
        livesLabel = new JLabel("Lives: " + Integer.toString(lives));
        livesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        livesLabel.setHorizontalAlignment(JLabel.CENTER);
        retriesLabel = new JLabel("Retries: " + Integer.toString(retries));
        retriesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        retriesLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel = new JLabel("Score: " + Integer.toString(score));
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        // BỔ SUNG ĐỒNG HỒ: Khởi tạo timeLabel
        timeLabel = new JLabel("Time: 00:00");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        
        textPanel.add(levelLabel);
        textPanel.add(livesLabel);
        textPanel.add(retriesLabel);
        textPanel.add(scoreLabel);
        textPanel.add(timeLabel);

        frame.add(textPanel, BorderLayout.EAST);
        // textLabel không cần thêm vào textPanel (tránh trùng)

        // BỔ SUNG ĐỒNG HỒ: Timer cập nhật thời gian mỗi giây
        clockTimer = new Timer(1000, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            timeLabel.setText("Time: " + formatTime(elapsed));
        });
        clockTimer.start();

        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        frame.add(boardPanel);

        // Cải thiện bố cục restartGamePanel
        restartGamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        restartGamePanel.setPreferredSize(new Dimension(boardWidth, 70));

        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(120, 30));
        restartButton.setMargin(new Insets(5, 10, 5, 10));
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
            
            if (choice == 0) { // Yes
                System.out.println("Attempting to open MainMenu from Exit");
                try {
                    frame.dispose();
                    new MainMenu();
                } catch (Exception ex) {
                    System.err.println("Error opening MainMenu: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        restartGamePanel.add(restartButton);
        restartGamePanel.add(exitButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);
        restartGamePanel.revalidate();
        restartGamePanel.repaint();
        frame.revalidate();
        frame.repaint();

        hideCardTimer = new Timer(3000, e -> hideCards());
        hideCardTimer.setRepeats(false);

        configureLevel();
        setupNewLevel();

        frame.pack();
        frame.setVisible(true);
    }

    void configureLevel() {
        switch (level) {
            case 1: 
                rows = 2; columns = 3;
                break;
            case 2: 
                rows = 3; columns = 4;
                break;
            case 3: 
                rows = 4; columns = 5;
                break;
        }
        boardWidth = columns * cardWidth;
        boardHeight = rows * cardHeight;
        frame.setSize(boardWidth + 200, boardHeight + 100);
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

    void setupNewLevel(){
        configureLevel();
        setupCards();
        shuffleCards();

        levelLabel.setText("Level: " + Integer.toString(level));     
        livesLabel.setText("Lives: " + Integer.toString(lives));
        retriesLabel.setText("Retries: " + Integer.toString(retries));
        scoreLabel.setText("Score: " + Integer.toString(score));
        // BỔ SUNG ĐỒNG HỒ: Cập nhật timeLabel
        timeLabel.setText("Time: " + formatTime(System.currentTimeMillis() - startTime));

        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(rows, columns));
        board.clear();

        for(int i = 0; i < cardSet.size(); i++){
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(cardClickListener);
            board.add(tile);
            boardPanel.add(tile);
        }

        restartGamePanel.setPreferredSize(new Dimension(boardWidth, 70));
        restartGamePanel.revalidate();
        restartGamePanel.repaint();
        frame.revalidate();
        frame.repaint();
        gameReady = false;
        restartButton.setEnabled(false);
        hideCardTimer.start();
    }

    void hideCards() {
        System.out.println("hideCards called, level: " + level);
        System.out.println("exitButton visible: " + exitButton.isVisible() + ", enabled: " + exitButton.isEnabled());
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
            restartGamePanel.revalidate();
            restartGamePanel.repaint();
            frame.revalidate();
            frame.repaint();
        }
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

    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

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
                "Level " + level + " completed!\nLevel Bonus: " + levelBonus + "\nTotal Score: " + score + "\nStarting Level " + (level + 1));
            level++;         
            lives = 5;       
            retries = 0;     
            setupNewLevel(); 
            scoreLabel.setText("Score: " + Integer.toString(score));
        } else {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            String formattedTime = formatTime(elapsedTime);

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
                    // BỔ SUNG ĐỒNG HỒ: Dừng clockTimer
                    clockTimer.stop();
                    frame.dispose();
                    new MainMenu();
                } catch (Exception ex) {
                    System.err.println("Error opening MainMenu: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else if (choice == 1) { // Restart
                // Reset thời gian và điểm
                startTime = System.currentTimeMillis();
                timeLabel.setText("Time: 00:00");
                level = 1;
                lives = 5;
                retries = 0;
                score = 0;
                scoreLabel.setText("Score: " + Integer.toString(score));
                setupNewLevel();
            }
        }
    }
}