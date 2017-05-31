package esi.atl.g41864.uno.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mike
 */
public class DiscardTest {

    /**
     * Test of addCard method, of class Discard.
     */
    @Test
    public void testAddCard() {
        System.out.println("addCard");
        Card card = new Card(ColorUno.RED, Value.NINE);
        Discard instance = new Discard();
        instance.addCard(card);
        Card expectedResult = instance.getTopCard();
        assertTrue(expectedResult.getColorShortcut().equalsIgnoreCase("R") && 
                expectedResult.getValueInt().equalsIgnoreCase("9"));
    }

    /**
     * Test of getTopCard method, of class Discard.
     */
    @Test
    public void testGetTopCard() {
        System.out.println("getTopCard");
        Card card = new Card(ColorUno.GREEN, Value.FOUR);
        Discard instance = new Discard();
        instance.addCard(card);
        Card expectedResult = instance.getTopCard();
        assertTrue(expectedResult.getColorShortcut().equalsIgnoreCase("V") && 
                expectedResult.getValueInt().equalsIgnoreCase("4"));
    }

    /**
     * Test of cardsAvailable method, of class Discard.
     */
    @Test
    public void testCardsAvailable() {
        System.out.println("cardsAvailable");
        Discard instance = new Discard();
        Card card = new Card(ColorUno.BLUE, Value.EIGHT);
        
        for(int i=0; i<=15; ++i){
            instance.addCard(card);
        }
        
        int expectedResult = 15;
        assertTrue(instance.cardsAvailable() == expectedResult);
    }

    /**
     * Test of getCards method, of class Discard.
     */
    @Test
    public void testGetCards() {
        System.out.println("getCards");
        Discard instance = new Discard();
        Card card0 = new Card(ColorUno.GREEN, Value.ONE);
        Card card1 = new Card(ColorUno.GREEN, Value.TWO);
        Card card2 = new Card(ColorUno.GREEN, Value.THREE);
        Card card3 = new Card(ColorUno.GREEN, Value.FOUR);
        
        instance.addCard(card0);
        instance.addCard(card1);
        instance.addCard(card2);
        instance.addCard(card3);
        
        List<Card> expResult = new ArrayList<>();
        expResult.add(card0);
        expResult.add(card1);
        expResult.add(card2);
        
        List<Card> result = instance.getCards();
        assertEquals(expResult, result);
    }

    /**
     * Test of clear method, of class Discard.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        Discard instance = new Discard();
        
        Card card0 = new Card(ColorUno.GREEN, Value.ONE);
        Card card1 = new Card(ColorUno.GREEN, Value.TWO);
        Card card2 = new Card(ColorUno.GREEN, Value.THREE);
        Card card3 = new Card(ColorUno.GREEN, Value.FOUR);
        
        instance.addCard(card0);
        instance.addCard(card1);
        instance.addCard(card2);
        instance.addCard(card3);
        
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    /**
     * Test of isEmpty method, of class Discard.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        Discard instance = new Discard();
        assertTrue(instance.isEmpty());
    }
    
}
