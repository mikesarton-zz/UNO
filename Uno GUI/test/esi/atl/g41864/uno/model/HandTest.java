package esi.atl.g41864.uno.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mike
 */
public class HandTest {
    
    /**
     * Test of add method, of class Hand.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Card card = new Card(ColorUno.GREEN, Value.NUL);
        Hand instance = new Hand();
        instance.add(card);
        Card expectedCard = instance.removeCard(0);
        
        assertTrue(expectedCard.getColorShortcut().equalsIgnoreCase("V") &&
                expectedCard.getValueInt().equalsIgnoreCase("0"));
    }

    /**
     * Test of getCards method, of class Hand.
     */
    @Test
    public void testGetCards() {
        System.out.println("getCards");
        Hand instance = new Hand();
        List<Card> expResult = new ArrayList<>();
        
        Card card0 = new Card(ColorUno.BLUE, Value.NUL);
        Card card1 = new Card(ColorUno.BLUE, Value.FIVE);
        Card card2 = new Card(ColorUno.BLUE, Value.FOUR);
        Card card3 = new Card(ColorUno.BLUE, Value.NINE);
        
        instance.add(card0);
        instance.add(card1);
        instance.add(card2);
        instance.add(card3);
        
        expResult.add(card0);
        expResult.add(card2);
        expResult.add(card1);
        expResult.add(card3);
        
        
        List<Card> result = instance.getCards();
        assertEquals(expResult, result);
    }

    /**
     * Test of removeCard method, of class Hand.
     */
    @Test
    public void testRemoveCard() {
        System.out.println("removeCard");
        Hand instance = new Hand();
        
        Card card0 = new Card(ColorUno.BLUE, Value.NUL);
        Card card1 = new Card(ColorUno.BLUE, Value.FIVE);
        Card card2 = new Card(ColorUno.BLUE, Value.FOUR);
        Card card3 = new Card(ColorUno.BLUE, Value.NINE);
        
        instance.add(card0);
        instance.add(card1);
        instance.add(card2);
        instance.add(card3);
        
        Card expResult = new Card(ColorUno.BLUE, Value.FIVE);
        Card result = instance.removeCard(2);
        assertTrue(expResult.getColorShortcut().equalsIgnoreCase(result.getColorShortcut()) && 
                expResult.getValueInt().equalsIgnoreCase(result.getValueInt()));
    }

    /**
     * Test of isEmpty method, of class Hand.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        Hand instance = new Hand();

        assertTrue(instance.isEmpty());
    }

    /**
     * Test of clear method, of class Hand.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        Hand instance = new Hand();
        
        Card card0 = new Card(ColorUno.BLUE, Value.NUL);
        Card card1 = new Card(ColorUno.BLUE, Value.FIVE);
        Card card2 = new Card(ColorUno.BLUE, Value.FOUR);
        Card card3 = new Card(ColorUno.BLUE, Value.NINE);
        
        instance.add(card0);
        instance.add(card1);
        instance.add(card2);
        instance.add(card3);
        
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
}
