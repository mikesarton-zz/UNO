package esi.atl.g41864.uno.model;

import esi.atl.g41864.uno.view.Display;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author mike
 */
public class UnoTest {

    /**
     * Test of start method, of class Uno.
     * @throws java.lang.Exception
     */
    @Test(expected = UnoException.class)
    public void testStart() throws Exception {
        List<String> listNames = Arrays.asList("Mike", "Olivier", "Damien", "Maxime");
        Uno instance = new Uno(listNames);
        instance.start();
        instance.start();
    }

    /**
     * Test of getFlippedCard method, of class Uno.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetFlippedCard() throws Exception {
        System.out.println("----- getFlippedCard -----");
        List<String> listNames = Arrays.asList("Mike", "Olivier", "Damien", "Maxime");
        Uno instance = new Uno(listNames);
        instance.start();
        Card result = instance.getFlippedCard();
        System.out.println("TopCard.value : " + result.getValueInt());
        System.out.println("TopCard.color : " + result.getColorShortcut());
        System.out.println("----- getFlippedCard -----");
    }

    /**
     * Test of getCurrentPlayer method, of class Uno.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetCurrentPlayer() throws Exception {
        System.out.println("----- getCurrentPlayer -----");
        List<String> listNames = Arrays.asList("Mike", "Olivier", "Damien", "Maxime");
        Uno instance = new Uno(listNames);
        instance.start();
        Player result = instance.getCurrentPlayer();
        System.out.println("Current player's name : " + result.getName());
        System.out.println("----- getCurrentPlayer -----");
    }

    /**
     * Test of playCard method, of class Uno.
     * @throws java.lang.Exception
     */
    @Test
    public void testPlayCard() throws Exception {
        System.out.println("----- playCard -----");
        List<String> listNames = Arrays.asList("Mike", "Olivier", "Damien", "Maxime");
        Uno instance = new Uno(listNames);
        instance.start();
        Player pl = instance.getCurrentPlayer();
        int cardId = instance.listPossibleCards().get(0);
        System.out.println("Carte possible du CP : " 
                + pl.getCards().get(cardId).getValueInt() 
                + " " + pl.getCards().get(cardId).getColorShortcut());
        System.out.println("Top card : " + instance.getFlippedCard().getValueInt() 
                + " " + instance.getFlippedCard().getColorShortcut());
        instance.playCard(cardId);
        System.out.println("Top card : " + instance.getFlippedCard().getValueInt()+  " " + instance.getFlippedCard().getColorShortcut());
        System.out.println("----- playCard -----");
    }

    /**
     * Test of listPossibleCards method, of class Uno.
     * @throws java.lang.Exception
     */
    @Test
    public void testListPossibleCards() throws Exception {
        System.out.println("----- listPossibleCards -----");
        List<String> listNames = Arrays.asList("Mike", "Olivier", "Damien", "Maxime");
        Uno instance = new Uno(listNames);
        instance.start();
        System.out.println("Top card : ");
        Display.displayCard(instance.getFlippedCard());
        System.out.println("Cartes du CP possibles : ");
        List<Card> playersCards = instance.getCurrentPlayer().getCards();
        List<Integer> indexCardsPossible = instance.listPossibleCards();
        List<Card> CardsPossible = new ArrayList<>();
        for(int i=0 ; i<indexCardsPossible.size(); ++i){
            CardsPossible.add(playersCards.get(indexCardsPossible.get(i)));
        }
        Display.displayHand(CardsPossible);
        System.out.println("----- listPossibleCards -----");
    }

    /**
     * Test of pickCard method, of class Uno.
     * @throws java.lang.Exception
     */
    @Test
    public void testPickCard() throws Exception {
        System.out.println("----- pickCard -----");
        List<String> listNames = Arrays.asList("Mike", "Olivier", "Damien", "Maxime");
        Uno instance = new Uno(listNames);
        instance.start();
        System.out.println("Cartes actuelles du CP : ");
        Display.displayHand(instance.getCurrentPlayer().getCards());
        instance.pickCard();
        System.out.println("Cartes après pickCard()");
        Display.displayHand(instance.getCurrentPlayer().getCards());
        System.out.println("----- pickCard -----");
    }

//    /**
//     * Test of endMove method, of class Uno.
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testEndMove() throws Exception {
//        System.out.println("endMove");
//        Uno instance = null;
//        instance.endMove();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of isOver method, of class Uno.
//     */
//    @Test
//    public void testIsOver() throws Exception {
//        System.out.println("isOver");
//        Uno instance = null;
//        boolean expResult = false;
//        boolean result = instance.isOver();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
