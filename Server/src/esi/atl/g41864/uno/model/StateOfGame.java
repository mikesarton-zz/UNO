package esi.atl.g41864.uno.model;

/**
 * This enumeration is used to indicate if the game is running or if he's stopped.
 * According to the game's state, some methods are not available.
 * @author mike
 */
enum StateOfGame {
    RUNNING ("R"),
    STOPPED ("S");
    
    private final String shortcut;
    
    StateOfGame(String shortcut){
        this.shortcut = shortcut;
    }
    
    String getShorcut(){
        return shortcut;
    }
}
