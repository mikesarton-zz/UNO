package esi.atl.g41864.uno.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mike
 */
public class DeckTest {

    /**
     * Test of isEmpty method, of class Deck.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        Deck instance = new Deck();

        for(int i = 0; i < 76; ++i){
            instance.removeFirstCard();
        }

        boolean result = instance.isEmpty();
        assertTrue(result);
    }
    
    /**
     * Test of isEmpty method, of class Deck.
     */
    @Test
    public void testDeck() {
        System.out.println("Deck()");
        Deck instance = new Deck();
        Card card = instance.removeFirstCard();
        assertTrue("R".equals(card.getColorShortcut()) && 
                "0".equals(card.getValueInt()));
    }
    
}
