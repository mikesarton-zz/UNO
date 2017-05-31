package esi.atl.g41864.uno.objects;

import java.io.Serializable;

/**
 * This is an enumeration with all the values a card can contains.
 * @author G41864
 */
public enum Value implements Serializable {
    NUL ("0"),
    ONE ("1"),
    TWO ("2"),
    THREE ("3"),
    FOUR ("4"),
    FIVE ("5"),
    SIX ("6"),
    SEVEN ("7"),
    EIGHT ("8"),
    NINE ("9");
    
    private final String value;
    
    Value(String val){
        value = val;
    }
    
    /**
     * This method returns the value.
     * @return The value.
     */
    public String getValue(){
        return value;
    }
    
    public static Value getEnumValue (String c) {
        switch(c) {
            case "0": return NUL;
            case "1": return ONE;
            case "2": return TWO;
            case "3": return THREE;
            case "4": return FOUR;
            case "5": return FIVE;
            case "6": return SIX;
            case "7": return SEVEN;
            case "8": return EIGHT;
            case "9": return NINE;
            default: return null;
        }
    }
    
    @Override
    public String toString(){
        return value;
    }
    
    boolean equals(Value other){
        return value.equals(other.getValue());
    }
}
