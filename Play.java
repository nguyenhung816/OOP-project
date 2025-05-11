import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Play {

    private static int level = 1;
    private static int score = 0;
    private static int movesLeft;
    private static boolean[][] revealedCards;
    private static Timer timer;
    private static JFrame frame;


    public static void startGame() {
        initializeLevel();
    }

    private static void initializeLevel() {
        int rows, cols;
        if (level <= 2) {
            rows = 3;
            cols = 4;
        } else if (level <= 6) {
            rows = 4;
            cols = 4;
        } else {
            rows = 5;
            cols = 4;
        }

        int totalPairs = (rows * cols) / 2;
        movesLeft = totalPairs + 5;
        revealedCards = new boolean[rows][cols];

        ArrayList<Integer> cardValues = new ArrayList<>();
        for (int i = 1; i <= totalPairs; i++) {
            cardValues.add(i);
            cardValues.add(i);
        }
        Collections.shuffle(cardValues);

        frame = new JFrame("Memory Game - Level " + level);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridLayout(rows, cols));

        JButton[][] cardButtons = new JButton[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int cardValue = cardValues.remove(0);
                JButton cardButton = new JButton(new ImageIcon("src/card_back.jpg")); // Replace with your card back image path
                cardButton.setFont(new Font("Arial", Font.BOLD, 20));
                cardButton.addActionListener(new CardFlipAction(cardButton, cardValue, i, j, cardButtons, totalPairs, rows, cols));
                cardButtons[i][j] = cardButton;
                frame.add(cardButton);
            }
        }

        // Reveal all cards for 3 seconds
        revealAllCards(cardButtons, cardValues, rows, cols);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void revealAllCards(JButton[][] cardButtons, ArrayList<Integer> cardValues, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cardButtons[i][j].setIcon(new ImageIcon("src/card_front_" + (cardValues.get(i * cols + j)) + ".jpg")); // Replace with card front images
            }
        }

        timer = new Timer(3000, e -> {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    cardButtons[i][j].setIcon(new ImageIcon("src/card_back.jpg"));
                }
            }
            timer.stop();
        });
        timer.start();
    }

    private static void showLevelComplete(int movesLeft) {
        JOptionPane.showMessageDialog(frame,
                "Level " + level + " Complete!\n" +
                        "Score: " + score + "\n" +
                        "Moves Left: " + movesLeft,
                "Level Complete", JOptionPane.INFORMATION_MESSAGE);

        if (level < 10) {
            level++;
            frame.dispose();
            initializeLevel();
        } else {
            JOptionPane.showMessageDialog(frame, "You finished all levels! Final Score: " + score, "Game Complete", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        }
    }

    private static void showGameOver() {
        JOptionPane.showMessageDialog(frame, "Game Over!\nFinal Score: " + score, "Game Over", JOptionPane.ERROR_MESSAGE);
        frame.dispose();
    }

    private static class CardFlipAction implements ActionListener {
        private JButton button;
        private int cardValue;
        private int row, col;
        private JButton[][] cardButtons;
        private int totalPairs;
        private int rows, cols;

        private static JButton firstButton = null;
        private static int firstCardValue = -1;

        public CardFlipAction(JButton button, int cardValue, int row, int col, JButton[][] cardButtons, int totalPairs, int rows, int cols) {
            this.button = button;
            this.cardValue = cardValue;
            this.row = row;
            this.col = col;
            this.cardButtons = cardButtons;
            this.totalPairs = totalPairs;
            this.rows = rows;
            this.cols = cols;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (revealedCards[row][col] || firstButton == button) return;

            button.setIcon(new ImageIcon("src/card_front_" + cardValue + ".jpg")); // Replace with your card front image path

            if (firstButton == null) {
                firstButton = button;
                firstCardValue = cardValue;
            } else {
                if (firstCardValue == cardValue) {
                    revealedCards[row][col] = true;
                    revealedCards[getCardPosition(firstButton, cardButtons, rows, cols).row][getCardPosition(firstButton, cardButtons, rows, cols).col] = true;
                    score += 10;

                    if (isLevelComplete(rows, cols)) {
                        score += 50;
                        if (movesLeft == totalPairs) {
                            score += 50;
                        }
                        showLevelComplete(movesLeft);
                    }
                } else {
                    Timer flipBackTimer = new Timer(500, event -> {
                        button.setIcon(new ImageIcon("src/card_back.jpg"));
                        firstButton.setIcon(new ImageIcon("src/card_back.jpg"));
                        firstButton = null;
                    });
                    flipBackTimer.setRepeats(false);
                    flipBackTimer.start();
                }
                firstButton = null;
                movesLeft--;
                if (movesLeft == 0) {
                    showGameOver();
                }
            }
        }
    }

    private static boolean isLevelComplete(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!revealedCards[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static CardPosition getCardPosition(JButton button, JButton[][] cardButtons, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (button.equals(cardButtons[i][j])) {
                    return new CardPosition(i, j);
                }
            }
        }
        return null;
    }

    private static class CardPosition {
        public int row, col;

        public CardPosition(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public static void main(String[] args) {
        startGame();
    }
}
