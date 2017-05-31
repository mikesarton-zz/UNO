package esi.atl.g41864.uno.view;

import esi.atl.g41864.uno.model.Checks;
import esi.atl.g41864.uno.model.UnoException;
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
    
    ScoreGUI(Stage parent, int currentScore) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent);
        
        gp = creationGrid(currentScore);
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
    
    private GridPane creationGrid(int score) {
        GridPane root = new GridPane();
       
        Label lbl1 = creationLabel("Le score actuel à atteindre est : " + score,
                root, 0, 0);
        
        lbl1.setId("label");

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
        String str = tf1.getText();
        try {
            int score = Checks.checkInput(str);
            newScore = score;
            scene.getWindow().hide();
        } catch (UnoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    private Label creationLabel(String name, GridPane gp, int column, int line) {

        Label lbl = new Label(name);
        lbl.setFont(Font.font("System", 15));
        lbl.setTextFill(Color.BLACK);
        gp.add(lbl, column, line);
        GridPane.setMargin(lbl, new Insets(10, 0, 0, 10));

        return lbl;
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
}
