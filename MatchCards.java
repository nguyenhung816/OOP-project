import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MatchCards {
    //VARIABLES
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
    int show2CardIndex1 = -1;
    int show2CardIndex2 = -1;

    ArrayList<Card> cardSet; //create a deck of cards with cardNames and cardImageIcons
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth; //5*128 = 640px
    int boardHeight = rows * cardHeight; //4*90 = 360px

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
    
    //game starts here
    MatchCards() {
        startTime = System.currentTimeMillis();
        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth + 300, boardHeight + 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        infoPanel.setPreferredSize(new Dimension(250, 80));
        infoPanel.setLayout(new GridLayout(5, 1));

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
        //Score text
        scoreLabel = new JLabel("Score: " + Integer.toString(score));
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        //Timer text
        timeLabel = new JLabel("Time: 00:00");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        
        //add the info panel to the right
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

        //add the item panels to the left
        setupItemsPanel();
        frame.add(itemsPanel, BorderLayout.WEST);

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
            scoreLabel.setText("Score " + Integer.toString(score));
            setupNewLevel();
        });

        //exit game / return to main menu button
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
                    //new App();
                    SwingUtilities.invokeLater(MainMenu::new);
                } catch (Exception ex) {
                    System.err.println("Error opening MainMenu: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        //add the restart and exit button to the bottom of the screen
        bottomGamePanel.add(restartButton);
        bottomGamePanel.add(exitButton);
        frame.add(bottomGamePanel, BorderLayout.SOUTH);
        bottomGamePanel.revalidate();
        bottomGamePanel.repaint();
        frame.revalidate();
        frame.repaint();

        hideCardTimer = new Timer(3000, e -> hideCards());
        hideCardTimer.setRepeats(false);

        show2CardsTimer = new Timer(3000, e-> hideShow2Cards());

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
        frame.setSize(boardWidth + 300, boardHeight + 200);
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
        scoreLabel.setText("Score: " + Integer.toString(score));
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

        boardPanel.revalidate();
        boardPanel.repaint();
        frame.pack();
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
            exitButton.setEnabled(true);
            bottomGamePanel.revalidate();
            bottomGamePanel.repaint();
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

    Timer showAllTimer = new Timer(3000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            
            for(int i = 0; i < board.size(); i++){
                board.get(i).setIcon(cardBackImageIcon);
            }
        }
    });

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

    //check to see if the level is completed
    //move to the next level if it is
    void checkIfLevelCompleted() { 
        for (JButton tile : board) {
            if (tile.getIcon() == cardBackImageIcon) {
                return;
            }
        }

        int levelBonus = calculateLevelBonus();
        score += levelBonus;

        if (level < maxLevel) {
            JOptionPane.showMessageDialog(frame, "Level " + level + " completed!\nStarting Level " + (level + 1));
            level++;         
            lives = 5;       
            retries = 0;
            scoreLabel.setText("Score: " + Integer.toString(score));
            setupNewLevel(); 
        } else {
            long endTime = System.currentTimeMillis();
            long elaspedTime = endTime - startTime;
            String formattedTime = formatTime(elaspedTime);
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
                    board.get(index).setIcon(cardBackImageIcon); // Flip back
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

    void hideShow2Cards() {
        if (show2CardIndex1 != -1) {
            board.get(show2CardIndex1).setIcon(cardBackImageIcon);
            board.get(show2CardIndex2).setIcon(cardBackImageIcon);
            show2CardIndex1 = -1;
            show2CardIndex2 = -1;
        }
    }

    void useExtendLives() {
        if (extendLivesUses <= 0) return;
        
        extendLivesUses--;
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

    void updateItemButtons() {
        showAllCardsBtn.setText("<html><center>Show All Cards<br>(" + showAllCardsUses + " uses left)</center></html>");
        showAllCardsBtn.setEnabled(showAllCardsUses > 0);
        
        show2MatchCardsBtn.setText("<html><center>Show 2 Match Cards<br>(" + show2MatchCardsUses + " uses left)</center></html>");
        show2MatchCardsBtn.setEnabled(show2MatchCardsUses > 0);
        
        extendLivesBtn.setText("<html><center>Extend Lives<br>(" + extendLivesUses + " use left)</center></html>");
        extendLivesBtn.setEnabled(extendLivesUses > 0);
    }
}