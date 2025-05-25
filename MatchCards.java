import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
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

    int rows = 4;
    int columns = 5;
    int cardWidth = 90;
    int cardHeight = 128;

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

    JPanel itemsPanel = new JPanel();
    JButton showAllCardsBtn = new JButton();
    JButton show2MatchCardsBtn = new JButton();
    JButton extendLivesBtn = new JButton();
    
    int showAllCardsUses = 3;
    int show2MatchCardsUses = 2;
    int extendLivesUses = 1;

    int lives = 3;
    int retries = 0;
    ArrayList<JButton> board;
    ArrayList<Boolean> matchedCards; 
    Timer hideCardTimer;
    Timer show2CardsTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;
    
    MatchCards() {
        setupCards();
        shuffleCards();
        matchedCards = new ArrayList<>(Collections.nCopies(cardSet.size(), false));

        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth + 200, boardHeight + 100); // Increased size for items panel
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel livesLabel = new JLabel("Lives: " + Integer.toString(lives));
        livesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        livesLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel retriesLabel = new JLabel("Retries: " + Integer.toString(retries));
        retriesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        retriesLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.setPreferredSize(new Dimension(boardWidth/2, 60));
        textPanel.add(livesLabel);
        textPanel.add(retriesLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        setupItemsPanel();
        frame.add(itemsPanel, BorderLayout.EAST);

        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            final int index = i; // For lambda expression
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady) {
                        return;
                    }
                    JButton tile = (JButton) e.getSource();
                    int tileIndex = board.indexOf(tile);
                    
                    if (matchedCards.get(tileIndex)) {
                        return;
                    }
                    
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {
                            card1Selected = tile;
                            card1Selected.setIcon(cardSet.get(tileIndex).cardImageIcon);
                        }
                        else if (card2Selected == null) {
                            card2Selected = tile;
                            card2Selected.setIcon(cardSet.get(tileIndex).cardImageIcon);

                            int card1Index = board.indexOf(card1Selected);
                            int card2Index = board.indexOf(card2Selected);
                            
                            if (!cardSet.get(card1Index).cardName.equals(cardSet.get(card2Index).cardName)) {
                                lives -= 1;
                                livesLabel.setText("Lives: " + Integer.toString(lives));
                                hideCardTimer.start();
                                if(lives == 0){
                                    resetGame(livesLabel, retriesLabel);
                                }
                            }
                            else {
                                matchedCards.set(card1Index, true);
                                matchedCards.set(card2Index, true);
                                card1Selected = null;
                                card2Selected = null;
                                
                                checkWinCondition();
                            }
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
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) {
                    return;
                }
                resetGame(livesLabel, retriesLabel);
            }
        });
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        hideCardTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();

        show2CardsTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideShow2Cards();
            }
        });
        show2CardsTimer.setRepeats(false);
    }

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
        if (showAllCardsUses <= 0 || !gameReady) return;
        
        showAllCardsUses--;
        showAllCardsBtn.setText("<html><center>Show All Cards<br>(" + showAllCardsUses + " uses left)</center></html>");
        if (showAllCardsUses == 0) {
            showAllCardsBtn.setEnabled(false);
        }

  
        for (int i = 0; i < board.size(); i++) {
            if (!matchedCards.get(i)) {
                board.get(i).setIcon(cardSet.get(i).cardImageIcon);
            }
        }

        Timer showAllTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
         
                for (int i = 0; i < board.size(); i++) {
                    if (!matchedCards.get(i)) {
                        board.get(i).setIcon(cardBackImageIcon);
                    }
                }
            }
        });
        showAllTimer.setRepeats(false);
        showAllTimer.start();
    }

    void useShow2MatchCards() {
        if (show2MatchCardsUses <= 0 || !gameReady) return;
        
        show2MatchCardsUses--;
        show2MatchCardsBtn.setText("<html><center>Show 2 Match Cards<br>(" + show2MatchCardsUses + " uses left)</center></html>");
        if (show2MatchCardsUses == 0) {
            show2MatchCardsBtn.setEnabled(false);
        }

        ArrayList<Integer> availableCards = new ArrayList<>();
        for (int i = 0; i < cardSet.size(); i++) {
            if (!matchedCards.get(i)) {
                availableCards.add(i);
            }
        }

        if (availableCards.size() >= 2) {
          
            String targetCardName = null;
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
                
                show2CardsTimer.restart();
            }
        }
    }

    void hideShow2Cards() {
        for (int i = 0; i < board.size(); i++) {
            if (!matchedCards.get(i) && board.get(i).getIcon() != cardBackImageIcon) {
                if (board.get(i) != card1Selected && board.get(i) != card2Selected) {
                    board.get(i).setIcon(cardBackImageIcon);
                }
            }
        }
    }

    void useExtendLives() {
        if (extendLivesUses <= 0) return;
        
        extendLivesUses--;
        extendLivesBtn.setText("<html><center>Extend Lives<br>(" + extendLivesUses + " use left)</center></html>");
        if (extendLivesUses == 0) {
            extendLivesBtn.setEnabled(false);
        }

        lives += 2;
        for (Component comp : textPanel.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Lives:")) {
                ((JLabel) comp).setText("Lives: " + Integer.toString(lives));
                break;
            }
        }
    }

    void resetGame(JLabel livesLabel, JLabel retriesLabel) {
        gameReady = false;
        restartButton.setEnabled(false);
        card1Selected = null;
        card2Selected = null;
        shuffleCards();
        matchedCards = new ArrayList<>(Collections.nCopies(cardSet.size(), false));

        for (int i = 0; i < board.size(); i++) {
            board.get(i).setIcon(cardSet.get(i).cardImageIcon);
        }

        lives = 3;
        livesLabel.setText("Lives: " + Integer.toString(lives));
        retries += 1;
        retriesLabel.setText("Retries: " + Integer.toString(retries));

        hideCardTimer.start();
    }

    void checkWinCondition() {
        boolean allMatched = true;
        for (Boolean matched : matchedCards) {
            if (!matched) {
                allMatched = false;
                break;
            }
        }
        
        if (allMatched) {
            JOptionPane.showMessageDialog(frame, "Congratulations! You won the game!");
            showAllCardsUses = 3;
            show2MatchCardsUses = 2;
            extendLivesUses = 1;
            updateItemButtons();
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

    void setupCards() {
        cardSet = new ArrayList<Card>();
        for (String cardName : cardList) {
           
            Image cardImg = new ImageIcon(getClass().getResource("src/img/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet);

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

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) { 
            int card1Index = board.indexOf(card1Selected);
            int card2Index = board.indexOf(card2Selected);
            
            if (!matchedCards.get(card1Index)) {
                card1Selected.setIcon(cardBackImageIcon);
            }
            if (!matchedCards.get(card2Index)) {
                card2Selected.setIcon(cardBackImageIcon);
            }
            
            card1Selected = null;
            card2Selected = null;
        }
        else { 
            for (int i = 0; i < board.size(); i++) {
                if (!matchedCards.get(i)) {
                    board.get(i).setIcon(cardBackImageIcon);
                }
            }
            gameReady = true;
            restartButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        new MatchCards();
    }
}