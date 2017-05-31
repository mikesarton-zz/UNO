package esi.atl.g41864.uno.model;

/**
 * This is an enumeration with all the values a card can contains.
 * @author G41864
 */
public enum Value {
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
    
    @Override
    public String toString(){
        return value;
    }
    
    boolean equals(Value other){
        return value.equals(other.getValue());
    }
}
