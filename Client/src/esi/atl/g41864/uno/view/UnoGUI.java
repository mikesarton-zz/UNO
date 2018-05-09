package esi.atl.g41864.uno.view;

import esi.atl.g41864.uno.net.UnoClient;
import esi.atl.g41864.uno.messages.GameMessage;
import esi.atl.g41864.uno.messages.MessagesTypes;
import esi.atl.g41864.uno.objects.Card;
import esi.atl.g41864.uno.objects.Player;
import esi.atl.g41864.uno.observers.Observer;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author mike
 */
public class UnoGUI implements Observer {

    private final UnoClient server;
    private final VBox root;
    private final BorderPane bPInfos;
    private final ScrollPane sPHandPlayer;
    private final BorderPane bPButtons;
    private HandView handView;
    private Button butPlay;
    private Button butPick;
    private final Scene scene;
    private final MenuBar menu;

    public UnoGUI(String playerName, String host, int port) throws MalformedURLException {
        
        root = new VBox();
        bPInfos = new BorderPane();
        sPHandPlayer = new ScrollPane();
        bPButtons = new BorderPane();
        menu = creationMenuBar();
        
        server = new UnoClient(host, port, playerName, this);
        if(server.isConnected()){
            server.addObserver(this);
            server.start();
            server.sendServerMessage(new GameMessage(MessagesTypes.CLI_CONNECT, playerName));
        } else {
            System.exit(-1);
        }

        sPHandPlayer.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sPHandPlayer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sPHandPlayer.setPadding(new Insets(-100, 0, 0, 0));
        sPHandPlayer.getStyleClass().add("scroll-pane");

        root.setId("background");
        root.setSpacing(100);
        root.getChildren().addAll(menu, bPInfos, sPHandPlayer, 
                bPButtons);
        root.getStylesheets().add(getClass().getResource("/esi/atl/g41864/uno/"
                + "resources/stylesheets/"
                + "styleUno.css").toExternalForm());
        
        setButtons();
        
        scene = new Scene(root, 1000, 700);
    }
    
    public Scene getScene() {
        return scene;
    }

    private void setInfosLine(List<Player> players, Card topCard, 
                  Player currentPlayer, boolean isDeckEmpty, int scoreToReach) {

        HBox hBoxLeft = new HBox();
        
        Label score = new Label("Score à atteindre : " + 
                                        String.valueOf(scoreToReach));
        score.setId("score");
        
        hBoxLeft.getChildren().addAll(infoBox(players, currentPlayer), score);
        hBoxLeft.setSpacing(50);
        hBoxLeft.setPadding(new Insets(0, -200, 0, 0));

        bPInfos.setLeft(hBoxLeft);
        bPInfos.setCenter(new CardView(topCard.getColor(), 
                topCard.getValue()).getCardView());
        bPInfos.setRight(new CardView("back").getCardView());
        bPInfos.setPadding(new Insets(-90, 0, 0, 0));
        
        if (!isDeckEmpty) {
            bPInfos.getRight().setOpacity(1);
        } else {
            bPInfos.getRight().setOpacity(0.4);
        }
    }

    private void setHandPlayer(List<Card> hand) {

        handView = new HandView(hand);
        List<CardView> list = handView.getHandView();
        HBox root = new HBox();

        for (CardView img : list) {
            img.getCardView().setOnMouseClicked((MouseEvent event) -> {
                handView.setCardsDown();
                img.getCardView().setTranslateY(-20);
                img.setState(CardState.SELECTED);
            });
            root.getChildren().add(img.getCardView());
            root.setPadding(new Insets(20, 0, 0, 0));
        }

        if ((list.size() * -7) >= -82) {
            root.setSpacing((list.size() * -7));
        } else {
            root.setSpacing(-77);
        }
        sPHandPlayer.setContent(root);
    }

    private void setButtons() {
        
        HBox playBox = new HBox();
        
        butPlay = creationButton("Play", 130);
        butPlay.setOnAction(this::handlePlay);

        butPick = creationButton("Pick", 130);
        butPick.setOnAction(this::handlePick);
        
        playBox.getChildren().addAll(butPick, butPlay);
        playBox.setSpacing(10);               
        
        bPButtons.setCenter(playBox);
        bPButtons.setPadding(new Insets(0, 0, 0, 410));
    }
    
    private void handlePlay(ActionEvent event) {
        try {
            Card card = handView.getSelectedCard();
            server.sendServerMessage(new GameMessage(MessagesTypes.CLI_PLAYCARD, card));
        } catch (IllegalArgumentException e) {
            printException(e.getMessage());
        }
    }

    private void handlePick(ActionEvent event) {
        server.sendServerMessage(new GameMessage(MessagesTypes.CLI_PICKCARD));
    }

    public void handleEndGame() {
        
        ButtonType butYes = new ButtonType("Rejouer");
        ButtonType butNo = new ButtonType("Arrêter");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Jeu terminé");
        alert.setHeaderText("Le score a été atteint.");
        alert.setContentText("Voulez-vous rejouer?");

        alert.getButtonTypes().setAll(butYes, butNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == butYes) {
            server.sendServerMessage(new GameMessage(MessagesTypes.CLI_RESTART));
        } else if (result.get() == butNo) {
            server.sendServerMessage(new GameMessage(MessagesTypes.CLI_DISCONNECT));
            server.deconnexion();
            scene.getWindow().hide();
        }
    }

    private MenuBar creationMenuBar() {

        MenuBar menuBar = new MenuBar();
        Menu menuGame = new Menu("Uno");

        MenuItem itemRestart = new MenuItem("Redémarrer");
        itemRestart.setOnAction((ActionEvent t) -> {
            server.sendServerMessage(new GameMessage(MessagesTypes.CLI_RESTART));
        });
        
        MenuItem itemExit = new MenuItem("Quitter");
        itemExit.setOnAction((ActionEvent t) -> {
            server.sendServerMessage(new GameMessage(MessagesTypes.CLI_DISCONNECT));
            server.deconnexion();
            scene.getWindow().hide();
        });

        MenuItem itemScore = new MenuItem("Changer score");
        itemScore.setOnAction((ActionEvent t) -> {
            ScoreGUI scoreGUI = new ScoreGUI((Stage) scene.getWindow());
            int score = scoreGUI.getScore();
            if (score != 0) {
                server.sendServerMessage(new GameMessage(MessagesTypes.CLI_CHANGESCORE, score));
            }
        });

        menuGame.getItems().addAll(itemExit, itemRestart, itemScore);
        menuBar.getMenus().add(menuGame);

        return menuBar;
    }

    private ScrollPane infoBox(List<Player> players, Player currentPlayer) {

        ScrollPane sp = new ScrollPane();
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.getStyleClass().add("scroll-pane");

        GridPane gp = new GridPane();

        Label lblTitleName = creationLabel("Nom", gp, 0, 0);
        Label lblTitleScore = creationLabel("Score", gp, 1, 0);
        lblTitleName.setUnderline(true);
        lblTitleScore.setUnderline(true);

        for (int i = 0; i < players.size(); i+=2) {
            Label lblName = creationLabel(players.get(i).getName(),
                    gp, 0, i + 1);
            creationLabel(String.valueOf(players.get(i).getScore()),
                    gp, 1, i + 1);
            if (players.get(i).hashCode() == currentPlayer.hashCode()) {
                lblName.setUnderline(true);
            }
            if (players.get(i).getCards().size() == 1) {
                creationLabel("UNO", gp, 2, i + 1);
            }

            if (i + 1 < players.size()) {
                Label lblName2 = creationLabel(players.get(i + 1).getName(), gp, 3, i + 1);
                creationLabel(String.valueOf(players.get(i + 1).getScore()),
                        gp, 4, i + 1);
                if (players.get(i + 1).hashCode() == currentPlayer.hashCode()) {
                    lblName2.setUnderline(true);
                }
                if (players.get(i + 1).getCards().size() == 1) {
                    creationLabel("UNO", gp, 5, i + 1);
                }
            }
        }

        sp.setContent(gp);

        return sp;
    }

    private Label creationLabel(String name, GridPane gp, int column, int line) {

        Label lbl = new Label(name);
        lbl.setFont(Font.font("System", 15));
        lbl.setTextFill(Color.WHITE);
        gp.add(lbl, column, line);
        GridPane.setMargin(lbl, new Insets(10, 0, 0, 10));

        return lbl;
    }

    private Button creationButton(String title, double width) {
        Button button = new Button();
        button.setText(title);
        button.setMaxWidth(width);
        button.getStyleClass().add("buttons");

        return button;
    }
    
    public void printException(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }

    @Override
    public void update() {
        setInfosLine(server.getGameState().getPlayers(),
                server.getGameState().getFlippedCard(),
                server.getGameState().getCurrentPlayer(),
                server.getGameState().isDeckEmpty(),
                server.getGameState().getScoreToReach());

        setHandPlayer(server.getGameState().getCardsCurrentPlayer());
    }
}
