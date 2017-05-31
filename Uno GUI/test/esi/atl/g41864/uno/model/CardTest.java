package esi.atl.g41864.uno.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mike
 */
public class CardTest {

    /**
     * Test of getColor method, of class Card.
     */
    @Test
    public void testGetColor() {
        System.out.println("getColor");
        Card instance = new Card(ColorUno.GREEN, Value.NINE);
        ColorUno expResult = ColorUno.GREEN;
        ColorUno result = instance.getColor();
        assertEquals(expResult, result);
    }

    /**
     * Test of getColorShortcut method, of class Card.
     */
    @Test
    public void testGetColorShortcut() {
        System.out.println("getColorShortcut");
        Card instance = new Card(ColorUno.RED, Value.FIVE);
        String expResult = "R";
        String result = instance.getColorShortcut();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValue method, of class Card.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Card instance = new Card(ColorUno.BLUE, Value.SEVEN);
        Value expResult = Value.SEVEN;
        Value result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValueInt method, of class Card.
     */
    @Test
    public void testGetValueInt() {
        System.out.println("getValueInt");
        Card instance = new Card(ColorUno.YELLOW, Value.ONE);
        String expResult = "1";
        String result = instance.getValueInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class Card.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        Card card = new Card(ColorUno.BLUE, Value.FOUR);
        Card instance = new Card(ColorUno.BLUE, Value.FOUR);
        int expResult = 0;
        int result = instance.compareTo(card);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of compareTo method, of class Card.
     */
    @Test
    public void testCompareTo2() {
        System.out.println("compareTo");
        Card card = new Card(ColorUno.GREEN, Value.FOUR);
        Card instance = new Card(ColorUno.BLUE, Value.FOUR);
        int result = instance.compareTo(card);
        assertTrue(result < 0);
    }
    
    /**
     * Test of compareTo method, of class Card.
     */
    @Test
    public void testCompareTo3() {
        System.out.println("compareTo");
        Card card = new Card(ColorUno.BLUE, Value.FOUR);
        Card instance = new Card(ColorUno.YELLOW, Value.FOUR);
        int result = instance.compareTo(card);
        assertTrue(result > 0);
    }
    
    /**
     * Test of compareTo method, of class Card.
     */
    @Test
    public void testCompareTo4() {
        System.out.println("compareTo");
        Card card = new Card(ColorUno.YELLOW, Value.FOUR);
        Card instance = new Card(ColorUno.YELLOW, Value.FOUR);
        int result = instance.compareTo(card);
        assertTrue(result == 0);
    }
    
    /**
     * Test of compareTo method, of class Card.
     */
    @Test
    public void testCompareTo5() {
        System.out.println("compareTo");
        Card card = new Card(ColorUno.YELLOW, Value.SIX);
        Card instance = new Card(ColorUno.YELLOW, Value.NINE);
        int result = instance.compareTo(card);
        assertTrue(result == 1);
    }
    
    /**
     * Test of compareTo method, of class Card.
     */
    @Test
    public void testCompareTo6() {
        System.out.println("compareTo");
        Card card = new Card(ColorUno.YELLOW, Value.SEVEN);
        Card instance = new Card(ColorUno.YELLOW, Value.NUL);
        int result = instance.compareTo(card);
        assertTrue(result == -1);
    }
    
}
