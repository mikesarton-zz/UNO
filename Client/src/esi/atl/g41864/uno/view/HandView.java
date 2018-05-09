
package esi.atl.g41864.uno.view;

import esi.atl.g41864.uno.objects.Card;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.HBox;

/**
 *
 * @author mike
 */
class HandView extends HBox {
    private List<CardView> list;
    
    HandView(List<Card> cards){
        list  = new ArrayList<>();
        
        cards.forEach((c) -> {
            list.add(new CardView(c.getColor(), c.getValue()));
        });
    }
    
    List<CardView> getHandView(){
        return list;
    }
    
    void setCardsDown(){
        for(CardView card : list){
            if(card.getState() == CardState.SELECTED){
                card.getCardView().setTranslateY(0);
                card.setState(CardState.UNSELECTED);
            }
        }
    }
    
    Card getSelectedCard() throws IllegalArgumentException{
        Card card = null;
        boolean found = false;
        int i = 0;
        
        while(!found && i<list.size()){
            if(list.get(i).getState() == CardState.SELECTED){
                card = list.get(i).getCard();
                found = true;
            }
                ++i;
        }
        
        if(card == null){
            throw new IllegalArgumentException("Aucune carte sélectionnée !");
        }
        
        return card;
    }
    
}
