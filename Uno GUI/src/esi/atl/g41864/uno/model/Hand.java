package esi.atl.g41864.uno.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the hand of the player.
 * It contains a list of cards, which is all the cards of the player.
 * @author G41864
 */
class Hand {

    private final List<Card> hand = new ArrayList<>();

    /**
     * This method receives a Card as parameter and add it to the list of cards.
     * Then, it sorts the list of cards to regroup them by color, then by value.
     * @param card 
     */
    void add(Card card) {
        hand.add(card);
        Collections.sort(hand);
    }

    /**
     * This method returns the list of cards.
     * @return The list of cards of the players.
     */
    List<Card> getCards() {
        return hand;
    }

    /**
     * This method receives an integer as parameter. It is the card's index
     * to remove into the list of cards.
     * @param index The card's index in the list of cards.
     * @return The card removed.
     */
    Card removeCard(Card card) throws UnoException {
        boolean found = false;
        int i=-1;
        
        while(!found && i<hand.size()){
            i++;
            if(hand.get(i).equals(card)){
                found = true;
            }
        }
        
        if(found){
            return hand.remove(i);
        } else {
            throw new UnoException("Carte non trouvée dans la main.");
        }
    }

    /**
     * This method simply indicates if the list of card is empty or not.
     * @return True if the list of cards is empty, false otherwise.
     */
    boolean isEmpty() {
        return hand.isEmpty();
    }
    
    /**
     * This method clear the list of cards.
     */
    void clear(){
        hand.clear();
    }
    
    boolean cardIsInHand(Card card){
        for(int i=0; i<hand.size(); ++i){
            if(hand.get(i).equals(card)){
                return true;
            }
        }
        return false;
    }
}
