package esi.atl.g41864.uno.model;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mike
 */
public class PlayerTest {

    /**
     * Test of getName method, of class Player.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Player instance = new Player("Mike", false);
        String expResult = "Mike";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getScore method, of class Player.
     */
    @Test
    public void testGetScore() {
        System.out.println("getScore");
        Player instance = new Player("Mike", false);
        instance.updateScore(500);
        int expResult = 500;
        int result = instance.getScore();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getScore method, of class Player.
     */
    @Test
    public void testGetScore2() {
        System.out.println("getScore");
        Player instance = new Player("Mike", false);
        instance.updateScore(-2500);
        int expResult = -2500;
        int result = instance.getScore();
        assertEquals(expResult, result);
    }

//    /**
//     * Test of getHand method, of class Player.
//     */
//    @Test
//    public void testGetHand() {
//        System.out.println("getHand");
//        Player instance = new Player("Mike", false);
//        
//        Card card0 = new Card(ColorUno.BLUE, Value.FOUR);
//        Card card1 = new Card(ColorUno.GREEN, Value.ONE);
//        Card card2 = new Card(ColorUno.RED, Value.THREE);
//        
//        instance.getHand().add(card0);
//        instance.getHand().add(card1);
//        instance.getHand().add(card2);
//        
//        Hand handResult = new Hand();
//        handResult.add(card0);
//        handResult.add(card1);
//        handResult.add(card2);
//        
//        Hand result = instance.getHand();
//        assertEquals(handResult, result);
//    }

    /**
     * Test of getCards method, of class Player.
     */
    @Test
    public void testGetCards() {
        System.out.println("getCards");
        Player instance = new Player("Mike", false);
        Hand handResult = new Hand();
        
        Card card0 = new Card(ColorUno.BLUE, Value.FOUR);
        Card card1 = new Card(ColorUno.GREEN, Value.ONE);
        Card card2 = new Card(ColorUno.RED, Value.THREE);
        
        instance.getHand().add(card0);
        instance.getHand().add(card1);
        instance.getHand().add(card2);
        
        handResult.add(card0);
        handResult.add(card1);
        handResult.add(card2);
        
        List<Card> expResult = handResult.getCards();
        
        
        List<Card> result = instance.getCards();
        assertEquals(expResult, result);
    }

    /**
     * Test of updateScore method, of class Player.
     */
    @Test
    public void testUpdateScore() {
        System.out.println("updateScore");
        int newScore = 321;
        Player instance = new Player("Mike", false);
        instance.updateScore(newScore);
        
        assertTrue(instance.getScore() == 321);
    }

    /**
     * Test of isIA method, of class Player.
     */
    @Test
    public void testIsIA() {
        System.out.println("isIA");
        Player instance = new Player("Mike", true);
        assertTrue(instance.isIA());
    }
    
        /**
     * Test of isIA method, of class Player.
     */
    @Test
    public void testIsIA2() {
        System.out.println("isIA");
        Player instance = new Player("Mike", false);
        assertFalse(instance.isIA());
    }
    
}
