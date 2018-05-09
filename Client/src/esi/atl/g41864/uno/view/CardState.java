
package esi.atl.g41864.uno.view;

/**
 *
 * @author mike
 */
enum CardState {
    SELECTED ("Sélectionné"),
    UNSELECTED ("Déselectionné");
    
    private final String state;
    
    CardState(String str){
        state = str;
    }
    
    String getState(){
        return state;
    }
}
