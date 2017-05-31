
package esi.atl.g41864.uno.observers;

/**
 *
 * @author mike
 */
public interface Observable {
    
    /**
     * This method add an observer to the observable's list.
     * @param obs The observer to add.
     */
    public void addObserver(Observer obs);
    
    /**
     * This method delete an observer to the observable's list.
     * @param obs The observer to delete.
     */
    public void deleteObserver(Observer obs);
    
    /**
     * This method calls the udpate() method of each observer of the
     * observable's list.
     * @param isOver A boolean set to true if the game is over, false otherwise.
     */
    public void notifyObs();
}
