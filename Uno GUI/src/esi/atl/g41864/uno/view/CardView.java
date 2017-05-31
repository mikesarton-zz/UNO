
package esi.atl.g41864.uno.view;

import esi.atl.g41864.uno.model.Card;
import esi.atl.g41864.uno.model.ColorUno;
import esi.atl.g41864.uno.model.Value;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author mike
 */
class CardView {
    private ImageView iv;
    private CardState state;
    private ColorUno color;
    private Value value;
    
    CardView(ColorUno color, Value value){
        creation(color.getShortcut() + value.getValue());
        this.color = color;
        this.value = value;
        state = CardState.UNSELECTED;
    }
    
    CardView(String cardName) {
        creation(cardName);
        color = null;
        value = null;
        state = null;
    }
    
    private void creation(String name){
        iv = new ImageView();
        iv.setImage(new Image("esi/atl/g41864/uno/resources/images/cards/" + 
                name + ".png"));
        iv.setFitHeight(200);
        iv.setFitWidth(120);
    }
    
    Card getCard(){
        return new Card(color, value);
    }
    
    ImageView getCardView(){
        return iv;
    }
    
    CardState getState(){
        return state;
    }
    
    void setState(CardState cState){
        state = cState;
    }
}
