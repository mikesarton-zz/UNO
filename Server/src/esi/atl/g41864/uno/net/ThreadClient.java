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
            List<String> lIAHand = new ArrayList<>();
            for(Object obj : playerHand) {
                JSONObject card = (JSONObject) obj;
                lplayerHand.add((String) card.get("c"));
            }
            for(Object obj : IAHand) {
                JSONObject card = (JSONObject) obj;
                lIAHand.add((String) card.get("c"));
            }
            uno.resetGame(playerScore, currentPlayerName, iaScore, scoreToReach,
                    topCard, lplayerHand, lIAHand);
        } catch (UnoException ue) {
            System.out.println("ThreadClient: Reconnexion: " + ue.getMessage());
            server.addMessage(client, ue.getMessage());
        }
        
    }
    
    private void saveGameState() {
        File file = new File(name + "Game.json");
        
        if(file.exists() && !file.isDirectory()) {
            cleanFile(file);
        }
        
        //  création d'un objet root
        JSONObject root = new JSONObject();
        
        //  ajout du nom du joueur
        root.put("nomJoueur", name);
        
        //  ajout des scores du joueur et de l'IA
        for(Player pl : uno.getPlayers()) {
            root.put(pl.getName() + "Score", pl.getScore());
        }        
        
        //  ajout du score à atteindre
        root.put("scoreToReach", uno.getScoreToReach());
        
        try {
            //  ajout de la top carte
            root.put("topCarte", uno.getFlippedCard().getColorShortcut()
                    + uno.getFlippedCard().getValueInt());

            //  ajout du nom du joueur courant
            root.put("currentPlayer", uno.getCurrentPlayer().getName());
        } catch (UnoException ue) {
            System.out.println("ThreadClient -> tentative de "
                    + "sauvegarder la top carte : "
                    + ue.getMessage());
            server.addMessage(client, ue.getMessage());
        }

        //  ajout des objets mains
        for(Player pl : uno.getPlayers()) {
            JSONArray main = new JSONArray();
            int i=1;
            for(Card c : pl.getCards()) {
                JSONObject carte = new JSONObject();
                carte.put("c", c.getColorShortcut() + c.getValueInt());
                main.add(carte);
                ++i;
            }
            root.put(pl.getName() + "Main", main);
        }
        
        // écriture du contenu dans un fichier json
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print(root.toJSONString());
            writer.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("ThreadClient -> tentative d'écrire la sauvegarde "
                    + "dans un fichier json: " + fnfe.getMessage());
            server.addMessage(client, fnfe.getMessage());
        }
    }
    
    private void cleanFile(File file) {
        try {
            PrintWriter w = new PrintWriter(file);
            w.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("ThreadClient: " + fnfe.getMessage());
            server.addMessage(client, fnfe.getMessage());
        }
    }
    
    private void deleteSaveFile() {
        try {            
            Files.delete(Paths.get(name + "Game.json"));
        } catch (IOException ioe) {
            System.out.println("ThreadClient: impossible de supprimer le fichier "
                    + "de sauvegarde : " + ioe.getMessage());
            server.addMessage(client, ioe.getMessage());
        }
    }

    @Override
    public void update() {
        sendClientGameState();
    }

}
