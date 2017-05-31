package esi.atl.g41864.uno.model;

/**
 *
 * @author G41864
 This class represents a card.
 It has a ColorUno and a Value from two enumerations.
 */
public class Card implements Comparable<Card>{

    private final ColorUno color;
    private final Value value;

    /**
     * This is the constructor of the class Card.
     * @param col The color of the card.
     * @param val The value of the card.
     */
    
    public Card(ColorUno col, Value val) {
        color = col;
        value = val;
    }

    /**
     * getColor() returns the Card's color.
     * @return The Card's color.
     */
    public ColorUno getColor() {
        return color;
    }
    
    /**
     * getColorShortcut() returns the letter of the Card's color.
     * @return The letter of the Card's color.
     */
    public String getColorShortcut(){
        return color.getShortcut();
    }

    /**
     * getValue() returns the Card's value.
     * @return The Card's value.
     */
    public Value getValue() {
        return value;
    }
    
    /**
     * getValueInt() returns the integer corresponding to the Card's value.
     * @return A integer corresponding to the Card's value.
     */
    public String getValueInt(){
        return value.getValue();
    }
    
    boolean equals(Card other){
        return color.equals(other.getColor()) && value.equals(other.getValue());
    }

    @Override
    public int compareTo(Card card) {
        if(this.equals(card)){
            return 0;
        } else if (color.equals(card.getColor())){
            if(Integer.valueOf(value.getValue()) < Integer.valueOf(
                    card.getValue().getValue())){
                return -1;
            } else {
                return 1;
            }
        } else {
            return color.getShortcut().compareTo(card.getColorShortcut());
        }
    }
}
