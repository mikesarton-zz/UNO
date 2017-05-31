package esi.atl.g41864.uno.objects;

import java.io.Serializable;

/**
 * This enumeration represents all the colors a card can takes.
 * @author G41864
 */
public enum ColorUno implements Serializable {

    /**
     * The red color.
     */
    RED ("Rouge", "r"),

    /**
     * The blue color.
     */
    BLUE ("Bleu", "b"),

    /**
     * The green color.
     */
    GREEN ("Vert", "g"),

    /** The yellow color.
     *
     */
    YELLOW ("Jaune", "y");
    
    private final String name;
    private final String shortcut;
    
    ColorUno(String name, String shortcut){
        this.name = name;
        this.shortcut = shortcut;
    }
    
    /**
     * This method returns the shortcut string of a color.
     * @return The shortcut string of a color.
     */
    public String getShortcut(){
        return shortcut;
    }
    
    String getName(){
        return name;
    }
    
    public static ColorUno getEnumType (String c) {
        switch(c) {
            case "r": return RED;
            case "b": return BLUE;
            case "g": return GREEN;
            case "y": return YELLOW;
            default: return null;
        }
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    boolean equals(ColorUno other){
        return shortcut.equals(other.getShortcut());
    }
}
