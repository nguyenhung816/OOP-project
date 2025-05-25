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
    

    String[] cardList = { //track cardNames
        
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

    ArrayList<Card> cardSet; //create a deck of cards with cardNames and cardImageIcons
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth; //5*128 = 640px
    int boardHeight = rows * cardHeight; //4*90 = 360px

    JFrame frame = new JFrame("Pokemon Match Cards");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    JLabel livesLabel = new JLabel();
    JLabel retriesLabel = new JLabel();
    JLabel levelLabel = new JLabel();

    int lives = 5;
    int retries = 0;
    ArrayList<JButton> board = new ArrayList<>();
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;
    
    MatchCards() {

        // frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textPanel.setPreferredSize(new Dimension(200, 80));
        textPanel.setLayout(new GridLayout(3, 1));

        //Lives text
        livesLabel = new JLabel("Lives: " + Integer.toString(lives));
        livesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        livesLabel.setHorizontalAlignment(JLabel.CENTER);
        //Retries text
        retriesLabel = new JLabel("Retries: " + Integer.toString(retries));
        retriesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        retriesLabel.setHorizontalAlignment(JLabel.CENTER);
        //Levels text
        levelLabel = new JLabel("Level: " + Integer.toString(level));
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        levelLabel.setHorizontalAlignment(JLabel.CENTER);
        

        textPanel.add(levelLabel);
        textPanel.add(livesLabel);
        textPanel.add(retriesLabel);

        frame.add(textPanel, BorderLayout.EAST);
        textPanel.add(textLabel);

        //card game board
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        frame.add(boardPanel);

        //restart game button
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

            //re assign buttons with new cards
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardSet.get(i).cardImageIcon);
            }

            lives = 5;
            livesLabel.setText("Lives: " + Integer.toString(lives));
            retries += 1;
            retriesLabel.setText("Retries: " + Integer.toString(retries));
            setupNewLevel();
        });
        
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

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
            //load each card image
            Image cardImg = new ImageIcon(getClass().getResource("src/img/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

            //create card object and add to cardSet
            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet);

        while ((cardSet.size() * 2) < (rows * columns)) {  //ensure enough cards
            cardSet.addAll(new ArrayList<>(cardSet));      
        }

        cardSet = new ArrayList<>(cardSet.subList(0, (rows * columns) / 2)); 
        cardSet.addAll(new ArrayList<>(cardSet)); //duplicate for match pairs

        //load the back card image
        Image cardBackImg = new ImageIcon(getClass().getResource("src/img/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        System.out.println(cardSet);
        //shuffle
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size()); //get random index
            //swap
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

        boardPanel.revalidate();
        boardPanel.repaint();
        gameReady = false;
        restartButton.setEnabled(false);
        hideCardTimer.start();
    }

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) { //only flip 2 cards
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        }
        else { //flip all cards face down
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
        }
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
                            
                            setupNewLevel();
                            hideCardTimer.start();
                        } else {
                            hideCardTimer.start();
                        }
                    } else {
                        card1Selected = null;
                        card2Selected = null;
                        checkIfLevelCompleted();
                    }
                }
            }
        }
    };

    //check to see if the level is completed
    //move to the next level if it is
    void checkIfLevelCompleted() { 
        for (JButton tile : board) {
            if (tile.getIcon() == cardBackImageIcon) {
                return;
            }
        }

        if (level < maxLevel) {
            JOptionPane.showMessageDialog(frame, "Level " + level + " completed!\nStarting Level " + (level + 1));
            level++;         
            lives = 5;       
            retries = 0;     
            setupNewLevel(); 
        } else {
            JOptionPane.showMessageDialog(frame, "Congratulations! You have beaten every levels!"); 
            frame.dispose(); 
            //[ADD] implement an option to restart the game frome level 1 or return to the main menu 
        }
    }
}