
package esi.atl.g41864.uno.model;

import esi.atl.g41864.uno.objects.Value;
import esi.atl.g41864.uno.objects.ColorUno;
import esi.atl.g41864.uno.objects.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the deck where the cards come from.
 * For the distribution, when a player must pick a card or for the first
 * card of the discard, the cards come from here.
 * 
 * @author G41864
 */
class Deck {
    private final List<Card> deck;
    
    /**
     * This is the constructor of the Deck. It creates cards, using the
 constructor of card, with the two enumerations : Value and ColorUno.
 And it adds each card to its list of cards named "deck".
     */
    Deck() {
        deck = new ArrayList<>();
        for(ColorUno c : ColorUno.values()){
            for(Value v : Value.values()){
                deck.add(new Card(c, v));
                if(v != Value.NUL){
                    deck.add(new Card(c, v));
                }
            }
        }
    }
    
    /**
     * This method receives a list of cards from its parameter.
     * This list of cards is mainly the cards from the discard,
     * and it add them to the list of cards of the deck.
     * After this, it shuffle the list.
     * 
     * @param discard The list of cards from the discard.
     */
    void refresh(List<Card> discard){
        discard.forEach((c) -> {
            deck.add(c);
        });
        Collections.shuffle(deck);
    }
    
    /**
     * This method simply shuffles the deck's list of cards.
     */
    void shuffleDeck(){
        Collections.shuffle(deck);
    }
    
    /**
     * This method removes the first card of the deck.
     * It's used when a player must pick a card or to set the first card
     * in the discard.
     * @return 
     */
    Card removeFirstCard(){
        return deck.remove(0);
    }
    
    /**
     * This method simply indicates if the deck is empty or not.
     * @return 
     */
    boolean isEmpty(){
        return deck.isEmpty();
    }
    
    void removeCard(Card c) {
        for(Card card : deck) {
            if(card.equals(c)) {
                deck.remove(card);
                break;
            }
        }
    }
    
}
