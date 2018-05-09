package esi.atl.g41864.uno.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author mike
 */
class ScoreGUI {
    private final Stage stage;
    private final Scene scene;
    private final VBox vbox;
    private final GridPane gp;
    private TextField tf1;
    private int newScore;
    
    ScoreGUI(Stage parent) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent);
        
        gp = creationGrid();
        vbox = new VBox();
        
        vbox.getChildren().add(gp);
        vbox.setId("background");
        vbox.getStylesheets().add(getClass().getResource("/esi/atl/g41864/uno/"
                + "resources/stylesheets/"
                + "styleScoreGUI.css").toExternalForm());
        
        scene = new Scene(vbox, 350, 100);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    private GridPane creationGrid() {
        GridPane root = new GridPane();

        EventHandler FilterScore = (EventHandler<KeyEvent>) (KeyEvent event) -> {
            try {
                Integer.parseInt(event.getCharacter());
            } catch (NumberFormatException e) {
                event.consume();
            }
        };
        
        tf1 = creationTextField("Entrez le nouveau score à atteindre",
                root, 0, 1);
        tf1.addEventFilter(KeyEvent.KEY_TYPED, FilterScore);
        
        Button validation = creationButton("Valider", root, 1, 1);
        validation.setOnAction(this::handleScore);

        return root;
    }
    
    int getScore() {
        return newScore;
    }
    
    private void handleScore(ActionEvent event) {
        try {
            String str = tf1.getText();
            int score = checkInput(str);
            newScore = score;
            scene.getWindow().hide();
        } catch (IllegalArgumentException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    private TextField creationTextField(String text, GridPane gp, int column,
            int line) {

        TextField txtField = new TextField();
        txtField.setPromptText(text);
        gp.add(txtField, column, line);
        GridPane.setMargin(txtField, new Insets(10, 0, 10, 10));

        return txtField;
    }

    private Button creationButton(String title, GridPane gp, int column,
            int line) {

        Button button = new Button();
        button.setText(title);
        button.getStyleClass().add("buttons");
        gp.add(button, column, line);
        GridPane.setMargin(button, new Insets(0, 10, 0, 10));

        return button;
    }
    
    static int checkInput(String str) throws IllegalArgumentException {
            int value = 0;
            
            try{
                value = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("N'oubliez pas d'entrer un score à "
                        + "atteindre.");
            }
            
            if (value <= 0) {
                throw new IllegalArgumentException("Le score doit être un entier positif.");
            }
            if(value > 700) {
                throw new IllegalArgumentException("Le score maximal possible est 700.");
            }
            
            return value;
    }
}
