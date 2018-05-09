
package esi.atl.g41864.uno.messages;

import esi.atl.g41864.uno.objects.Card;
import esi.atl.g41864.uno.objects.Player;
import java.io.Serializable;
import java.util.List;

/**
 * This class is used as protocol to update the client's view.
 * @author mike
 */
public class GameState implements Serializable {
    
    private final boolean isOver;
    private final boolean isDeckEmpty;
    private final List<Player> players;
    private final List<Card> cardsCurrentPlayer;
    private final Card flippedCard;
    private final Player currentPlayer;
    private final int scoreToReach;
    
    /**
     *
     * @param isOver True if the game is over, falte otherwise
     * @param isDeckEmpty True if the game is empty, false otherwise
     * @param players The players's list
     * @param cardsCurrentPlayer The current player's cards
     * @param flippedCard The top card of the game
     * @param currentPlayer The current player
     * @param scoreToReach The score to reach
     */
    public GameState(boolean isOver, boolean isDeckEmpty, List<Player> players, 
            List<Card> cardsCurrentPlayer, Card flippedCard, Player currentPlayer,
                                                            int scoreToReach) {
        
        this.isOver = isOver;
        this.isDeckEmpty = isDeckEmpty;
        this.players = players;
        this.cardsCurrentPlayer = cardsCurrentPlayer;
        this.flippedCard = flippedCard;
        this.currentPlayer = currentPlayer;
        this.scoreToReach = scoreToReach;
    }
    
    public GameState (GameState game) {
        this.isOver = game.getIsOver();
        this.isDeckEmpty = game.isDeckEmpty();
        this.players = game.getPlayers();
        this.cardsCurrentPlayer = game.getCardsCurrentPlayer();
        this.flippedCard = game.getFlippedCard();
        this.currentPlayer = game.getCurrentPlayer();
        this.scoreToReach = game.getScoreToReach();
    }
    
    /**
     *
     * @return True if the game is over, false otherwise
     */
    public boolean getIsOver() {
        return isOver;
    }
    
    /**
     *
     * @return True if the deck is empty, false otherwise
     */
    public boolean isDeckEmpty() {
        return isDeckEmpty;
    }
    
    /**
     *
     * @return The players's list
     */
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     *
     * @return The current player's cards
     */
    public List<Card> getCardsCurrentPlayer() {
        return cardsCurrentPlayer;
    }
    
    /**
     *
     * @return The top card of the game
     */
    public Card getFlippedCard() {
        return flippedCard;
    }
    
    /**
     *
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     *
     * @return The score to reach
     */
    public int getScoreToReach() {
        return scoreToReach;
    }
}
