import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

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
        "arbok", "beedrill", "blastoise", "butterfree", "charizard",
        "dugtrio", "nidoqueen", "machamp", "venusaur", "jigglypuff"
    };

    int rows = 4;
    int columns = 5;

    int cardWidth;
    int cardHeight;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardWidth;
    int boardHeight;

    JFrame frame = new JFrame("Pokemon Match Cards");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    int lives = 3;
    int retries = 0;
    ArrayList<JButton> board;
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;

    MatchCards() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        cardWidth = (int) (screenSize.width * 0.08);   // ~8% chiều rộng
        cardHeight = (int) (screenSize.height * 0.15); // ~15% chiều cao

        boardWidth = columns * cardWidth;
        boardHeight = rows * cardHeight;

        setupCards();
        shuffleCards();

        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth + 200, boardHeight + 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel livesLabel = new JLabel("Lives: " + lives);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel retriesLabel = new JLabel("Retries: " + retries);
        retriesLabel.setFont(new Font("Arial", Font.BOLD, 20));

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(livesLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(retriesLabel);

        frame.add(textPanel, BorderLayout.EAST);

        board = new ArrayList<>();
        boardPanel.setLayout(new GridLayout(rows, columns, 10, 10));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        boardPanel.setBackground(new Color(240, 248, 255)); // pastel background

        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(e -> {
                if (!gameReady) return;

                JButton clicked = (JButton) e.getSource();
                if (clicked.getIcon() == cardBackImageIcon) {
                    if (card1Selected == null) {
                        card1Selected = clicked;
                        int index = board.indexOf(card1Selected);
                        clicked.setIcon(cardSet.get(index).cardImageIcon);
                    } else if (card2Selected == null) {
                        card2Selected = clicked;
                        int index = board.indexOf(card2Selected);
                        clicked.setIcon(cardSet.get(index).cardImageIcon);

                        if (card1Selected.getIcon() != card2Selected.getIcon()) {
                            lives--;
                            livesLabel.setText("Lives: " + lives);
                            hideCardTimer.start();

                            if (lives == 0) {
                                card1Selected = null;
                                card2Selected = null;
                                shuffleCards();
                                for (int j = 0; j < board.size(); j++) {
                                    board.get(j).setIcon(cardSet.get(j).cardImageIcon);
                                }
                                lives = 3;
                                retries++;
                                livesLabel.setText("Lives: " + lives);
                                retriesLabel.setText("Retries: " + retries);
                                hideCardTimer.start();
                            }
                        } else {
                            card1Selected = null;
                            card2Selected = null;
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth, 40));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(e -> {
            if (!gameReady) return;
            gameReady = false;
            restartButton.setEnabled(false);
            card1Selected = null;
            card2Selected = null;
            shuffleCards();

            for (int j = 0; j < board.size(); j++) {
                board.get(j).setIcon(cardSet.get(j).cardImageIcon);
            }

            lives = 3;
            retries++;
            livesLabel.setText("Lives: " + lives);
            retriesLabel.setText("Retries: " + retries);
            hideCardTimer.start();
        });

        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        hideCardTimer = new Timer(1500, e -> hideCards());
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();
    }

    void setupCards() {
        cardSet = new ArrayList<>();
        for (String cardName : cardList) {
            Image cardImg = new ImageIcon("src/img/" + cardName + ".jpg").getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH));
            cardSet.add(new Card(cardName, cardImageIcon));
        }
        cardSet.addAll(cardSet);

        Image cardBackImg = new ImageIcon("src/img/back.jpg").getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size());
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
    }

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        } else {
            for (JButton btn : board) {
                btn.setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MatchCards::new);
    }
}
