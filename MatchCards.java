import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MatchCards {
    // Thẻ và items
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

    // Các biến chính
    int rows = 4;
    int columns = 5;
    int cardWidth = 90;
    int cardHeight = 128;
    int lives = 3;
    int retries = 0;
    int showAllUses = 3;
    int showTwoUses = 2;
    int extendMovesUses = 1;

    ArrayList<Card> cardSet;
    ArrayList<JButton> board;
    ImageIcon cardBackImageIcon;
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;

    JLabel livesLabel = new JLabel("Lives: " + lives);
    JLabel retriesLabel = new JLabel("Retries: " + retries);
    JLabel showAllLabel = new JLabel( + showAllUses + " uses left");
    JLabel showTwoLabel = new JLabel(+ showTwoUses + " uses left");
    JLabel extendMovesLabel = new JLabel( + extendMovesUses + " uses left");

    JFrame frame = new JFrame("Pokemon Match Cards with Items");

    MatchCards() {
        setupCards();
        shuffleCards();

        // Giao diện chính
        frame.setLayout(new BorderLayout());
        frame.setSize(columns * cardWidth + 200, rows * cardHeight + 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(1, 2));
        textPanel.add(livesLabel);
        textPanel.add(retriesLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(rows, columns));
        board = new ArrayList<>();

        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setIcon(cardBackImageIcon);
            tile.addActionListener(e -> handleCardClick(tile));
            board.add(tile);
            boardPanel.add(tile);
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        // Bảng điều khiển items
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(3, 1));

        JButton showAllButton = new JButton("Show All Cards");
        showAllButton.addActionListener(e -> useShowAll());

        JButton showTwoButton = new JButton("Show 2 Matches");
        showTwoButton.addActionListener(e -> useShowTwoMatches());

        JButton extendMovesButton = new JButton("Extend Moves");
        extendMovesButton.addActionListener(e -> useExtendMoves());

        itemPanel.add(showAllButton);
        itemPanel.add(showAllLabel);
        itemPanel.add(showTwoButton);
        itemPanel.add(showTwoLabel);
        itemPanel.add(extendMovesButton);
        itemPanel.add(extendMovesLabel);

        frame.add(itemPanel, BorderLayout.EAST);

        frame.setVisible(true);

        hideCardTimer = new Timer(1500, e -> hideCards());
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();
    }

    // Cài đặt thẻ
    void setupCards() {
        cardSet = new ArrayList<>();
        String[] cardList = {"arbok", "beedrill", "blastoise", "butterfree", "charizard"};

        for (String cardName : cardList) {
            Image cardImg = new ImageIcon(getClass().getResource("src/img/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card(cardName, cardImageIcon));
        }
        cardSet.addAll(cardSet); // Nhân đôi để tạo cặp

        Image cardBackImg = new ImageIcon(getClass().getResource("src/img/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size());
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
    }

    void handleCardClick(JButton tile) {
        if (!gameReady || tile.getIcon() != cardBackImageIcon) return;

        int index = board.indexOf(tile);
        Card selectedCard = cardSet.get(index);

        tile.setIcon(selectedCard.cardImageIcon);
        if (card1Selected == null) {
            card1Selected = tile;
        } else {
            card2Selected = tile;
            if (!cardsMatch(card1Selected, card2Selected)) {
                lives--;
                livesLabel.setText("Lives: " + lives);
                hideCardTimer.start();
            } else {
                card1Selected = null;
                card2Selected = null;
            }
        }
    }

    boolean cardsMatch(JButton c1, JButton c2) {
        int i1 = board.indexOf(c1);
        int i2 = board.indexOf(c2);
        return cardSet.get(i1).cardImageIcon.equals(cardSet.get(i2).cardImageIcon);
    }

    void hideCards() {
        if (card1Selected != null && card2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card2Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected = null;
        }
    }

    // Sử dụng item: Hiển thị tất cả thẻ
    void useShowAll() {
        if (showAllUses > 0) {
            showAllUses--;
            showAllLabel.setText( + showAllUses + " uses left");
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardSet.get(i).cardImageIcon);
            }
            Timer timer = new Timer(1500, e -> {
                for (int i = 0; i < board.size(); i++) {
                    board.get(i).setIcon(cardBackImageIcon);
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    // Sử dụng item: Hiển thị 2 thẻ phù hợp
    void useShowTwoMatches() {
        if (showTwoUses > 0) {
            showTwoUses--;
            showTwoLabel.setText( + showTwoUses + " uses left");
            for (int i = 0; i < board.size(); i++) {
                for (int j = i + 1; j < board.size(); j++) {
                    if (cardsMatch(board.get(i), board.get(j))) {
                        board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                        board.get(j).setIcon(cardSet.get(j).cardImageIcon);
                        return;
                    }
                }
            }
        }
    }

    // Sử dụng item: Thêm lượt
    void useExtendMoves() {
        if (extendMovesUses > 0) {
            extendMovesUses--;
            extendMovesLabel.setText(+ extendMovesUses + " uses left");
            lives += 1;
            livesLabel.setText("Lives: " + lives);
        }
    }

    public static void main(String[] args) {
        new MatchCards();
    }
}
