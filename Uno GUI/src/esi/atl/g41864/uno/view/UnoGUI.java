package esi.atl.g41864.uno.view;

import esi.atl.g41864.uno.observers.Observer;
import esi.atl.g41864.uno.model.Card;
import esi.atl.g41864.uno.model.Player;
import esi.atl.g41864.uno.model.Uno;
import esi.atl.g41864.uno.model.UnoException;
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
import javafx.scene.control.ToggleButton;
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

    private final MyMediaPlayer mediaPlayer;
    private ToggleButton muteMusicButton;
    private Button increaseVolumeButton;
    private Button decreaseVolumeButton;
    private final Uno uno;
    private final VBox root;
    private final BorderPane bPInfos;
    private final ScrollPane sPHandPlayer;
    private final BorderPane bPButtons;
    private HandView handView;
    private Button butPlay;
    private Button butPick;
    private final Scene scene;
    private final MenuBar menu;

    public UnoGUI(List<String> listNames) throws UnoException, 
            MalformedURLException {
        
        root = new VBox();
        bPInfos = new BorderPane();
        sPHandPlayer = new ScrollPane();
        bPButtons = new BorderPane();
        menu = creationMenuBar();
        
        uno = new Uno(listNames);
        uno.addObserver(this);
        uno.start();
        
        mediaPlayer = new MyMediaPlayer();
        mediaPlayer.addObserver(this);

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
        
        setButtons(mediaPlayer.getVolume());
        
        scene = new Scene(root, 1000, 700);
    }
    
    public Scene getScene() {
        return scene;
    }

    private void setInfosLine(List<Player> players, Card topCard, 
                                    Player currentPlayer, boolean isDeckEmpty) {

        HBox hBoxLeft = new HBox();
        
        Label score = new Label("Score à atteindre : " + 
                                        String.valueOf(uno.getScoreToReach()));
        score.setId("score");
        
        hBoxLeft.getChildren().addAll(infoBox(players, currentPlayer), score);
        hBoxLeft.setSpacing(50);
        hBoxLeft.setPadding(new Insets(0, -200, 0, 0));
        
        if(bPInfos.getRight() == null) {
            bPInfos.setRight(new CardView("back").getCardView());
        }
        
        if(!isDeckEmpty){
            bPInfos.getRight().setOpacity(1);
        } else {
            bPInfos.getRight().setOpacity(0.4);
        }

        bPInfos.setLeft(hBoxLeft);
        bPInfos.setCenter(new CardView(topCard.getColor(), 
                topCard.getValue()).getCardView());
        bPInfos.setPadding(new Insets(-90, 0, 0, 0));
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

    private void setButtons(double volume) {
        
        muteMusicButton = new ToggleButton();
        muteMusicButton.setPadding(new Insets(0, 0, 0, 35));
        muteMusicButton.getStyleClass().add("toggleButton");
        muteMusicButton.setOnAction(this::handleMusic);
        
        increaseVolumeButton = new Button();
        increaseVolumeButton.getStyleClass().add("plusButton");
        increaseVolumeButton.setOnAction(this::handleVolumePlus);
        
        decreaseVolumeButton = new Button();
        decreaseVolumeButton.getStyleClass().add("lessButton");
        decreaseVolumeButton.setOnAction(this::handleVolumeLess);
        
        HBox playBox = new HBox();
        HBox volumeBox = new HBox();
        VBox musicBox = new VBox();
        
        butPlay = creationButton("Play", 130);
        butPlay.setOnAction(this::handlePlay);

        butPick = creationButton("Pick", 130);
        butPick.setOnAction(this::handlePick);
        
        playBox.getChildren().addAll(butPick, butPlay);
        playBox.setSpacing(10);
        
        volumeBox.getChildren().addAll(decreaseVolumeButton, increaseVolumeButton);
        
        Label vol = new Label(String.format("%.0f", volume*100) + "%");
        vol.setPadding(new Insets(0, 0, 0, 35));
        vol.setId("volumeState");
        
        musicBox.getChildren().addAll(muteMusicButton, volumeBox, vol);
        
        bPButtons.setCenter(playBox);
        bPButtons.setRight(musicBox);
        bPButtons.setPadding(new Insets(0, 0, 0, 410));
    }

    private void handleMusic(ActionEvent event) {
        mediaPlayer.muteMusic();
    }
    
    private void handleVolumePlus(ActionEvent event) {
        mediaPlayer.increaseVolume();
    }
    
    private void handleVolumeLess(ActionEvent event) {
        mediaPlayer.decreaseVolume();
    }
    
    private void handlePlay(ActionEvent event) {
        try {
            Card card = handView.getSelectedCard();
            uno.playCard(card);
        } catch (UnoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    private void handlePick(ActionEvent event) {
        try {
            uno.pickCard();
        } catch (UnoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    private void handleEndGame() throws UnoException {
        
        ButtonType butYes = new ButtonType("Rejouer");
        ButtonType butNo = new ButtonType("Arrêter");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Jeu terminé");
        alert.setHeaderText("Le score a été atteint.");
        alert.setContentText("Voulez-vous rejouer?");

        alert.getButtonTypes().setAll(butYes, butNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == butYes) {
            uno.restart();
        } else if (result.get() == butNo) {
            scene.getWindow().hide();
        }
    }

    private MenuBar creationMenuBar() {

        MenuBar menuBar = new MenuBar();
        Menu menuGame = new Menu("Uno");

        MenuItem itemRestart = new MenuItem("Redémarrer");
        itemRestart.setOnAction((ActionEvent t) -> {
            try {
                uno.restart();
            } catch (UnoException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
        });
        
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      