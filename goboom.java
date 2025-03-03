import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class goboom {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<String> deck = createDeck(); // init. create deck method
        Collections.shuffle(deck); // shuffle deck

        // Assign determinant for first turn trick #1 center card
        String centerCard = deck.remove(0);
        int currentPlayer = getFirstPlayer(centerCard);

        // Deal cards to players
        List<List<String>> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new ArrayList<>());
        }

        // distributes cards from deck to each player
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                players.get(j).add(deck.remove(0));
            }
        }

        List<String> center = new ArrayList<>(); // initialize center pile
        center.add(centerCard); // put the center card in the center pile

        int trickNumber = 1;

        // Menu
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("s: Start a new game");
        System.out.println("x: Exit the game");
        System.out.println("d: Draw a card from the deck");
        System.out.println("card: To play a card");
        System.out.println("load: Load a game from previous save");
        System.out.println("save: Save a game");
        System.out.println("sk: Skip your turn");
        System.out.println("-----------------");

        int turnsPlayed = 0;
        boolean running = true;
        String command;
        do {
            // Game status display
            System.out.println("Trick #" + trickNumber);
            for (int i = 0; i < 4; i++) {
                System.out.println("Player" + (i + 1) + ": " + players.get(i));
            }
            System.out.println("Center : " + center);
            System.out.println("Deck : " + deck);
            System.out.println("Score: Player1 = 0 | Player2 = 0 | Player3 = 0 | Player4 = 0");
            System.out.println("Turn : Player" + currentPlayer);
            System.out.println("-------------");
            System.out.print("> ");
            command = input.nextLine();

            switch (command) {
                
                case "sk":
                    // Skip the player's turn
                    System.out.println("Player" + currentPlayer + " skips their turn.");

                    currentPlayer = (currentPlayer % 4) + 1;
                    turnsPlayed++;
                    System.out.println("Turns Played" + turnsPlayed);

                    if (turnsPlayed == 4) {
                        // Calculate the winning card for the center
                        String winningCard = getWinningCard1(center, centerCard);
                        int winningPlayer = (currentPlayer + center.indexOf(winningCard)) % 4 + 1;
                        System.out.println("*** Player" + winningPlayer + " wins Trick #" + trickNumber + " ***");
                        System.out.println("Winning Card: " + winningCard);
                        centerCard = winningCard;
                        center.clear();
                        currentPlayer = winningPlayer;
                        turnsPlayed = 0;

                        // Increment trick number
                        trickNumber++;
                    }
                    break;
                
                case "s":
                    // Reset everything
                    System.out.println("Game Reset!");
                    deck = createDeck();
                    Collections.shuffle(deck);
                    centerCard = deck.remove(0);
                    currentPlayer = getFirstPlayer(centerCard);
                    players.clear();
                    for (int i = 0; i < 4; i++) {
                        players.add(new ArrayList<>());
                    }
                    for (int i = 0; i < 7; i++) {
                        for (int j = 0; j < 4; j++) {
                            players.get(j).add(deck.remove(0));
                        }
                    }
                    center.clear();
                    center.add(centerCard);
                    trickNumber = 1;
                    break;

                case "x":
                    running = false;
                    System.out.println("Closing Game...");
                    break;

                case "d":
                    if (deck.size() > 0) {
                        String newCard = deck.remove(0);
                        players.get(currentPlayer - 1).add(newCard);
                    }
                    else  { 
                    System.out.println("The deck is empty");
                        boolean hasValidCard = false;

                        // Check if the current player has a valid card to play
                        for (String card : players.get(currentPlayer - 1)) {
                            if (isCardSameSuitOrRank(card, centerCard)) {
                                hasValidCard = true;
                                break;
                            }
                        }

                        if (!hasValidCard) {
                            System.out.println(
                                    "Player" + currentPlayer + " does not have a valid card to play. Skipping turn.");
                            currentPlayer = (currentPlayer % 4) + 1;
                            break;
                        }
                    }

                    break;

                    case "save":
                    try {
                        GameState gameState = new GameState();
                        gameState.deck = new ArrayList<>(deck);
                        gameState.players = new ArrayList<>(players);
                        gameState.center = new ArrayList<>(center);
                        gameState.centerCard = centerCard;
                        gameState.currentPlayer = currentPlayer;
                        gameState.trickNumber = trickNumber;

                        FileOutputStream fos = new FileOutputStream("gameState.dat");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(gameState);
                        oos.close();

                        System.out.println("Game saved.");
                    } catch (IOException e) {
                        System.out.println("An error occurred while saving the game: " + e.getMessage());
                    }
                    break;

                case "load":
                    try {
                        FileInputStream fis = new FileInputStream("gameState.dat");
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        GameState gameState = (GameState) ois.readObject();
                        ois.close();

                        deck = gameState.deck;
                        players = gameState.players;
                        center = gameState.center;
                        centerCard = gameState.centerCard;
                        currentPlayer = gameState.currentPlayer;
                        trickNumber = gameState.trickNumber;

                        System.out.println("Game loaded.");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("An error occurred while loading the game: " + e.getMessage());
                    }
                    break;

                    case "card":
    System.out.println("Choose a card from your hand to play");
    System.out.print("> ");
    String playedCard = input.nextLine();

    // Check if the player has the card in their hand
    if (players.get(currentPlayer - 1).contains(playedCard)) {
        // Check if it's the first trick
        if (trickNumber == 1) {
            // Check if the played card follows the leading card suit and rank
            if (center.isEmpty() || isCardSameSuitOrRank(playedCard, centerCard)) {
                // Remove the played card from the player's hand and add it to the center
                players.get(currentPlayer - 1).remove(playedCard);
                center.add(playedCard);
                System.out.println("Player" + currentPlayer + " played " + playedCard);

                // Check if the center is complete
                if (center.size() == 5 || turnsPlayed == 3) {
                    center.remove(centerCard);
                    String winningCard = getWinningCard1(center, centerCard);
                    int winningPlayer = (currentPlayer + center.indexOf(winningCard)) % 4 + 1;
                    System.out.println("*** Player" + winningPlayer + " wins Trick #" + trickNumber + " ***");
                    System.out.println("Winning Card: " + winningCard);
                    centerCard = winningCard;
                    center.clear();
                    currentPlayer = winningPlayer;
                    turnsPlayed = 0;

                    // Increment trick number
                    trickNumber++;
                } else {
                    currentPlayer = (currentPlayer % 4) + 1;
                    turnsPlayed++;
                }
            } else {
                System.out.println("The played card must follow the leading card suit and rank.");
            }
        } else if (trickNumber >= 2) {
           
            //initializing leading card
            if (center.size() == 0){
            players.get(currentPlayer - 1 ).remove(playedCard);
            center.add(playedCard);
            System.out.println("Player" + currentPlayer + " played " + playedCard);
            currentPlayer = (currentPlayer % 4) + 1;
            break;
            }
            
            String leadcard = center.get(0);
            
            if (isCardSameSuitOrRank(playedCard, leadcard) && center.size() >= 1) {
                // Remove the played card from the player's hand and add it to the center
                players.get(currentPlayer - 1).remove(playedCard);
                center.add(playedCard);
                System.out.println("Player" + currentPlayer + " played " + playedCard);
                        for (int i = 0; i < 4; i++) {
                                        if (players.get(i).isEmpty()) {
                                            System.out.println(
                                                    "Player" + (i + 1) + " has no more cards. The game is over.");

                                            // Calculate points and print the table
                                            System.out.println("+---------+--------+");
                                            System.out.println("| Player  | Points |");
                                            System.out.println("+---------+--------+");
                                            for (int j = 0; j < 4; j++) {
                                                int points = calculatePoints(players.get(j));
                                                System.out.println("| Player" + (j + 1) + " | " + points + "    |");
                                            }
                                            System.out.println("+---------+--------+");

                                            running = false;
                                            break;
                                        }
                                    }
                // Check if the center is complete
                if (center.size() == 4 || turnsPlayed == 3) {
                    String winningCard = getWinningCard2(center, leadcard);
                    int winningPlayer = (currentPlayer + center.indexOf(winningCard)) % 4 + 1;
                    System.out.println("Player" + winningPlayer + " wins Trick #" + trickNumber);
                    System.out.println("Winning Card: " + winningCard);
                    leadcard = winningCard;
                    center.clear();
                    currentPlayer = winningPlayer;
                    turnsPlayed = 0;

                    trickNumber++;
                } else {
                    currentPlayer = (currentPlayer % 4) + 1;
                    turnsPlayed++;
                }
            } else {
                System.out.println("The played card must follow the first player's card suit and rank.");
            }
        }
    } else {
        System.out.println("You do not have this card.");
    }
    break;
            
                default:
                System.out.println("Input invalid! Please choose the following options given (PRESS ENTER TO CONTINUE)");
                try {
                    input.nextLine(); // using your Scanner object
                } catch (Exception e) {
                    System.out.println("An error occurred while waiting for input: " + e.getMessage());
                }
                break;
                
            }
        } while (running);
    }

    // create deck
    public static List<String> createDeck() {
        List<String> deck = new ArrayList<>();
        String[] suits = { "c", "d", "h", "s" };
        String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K", "A" };

        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(suit + rank);
            }
        }

        return deck;
    }

    // firstplayer method
    public static int getFirstPlayer(String card) {
        int firstPlayer = 0;
        char rank = card.charAt(1);
        switch (rank) {
            case 'A':
            case '5':
            case '9':
            case 'K':
                firstPlayer = 1;
                break;
            case '2':
            case '6':
            case 'X':
                firstPlayer = 2;
                break;
            case '3':
            case '7':
            case 'J':
                firstPlayer = 3;
                break;
            case '4':
            case '8':
            case 'Q':
                firstPlayer = 4;
                break;
        }
        return firstPlayer;
    }

    // get the winning card from the center pile for first trick
    public static String getWinningCard1(List<String> center, String centerCard) {
        String winningCard = null;
        char centerSuit = centerCard.charAt(0);

        // Filter for cards that have the same suit as the centerCard
        for (String card : center) {
            if (card.charAt(0) == centerSuit) {
                // If this is the first card with the correct suit or if it has a higher rank
                // than the current winning card
                if (winningCard == null || compareRanks(card.charAt(1), winningCard.charAt(1)) > 0) {
                    winningCard = card;
                }
            }
        }
        return winningCard;
    }

    public static String getWinningCard2(List<String> center, String leadcard) {
        String winningCard = null;
        char leadSuit = leadcard.charAt(0);
        for (String card : center) {
            if (card.charAt(0) == leadSuit) {
                if (winningCard == null || compareRanks(card.charAt(1), winningCard.charAt(1)) > 0) {
                    winningCard = card;
                }
            }
        }
        return winningCard;
    }

    // compare the ranks of two cards
    public static int compareRanks(char rank1, char rank2) {
        String ranks = "23456789XJQKA";
        return ranks.indexOf(rank1) - ranks.indexOf(rank2);
    }

    // Method to check if the played card follows the first player's card suit or rank
    private static boolean isCardSameSuitOrRank(String playedCard, String firstPlayerCard) {
        String playedSuit = playedCard.substring(0, 1);
        String playedRank = playedCard.substring(1);
        String firstPlayerSuit = firstPlayerCard.substring(0, 1);
        String firstPlayerRank = firstPlayerCard.substring(1);
    
        // Compare the suits and ranks
        return playedSuit.equals(firstPlayerSuit) ||  playedRank.equals(firstPlayerRank);
    }   
    public static int calculatePoints(List<String> playerHand) {
        int points = 0;
        for (String card : playerHand) {
            char rank = card.charAt(1);
            switch (rank) {
                case 'A':
                    points += 1;
                    break;
                case 'J':
                case 'Q':
                case 'K':
                case 'X':
                    points += 10;
                    break;
                default:
                    points += Character.getNumericValue(rank);
                    break;
            }
        }
        return points;
    }

}