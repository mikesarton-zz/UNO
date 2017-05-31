package esi.atl.g41864.uno.model;

import esi.atl.g41864.uno.objects.Value;
import esi.atl.g41864.uno.objects.Player;
import esi.atl.g41864.uno.objects.ColorUno;
import esi.atl.g41864.uno.objects.Card;
import esi.atl.g41864.uno.observers.Observable;
import esi.atl.g41864.uno.observers.Observer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is the interface between the view and the model. It gives to the
 * view the useful informations and do all the calculs here by calling the
 * others classes in the model.
 *
 * @author G41864
 */
public class Uno implements Observable {

    private Deck deck;
    private Discard discard;
    private Player currentPlayer;
    private final List<Player> players;
    private StateOfGame state = StateOfGame.STOPPED;
    private int scoreToReach;
    private boolean isOver;
    private List<Observer> observers;

    /**
     * This is the constructor for a Uno game. It receives a list of string as
     * parameter. This list contains the names of the players. The constructor
     * first check if the number of players is correct. Then, for each name, it
     * creates a player. If the name starts by "IA", it sets the second
     * parameter of the player's constructor to true. Then the constructor
     * creates a deck and a discard.
     *
     * @param playerName The name of the player.
     * @throws UnoException If the game is running or if a name is empty.
     */
    public Uno(String playerName) throws UnoException {
        if (state != StateOfGame.STOPPED) {
            throw new UnoException("Uno() ne peut pas être appelé si le jeu"
                    + " n'est pas en train de tourner.");
        }
        players = new ArrayList<>();
        players.add(new Player(playerName, false));
        players.add(new Player("IA", true));
        observers = new ArrayList<>();
        isOver = false;
        scoreToReach = 100;
        discard = new Discard();
        deck = new Deck();
    }

    /**
     * This method starts the game. The game's state must be "stopped". This
     * method clears the discard if she isn't empty, creates a new deck,
     * shuffles the deck, distributes the cards to the players, add one card to
     * the discard and then choose the first player to play. Finally, it sets
     * the game's state to "running".
     *
     * @throws UnoException If the game is running.
     */
    public void start() throws UnoException {
        if (state != StateOfGame.STOPPED) {
            throw new UnoException("start() ne peut pas être appelé si"
                    + " le jeu n'est pas en train de tourner.");
        }

        if (!discard.isEmpty()) {
            discard.clear();
        }

        deck = new Deck();
        deck.shuffleDeck();

        distributeCards();

        discard.addCard(deck.removeFirstCard());
        currentPlayer = players.get(new Random().nextInt(players.size()));
        state = StateOfGame.RUNNING;
        
        if(isOver) {
            isOver = false;
        }
        
        notifyObs();
        
        if (currentPlayer.isIA()) {
            IAMove();
        }
    }
    
    /**
     * This method indicates wheter the deck is empty or not.
     * @return True if the deck is empty, false otherwise.
     */
    public boolean isDeckEmpty(){
        return deck.isEmpty();
    }

    /**
     * This method restart the game when the round is ended but the score
     * isn't reached yet.
     * @throws UnoException See start() method.
     */
    public void restart() throws UnoException {
        if (state == StateOfGame.RUNNING) {
            state = StateOfGame.STOPPED;
        }
        resetScores();
        start();
    }
    
    public void resetGame(int playerScore, String currentPlayerName, int IAScore, int scoreToReach, String topCard, List<String> playerCards, List<String> IACards) {
        state = StateOfGame.RUNNING;
        for(Player pl : players) {
            if(pl.getName().equalsIgnoreCase("IA")) {
                pl.updateScore(IAScore);
                pl.getHand().clear();
                for(String str : IACards) {
                    ColorUno col = ColorUno.getEnumType(str.substring(0, 1));
                    Value val = Value.getEnumValue(str.substring(1));
                    pl.getHand().add(new Card(col, val));
                }
            } else {
                pl.updateScore(playerScore);
                pl.getHand().clear();
                for(String str : playerCards) {
                    ColorUno col = ColorUno.getEnumType(str.substring(0, 1));
                    Value val = Value.getEnumValue(str.substring(1));
                    pl.getHand().add(new Card(col, val));
                }
            }
            if (pl.getName().equalsIgnoreCase(currentPlayerName)) {
                currentPlayer = pl;
            }
        }
        this.scoreToReach = scoreToReach;
        ColorUno col = ColorUno.getEnumType(topCard.substring(0, 1));
        Value val = Value.getEnumValue(topCard.substring(1));
        discard.addCard(new Card(col, val));
        for(Player pl : players) {
            for(Card c : pl.getCards()) {
                deck.removeCard(c);
            }
        }
        deck.removeCard(discard.getTopCard());
        deck.shuffleDeck();
        notifyObs();
    }

    /**
     * This method set the score of each player to 0.
     */
    private void resetScores() {
        players.forEach((pl) -> {
            pl.updateScore(-pl.getScore());
        });
    }

    /**
     * This method returns the list of players.
     * @return The list of players.
     */
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     * This method returns true if the game is over, false otherwise.
     * @return the attribute isOver.
     */
    public boolean getIsOver(){
        return isOver;
    }

    /**
     * This method distributes the cards from the deck to the players. If the
     * players already have cards, it cleans their hands. Each player receives 7
     * cards.
     */
    private void distributeCards() {
        players.forEach((pl) -> {
            pl.getHand().clear();
        });

        for (int i = 1; i <= 7; ++i) {
            players.forEach((pl) -> {
                pl.getHand().add(deck.removeFirstCard());
            });
        }
    }

    /**
     * This method returns the card on the top of the discard.
     *
     * @return The card on the top of the discard.
     * @throws UnoException If the game is stopped.
     */
    public Card getFlippedCard() throws UnoException {
        if (state != StateOfGame.RUNNING) {
            throw new UnoException("getFlippedCard() ne peut pas être appelé"
                    + " si le jeu n'est pas en train de tourner.");
        }
        return discard.getTopCard();
    }

    /**
     * This method returns the current player.
     *
     * @return The current player.
     * @throws UnoException If the game is stopped.
     */
    public Player getCurrentPlayer() throws UnoException {
        if (state != StateOfGame.RUNNING) {
            throw new UnoException("getCurrentPlayer() ne peut pas être appelé"
                    + " si le jeu n'est pas en train de tourner.");
        }

        return currentPlayer;
    }

    /**
     * This method sets the current player to the next one.
     *
     * @throws UnoException If the game is stopped.
     */
    private void nextPlayer() throws UnoException {
        int index = players.indexOf(currentPlayer) + 1;
        if (index == players.size()) {
            index = 0;
        }
        currentPlayer = players.get(index);
        if (currentPlayer.isIA()) {
            IAMove();
        }
        
    }

    private void IAMove() throws UnoException {
        List<Card> IAHand = currentPlayer.getHand().getCards();
        int i = 0;
        boolean found = false;
        while (!found && i < IAHand.size()) {
            found = isPlayableCard(IAHand.get(i));
            if (found) {
                playCard(IAHand.get(i));
                break;
            }
            ++i;
        }
        if (!found) {
            pickCard();
        }
    }

    /**
     * This method receives an integer as parameter. This parameter is the index
     * of the card the player wants to play from his list of cards.
     *
     * @param card
     * @throws UnoException If the game is stopped or if the index isn't in the
     * range of the list or if the card can't be played at this moment.
     */
    public void playCard(Card card) throws UnoException {
        if (state != StateOfGame.RUNNING) {
            throw new UnoException("playCard() ne peut pas être appelé "
                    + "si le jeu n'est pas en train de tourner.");
        }

        if (!currentPlayer.getHand().cardIsInHand(card)) {
            throw new UnoException("Vous n'avez pas cette carte dans votre main.");
        }

        if (!isPlayableCard(card)) {
            throw new UnoException("Vous ne pouvez pas jouer cette carte.");
        }

        discard.addCard(currentPlayer.getHand().removeCard(card));
        endMove();
    }

    /**
     * This method looks for the cards that can be played in the current
     * player's hand. If none card can be played, the size of the list is 0.
     *
     * @param card
     * @return The list with the index of all the cards than can be played.
     * @throws UnoException If the game is stopped.
     */
    private boolean isPlayableCard(Card card) throws UnoException {
        if (state != StateOfGame.RUNNING) {
            throw new UnoException("isPlayableCard() ne peut pas être appelé"
                    + " si le jeu n'est pas en train de tourner.");
        }

        return discard.getTopCard().getColor().equals(card.getColor())
                || discard.getTopCard().getValue().equals(card.getValue());
    }

    /**
     * This method picks a card in the deck and add it to the current player's
     * hand.
     *
     * @throws UnoException If the game is stopped or if the deck is empty.
     */
    public void pickCard() throws UnoException {
        if (state != StateOfGame.RUNNING) {
            throw new UnoException("pickCard() ne peut pas être appelé"
                    + " si le jeu n'est pas en train de tourner.");
        }

        if (!deck.isEmpty()) {
            currentPlayer.getHand().add(deck.removeFirstCard());
        } else {
            throw new UnoException("Le deck est vide. Vous devez jouer une carte.");
        }

        endMove();
    }

    /**
     * This method checks if the deck is empty and if so, if some cards are
     * available in the discard to move them into the deck. Then, it checks if
     * the current player has only one card, if so, print UNO. Then, it checks
     * if the current player's hand is empty, if not, changes the current player
     * to the next one.
     *
     * @throws UnoException If the game is stopped.
     */
    private void endMove() throws UnoException {
        if (state != StateOfGame.RUNNING) {
            throw new UnoException("endMove() ne peut pas être appelé"
                    + " si le jeu n'est pas en train de tourner.");
        }

        if (deck.isEmpty() && discard.cardsAvailable() >= 1) {
            deck.refresh(discard.getCards());
        }

        if (!currentPlayer.getHand().isEmpty()) {
            nextPlayer();
            if(!isOver){
                notifyObs();
            }
        } else {
            isOver();
        }
    }

    /**
     * This method checks if the current player's hand is empty. If so, it sets
     * the game's state to "stopped", and calls methods to calcul the score of
     * add to the current player. It prints the new score of the player. If the
     * goal score is reached, it prints also a score board. If not, it starts a                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              