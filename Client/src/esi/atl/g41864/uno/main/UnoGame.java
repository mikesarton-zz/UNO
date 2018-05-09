package esi.atl.g41864.uno.main;

import esi.atl.g41864.uno.net.IPAddressValidator;
import esi.atl.g41864.uno.view.ConnexionGUI;
import esi.atl.g41864.uno.view.UnoGUI;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author G41864
 */
public class UnoGame extends Application{
    private String playerName;
    private static String hostname;
    private static int port;
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        switch (args.length) {
            case 2:
                IPAddressValidator ipValidator = new IPAddressValidator();
                if (!ipValidator.validate(args[0])) {
                    System.out.println("Erreur: Cette adresse IP n'est pas valide.");
                    System.exit(1);
                }   
                hostname = args[0];
                try {
                    port = Integer.valueOf(args[1]);
                    if (port < 1024) {System.out.println("Le port doit être >= "
                            + "1024"); System.exit(1);}
                } catch (NumberFormatException nfe) {
                    System.out.println("Le port doit être un entier.");
                    System.exit(1);
                }
                break;
            case 1:
                System.out.println("Erreur: Si vous entrez une IP, entrez au moins"
                        + " un port...");
                System.exit(1);
                break;
            default:
                hostname = "localhost";
                port = 55678;
                break;
        }
        
        
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("UNO");
        primaryStage.setResizable(false);
        primaryStage.impl_setPrimary(false);
        
        ConnexionGUI sc1 = new ConnexionGUI();
        handleWindows1(sc1, primaryStage);
        
        UnoGUI uno = new UnoGUI(playerName, hostname, port);
        handleWindows2(uno, primaryStage);
    }
    
    /**
     * This method shows the pseudo window.
     * @param sc The scene to show
     * @param stage The stage on which put the scene
     */
    public void handleWindows1(ConnexionGUI sc, Stage stage){
        stage.setScene(sc.getScene());
        stage.showAndWait();
        playerName = sc.getName();        
    }
    
    /**
     * This method shows the game's view.
     * @param uno The game's view
     * @param stage The stage on which put the view
     */
    public void handleWindows2(UnoGUI uno, Stage stage) {
        stage.setScene(uno.getScene());
        stage.show();
    }
}
