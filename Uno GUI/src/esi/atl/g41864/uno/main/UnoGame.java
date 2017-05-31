package esi.atl.g41864.uno.main;

import esi.atl.g41864.uno.view.InitPlayersGUI;
import esi.atl.g41864.uno.view.UnoGUI;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author G41864
 */
public class UnoGame extends Application{
    private List<String> playersNames = new ArrayList<>();
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("UNO");
        primaryStage.setResizable(false);
        primaryStage.impl_setPrimary(false);
        
        InitPlayersGUI sc1 = new InitPlayersGUI();
        handleWindows1(sc1, primaryStage);
        
        UnoGUI uno = new UnoGUI(playersNames);
        handleWindows2(uno, primaryStage);
    }
    
    public void handleWindows1(InitPlayersGUI init, Stage stage){
        stage.setScene(init.getScene());
        stage.showAndWait();
        
        playersNames = init.getNames();
    }
    
    public void handleWindows2(UnoGUI uno, Stage stage) {
        stage.setScene(uno.getScene());
        stage.show();
    }
}
