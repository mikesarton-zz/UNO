package esi.atl.g41864.uno.net;

import esi.atl.g41864.uno.messages.GameMessage;
import esi.atl.g41864.uno.messages.GameState;
import esi.atl.g41864.uno.messages.MessagesTypes;
import esi.atl.g41864.uno.objects.Card;
import esi.atl.g41864.uno.objects.Player;
import esi.atl.g41864.uno.model.Uno;
import esi.atl.g41864.uno.model.UnoException;
import esi.atl.g41864.uno.observers.Observer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

/**
 * This class is a specific thread for each client connecte to the server.
 * @author mike
 */
class ThreadClient extends Thread implements Observer {

    private Server server;
    private final Socket client;
    private boolean isClosed;
    private String name;
    private Uno uno;
    private ObjectOutputStream toClient;
    private ObjectInputStream fromClient;
    
    ThreadClient(Socket socket, Server server) {
        if ((server == null) || (socket == null)) {
            throw new IllegalArgumentException("ThreadClient: arguments null "
                    + "pour faire un ThreadClient");
        }
        client = socket;
        isClosed = false;
        this.server = server;
        try {
            toClient = new ObjectOutputStream(client.getOutputStream());
            fromClient = new ObjectInputStream(client.getInputStream());
        } catch (IOException ioe) {
            System.out.println("ThreadClient: " + ioe.getMessage());
        }
    }
    
    @Override
    public void run() {
        while (!isClosed) {
            GameMessage message = receiveClientMessage();

            switch (message.getMessageType()) {
                case CLI_CONNECT:
                    name = message.getStr();
                    connexion();
                    break;
                case CLI_DISCONNECT:
                    deconnexion();
                    isClosed = true;
                    break;
                case CLI_PLAYCARD:
                    try {uno.playCard(message.getCard());} 
                    catch (UnoException ue) {
                        sendClientErrorMessage(ue.getMessage());}
                    break;
                case CLI_PICKCARD:
                    try {uno.pickCard();} catch (UnoException ue) {
                        sendClientErrorMessage(ue.getMessage());}
                    break;
                case CLI_RESTART:
                    try {uno.restart();} 
                    catch (UnoException ue) {
                        sendClientErrorMessage(ue.getMessage());}
                    break;
                case CLI_CHANGESCORE:
                    try {uno.setScoreToReach(message.getValue());} 
                    catch (UnoException ue) {
                        sendClientErrorMessage(ue.getMessage());}
                    break;
                case CLI_RECONNECT:
                    name = message.getStr();
                    reconnexion();
                    break;
                default:
                    sendClientGameState();
                    break;
            }
        }
    }

    private GameMessage receiveClientMessage() {
        try {
            Object obj = fromClient.readObject();
            GameMessage message = (GameMessage) obj;
            return message;
        } catch (IOException | ClassNotFoundException err) {
            System.out.println("ThreadClient: " + err.getMessage());
            server.addMessage(client, err.getMessage());
            return null;
        }
    }
    
    private void sendClientMessage(GameMessage message) {
        try {
            toClient.writeObject(message);
            toClient.flush();
        } catch (IOException ioe) {
            System.out.println("ThreadClient: " + ioe.getMessage());
            server.addMessage(client, ioe.getMessage());
        }
    }
    
    private void sendClientErrorMessage(String err) {
        try {
            GameMessage msg = new GameMessage(MessagesTypes.SRV_EXCEPTION, err);
            toClient.writeObject(msg);
            toClient.flush();
        } catch (IOException ioe) {
            System.out.println("ThreadClient: " + ioe.getMessage());
            server.addMessage(client, ioe.getMessage());
        }
    }
    
    private void sendClientGameState() {
        try {
            if(uno.getIsOver()) {
                sendClientMessage(new GameMessage(MessagesTypes.SRV_GAMEOVER));
            } else {
                GameState gameState = new GameState(uno.getIsOver(), 
                    uno.isDeckEmpty(), uno.getPlayers(), 
                    uno.getCurrentPlayer().getCards(), uno.getFlippedCard(), 
                    uno.getCurrentPlayer(), uno.getScoreToReach());
            toClient.writeObject(gameState);
            toClient.flush();
            toClient.reset();
            saveGameState();
            }            
        } catch (IOException ioe) {
            System.out.println("ThreadClient: " + ioe.getMessage());
            server.addMessage(client, ioe.getMessage());
        } catch (UnoException ue) {
            sendClientMessage(new GameMessage(MessagesTypes.SRV_EXCEPTION, 
                    ue.getMessage()));
            server.addMessage(client, ue.getMessage());
        }
    }
    
    private void connexion() {
        System.out.println("ThreadClient: Joueur " + name + ": connecté au serveur.");
        try {
            uno = new Uno(name);
            uno.addObserver(this);
            uno.start();
            saveGameState();
        } catch (UnoException ue) {
            sendClientMessage(new GameMessage(MessagesTypes.SRV_EXCEPTION, 
                    ue.getMessage()));
            server.addMessage(client, ue.getMessage());
        }
    }
    
    private void deconnexion() {
        System.out.println("ThreadClient: Joueur " + name + ": déconnecté "
                + "du serveur.");
        try {
            server.addDeconnexion(client);
            toClient.close();
            fromClient.close();
            client.close();
            deleteSaveFile();
        } catch (IOException ioe) {
            System.out.println("ThreadClient: " + ioe.getMessage());
            server.addMessage(client, ioe.getMessage());
        }
    }
    
    private void reconnexion() {
        JSONObject root;
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(name + "Game.json"));
            root = (JSONObject) obj;
        } catch (IOException | ParseException err) {
            //  si impossible de retrouver le fichier de sauvegarde
            sendClientMessage(new GameMessage(MessagesTypes.SRV_RESPONSETORECONNECT,
                    "Impossible de retrouver les données de votre partie."));
            server.addMessage(client, "Impossible de retrouver les données de "
                    + "votre partie.");
            return;
        }
        
        System.out.println("ThreadClient: Joueur " + name + " s'est reconnecté "
                + "au serveur.");
        server.addMessage(client, "ThreadClient: Joueur " + name + " s'est "
                + "reconnecté au serveur.");
        
        try {
            uno = new Uno(name);
            uno.addObserver(this);
            int playerScore = (int) (long) root.get(name + "Score");
            int iaScore = (int) (long) root.get("IAScore");
            int scoreToReach = (int) (long) root.get("scoreToReach");
            String topCard = (String) root.get("topCarte");
            String currentPlayerName = (String) root.get("currentPlayer");
            
            JSONArray playerHand = (JSONArray) root.get(name + "Main");
            JSONArray IAHand = (JSONArray) root.get("IAMain");
            List<String> lplayerHand = new ArrayList<>();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     