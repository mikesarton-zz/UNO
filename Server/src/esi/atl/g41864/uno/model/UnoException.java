package esi.atl.g41864.uno.model;

import java.io.Serializable;

/**
 * This is a personnalized exception for this game.
 * @author mike
 */
public class UnoException extends Exception implements Serializable {
    /**
     * This is the constructor for the personnalized exception.
     * It receives a string as parameter and sends it to the upper class.
     * @param msg 
     */
    public UnoException(String msg){
        super(msg);
    }
}
