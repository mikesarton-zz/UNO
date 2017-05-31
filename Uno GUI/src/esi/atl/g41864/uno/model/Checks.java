package esi.atl.g41864.uno.model;

/**
 *
 * @author mike
 */
public class Checks {

    /**
     * This method checks if the string received in parameter is an
     * positive integer and superior to 0.
     * @param str The string to check.
     * @return The integer value from the parameter.
     * @throws UnoException If the string can't be converted to an integer
     * or if the value is inferior or equal to 0.
     */
    public static int checkInput(String str) throws UnoException {
            int value = 0;
            
            try{
                value = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                throw new UnoException("N'oubliez pas d'entrer un score à "
                        + "atteindre.");
            }
            
            if (value <= 0) {
                throw new UnoException("Le score doit être un entier positif.");
            }
            if(value > 700) {
                throw new UnoException("Le score maximal possible est 700.");
            }
            
            return value;
    }
}
