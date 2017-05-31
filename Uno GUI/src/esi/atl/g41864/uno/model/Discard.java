package esi.atl.g41864.uno.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the discard of the game. It is here that de players
 * will add their cards.
 * This class has a list of cards named "discard", it represents all the cards,
 * and a Card named "topCard", it's the card on the top of the discard.
 * @author mike
 */
class Discard {
    private final List<Card> discard = new ArrayList<>();
    private Card topCard;
    
    /**
     * This method receives a card as parameter and add her to the discard's
     * list of cards. Then, it sets the topCard as this card.
     * @param card 
     */
    void addCard(Card card){
        discard.add(card);
        topCard = card;
    }
    
    /**
     * This method returns the top card of the discard.
     * @return The top card of the discard.
     */
    Card getTopCard() {
        return topCard;
    }
    
    /**
     * This method returns the number of cards available to be taken.
     * It's used when the deck is empty and we want to know if some cards
     * from the discard can be taken. 
     * The integer is simply the size of the discard, less one (for the top card).
     * @return The number of cards available to be taken.
     */
    int cardsAvailable(){
        return discard.size() - 1;
    }
    
    /**
     * This method returns a list with all the cards excepted the top card.
     * @return A list with all the cards excepted the top card.
     */
    List<Card> getCards(){
        List<Card> list = new ArrayList<>(discard);
        list.remove(topCard);
        discard.removeAll(list);
        return list;
    }
    
    /**
     * This method simply clear the list of cards.
     */
    void clear(){
        discard.clear();
    }
    
    /**
     * This method simply indicates if the list of cards is empty or not.
     * @return True is the list is empty, false otherwise.
     */
    boolean isEmpty(){
        return discard.isEmpty();
    }
}
