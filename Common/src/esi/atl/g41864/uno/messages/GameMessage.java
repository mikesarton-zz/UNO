
package esi.atl.g41864.uno.messages;

import esi.atl.g41864.uno.objects.Card;
import java.io.Serializable;

/**
 * This class is used as protocol to send messages between the server and
 * the client.
 * @author mike
 */
public class GameMessage implements Serializable {
    private final MessagesTypes message;
    private Card card = null;
    private String str = null;
    private int value;
    
    /**
     * The constructor 1
     * @param msg The message type
     */
    public GameMessage(MessagesTypes msg) {
        message = msg;
    }
    
    /**
     * The constructor 2
     * @param msg The message type
     * @param card The card to play
     */
    public GameMessage(MessagesTypes msg, Card card) {
        this(msg);
        this.card = card;
    }
    
    /**
     * The constructor 3
     * @param msg The message type
     * @param string The string to send
     */
    public GameMessage(MessagesTypes msg, String string) {
        this(msg);
        this.str = string;
    }
    
    /**
     * The constructor 4
     * @param msg The message type
     * @param value The value to send
     */
    public GameMessage(MessagesTypes msg, int value) {
        this(msg);
        this.value = value;
    }
    
    /**
     *
     * @return The message type
     */
    public MessagesTypes getMessageType() {
        return message;
    }
    
    /**
     *
     * @return The card to play
     */
    public Card getCard() {
        return card;
    }
    
    /**
     *
     * @return The message
     */
    public String getStr() {
        return str;
    }
    
    /**
     *
     * @return The value
     */
    public int getValue() {
        return value;
    }
}
