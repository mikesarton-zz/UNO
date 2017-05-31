package esi.atl.g41864.uno.view;

import esi.atl.g41864.uno.model.UnoException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author mike
 */
public class InitPlayersGUI {

    private final List<Button> buttonsIA;
    private final List<Button> buttonsClean;
    private final List<TextField> names;
    private List<String> listPlayers;
    private Button butStart;
    private final VBox root;
    private final Scene scene;
    private final GridPane gridpane;
    private final MenuBar menu;

    /**
     * This is the constructor of this scene.
     * It receives the stage on which we put the scene, has an VBox on which
     * we put all the children and calls the methods necessary to create
     * the scene.
     */
    public InitPlayersGUI() {
        buttonsIA = new ArrayList<>();
        buttonsClean = new ArrayList<>();
        names = new ArrayList<>();
        listPlayers = new ArrayList();
        gridpane = creationGrid();
        menu = creationMenuBar();
        root = new VBox();
        
        root.getChildren().addAll(menu, gridpane);
        root.setId("background");
        root.getStylesheets().add(getClass().getResource("/esi/atl/g41864/uno/"
                + "resources/stylesheets/"
                + "styleInitPlayers.css").toExternalForm());
        
        scene = new Scene(root, 400, 580);
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public List<String> getNames() {
        return listPlayers;
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

    private GridPane creationGrid() {

        GridPane gp = new GridPane();
        GridPane.setMargin(gp, new Insets(10, 10, 100, 10));
        creationLabel("Noms des joueurs", gp, 0, 0);

        for (int i = 1; i <= 10; ++i) {
            names.add(creationTextField("Nom du joueur " + i, gp, 0, i));

            final int i_final = i;

            buttonsIA.add(creationButton("IA", gp, 1, i));
            buttonsIA.get(i - 1).setOnAction((ActionEvent t) -> {
                names.get(i_final - 1).setText("IA" + i_final);
            });

            buttonsClean.add(creationButton("Clean", gp, 2, i));
            buttonsClean.get(i - 1).setOnAction((ActionEvent t) -> {
                names.get(i_final - 1).clear();
            });
        }

        butStart = creationButton("START", gp, 3, 11);
        butStart.setOnAction((ActionEvent t) -> {
            try {
                listPlayers = createListNames();
                scene.getWindow().hide();
            } catch (UnoException e) {
                Alert alert = new Alert(AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
        });

        return gp;
    }

    private List<String> createListNames() throws UnoException {

        List<String> list = new ArrayList<>();

        for (int i = 0; i < names.size(); ++i) {
            String name = names.get(i).getText();
            if (!name.equalsIgnoreCase("")) {
                list.add(name);
            }
        }

        if (list.size() < 2) {
            throw new UnoException("Il faut minimum 2 joueurs");
        }

        return list;
    }
}
