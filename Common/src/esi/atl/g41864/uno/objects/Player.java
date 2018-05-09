package esi.atl.g41864.uno.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a player.
 * It as a name (string), a score (int), a hand (Hand) and a boolean indicating
 * if he's a IA or not.
 * @author G41864
 */
public class Player implements Serializable {

    private final String name;
    private int score;
    private final Hand hand;
    private final boolean ia;

    /**
     * This is the constructor of a player.
     * It receives two parameter, a string for his name and a boolean indicating
     * if he's an IA or not.
     * 
     * If the player is an IA, his name will be IAx.
     * 
     * This constructor set the player's name, the boolean to true or false 
     * and the score to 0.
     * @param name
     * @param isIA 
     */
    public Player(String name, boolean isIA) {
        ia = isIA;
        this.name = name;
        score = 0;
        hand = new Hand();
    }

    /**
     * This method returns the name of the player.
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the score of the player.
     * @return The score of the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * This method returns the hand of the player.
     * @return The hand of the player.
     */
    public Hand getHand() {
        return hand;
    }
    
    /**
     * This method returns a list of the cards in the player's hand.
     * @return A list of the cards in the player's hand.
     */
    public List<Card> getCards(){
        return new ArrayList<>(hand.getCards());
    }

    /**
     * This method receives a score as parameter and add it to the player's score.
     * @param newScore The score to add to the player.
     */
    public void updateScore(int newScore) {
        score += newScore;
    }
    
    /**
     * This method indicates if the player is a IA or not.
     * @return True if the player is an IA, false otherwise.
     */
    public boolean isIA(){
        return ia;
    }
}
