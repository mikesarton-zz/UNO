package esi.atl.g41864.uno.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author mike
 */
public class ConnexionGUI {

    private final BorderPane root;
    private final VBox subroot;
    private final Scene scene;
    private final MenuBar menu;
    private TextField txtField;
    private String name;

    public ConnexionGUI() {
        menu = creationMenuBar();
        subroot = creationVBox();
        
        root = new BorderPane();
        root.setTop(menu);
        root.setCenter(subroot);
        root.setId("background");
        root.getStylesheets().add(getClass().getResource("/esi/atl/g41864/uno/"
                + "resources/stylesheets/"
                + "styleInitPlayers.css").toExternalForm());
        
        scene = new Scene(root, 800, 451);
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public String getName() {
        return name;
    }
    
    //  le corps de la fenêtre
    private VBox creationVBox(){
        VBox root = new VBox();
        HBox hboxTxtFd = new HBox();
        HBox hboxButton = new HBox();
        
        txtField = creationTextField("Votre nom");
        Button butLogon = creationButton("Jouer");
        butLogon.setOnAction(this::checkName);
        
        hboxTxtFd.getChildren().add(txtField);
        hboxTxtFd.setPadding(new Insets(0,0,0,40));
        
        hboxButton.getChildren().add(butLogon);
        hboxButton.setPadding(new Insets(0, 0, 0, 50));
        
        root.getChildren().addAll(hboxTxtFd, hboxButton);
        root.setPadding(new Insets(100, 0, 0, 250));
        root.setSpacing(20);
        return root;
    }   

    private MenuBar creationMenuBar() {

        MenuBar menuBar = new MenuBar();
        Menu menuGame = new Menu("Uno");
        MenuItem itemExit = new MenuItem("Exit");
        itemExit.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });
        menuGame.getItems().add(itemExit);
        menuBar.getMenus().add(menuGame);

        return menuBar;
    }

    private TextField creationTextField(String text) {

        TextField txtField = new TextField();
        txtField.setPromptText(text);
        txtField.setMaxWidth(225);
        txtField.setMinHeight(50);
        txtField.setId("txtField");
        txtField.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(final ObservableValue<? extends String> ov, 
                                final String oldValue, final String newValue) {
            if (txtField.getText().length() > 15) {
                String s = txtField.getText().substring(0, 15);
                txtField.setText(s);
            }
        }
    });
        Platform.runLater(() -> root.requestFocus());
        
        return txtField;
    }

    private Button creationButton(String title) {

        Button button = new Button();
        button.setText(title);
        button.getStyleClass().add("buttons");
        button.setPrefSize(200, 50);

        return button;
    }
    
    //  vérification sur le pseudo entré
    private void checkName(ActionEvent event) throws IllegalArgumentException {
        if(!txtField.getText().equalsIgnoreCase("")){
            name = txtField.getText();
            scene.getWindow().hide();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Vous devez indiquer "
                    + "votre pseudo");
            alert.showAndWait();
        }
    }
}
