
package esi.atl.g41864.uno.net;

import esi.atl.g41864.uno.messages.GameMessage;
import esi.atl.g41864.uno.messages.GameState;
import esi.atl.g41864.uno.messages.MessagesTypes;
import esi.atl.g41864.uno.observers.Observable;
import esi.atl.g41864.uno.observers.Observer;
import esi.atl.g41864.uno.view.UnoGUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

/**
 * This class is the thread for the client. It listens on the port in order to
 * communicate with the server.
 * @author mike
 */
public class UnoClient extends Thread implements Observable {
    
    private Socket client = null;
    private String host;
    private int port;
    private String playerName;
    private boolean isClosed;
    private boolean isConnected;
    private final UnoGUI unoGui;
    private List<Observer> observers;
    private GameState gs;
    private ObjectOutputStream toClient;
    private ObjectInputStream fromClient;
    
    /**
     * The constructor for the thread.
     * @param host The IP address
     * @param port The socket port
     * @param playerName The name of the player
     * @param unoGui The view of UNO
     */
    public UnoClient (String host, int port, String playerName, UnoGUI unoGui) {
        isClosed = false;
        isConnected = true;
        observers = new ArrayList<>();
        this.unoGui = unoGui;
        this.host = host;
        this.port = port;
        this.playerName = playerName;
        try {
            client = new Socket(host, port);
            toClient = new ObjectOutputStream(client.getOutputStream());
            fromClient = new ObjectInputStream(client.getInputStream());
        } catch (IOException ioe) {
            System.out.println("ThreadServer: " + ioe.getMessage());
            System.out.println("ThreadServer: aucun serveur disponible.");
            isClosed = true;
        }
    }
    
    @Override
    public void run() {
        while (!isClosed) {
            receiveServerMessage();
        }
    }
    
    private void treatment(GameMessage message) {
        switch (message.getMessageType()) {
            case SRV_EXCEPTION:
                Platform.runLater(() -> {
                    unoGui.printException(message.getStr());
                });
                break;
            case SRV_GAMEOVER:
                Platform.runLater(() -> {
                    unoGui.handleEndGame();
                 });
                break;
            default:
                break;
        }
    }
    
    
    public GameState getGameState() {
        return new GameState(gs);
    }

    private void receiveServerMessage() {
        try {
            Object obj = fromClient.readObject();
            if (obj instanceof GameMessage) {
                GameMessage gm = (GameMessage) obj;
                treatment(gm);
            } else if (obj instanceof GameState) {
                gs = new GameState ((GameState) obj);
                Platform.runLater(() -> {
                    notifyObs();
                });
            } else {
                System.out.println("ThreadServer: Un message a été reçu mais "
                        + "est inconnu.");                
            }
        } catch (IOException | ClassNotFoundException err) {
            if(isConnected) {
                System.out.println("ThreadServer: connexion au serveur perdue.");
                isClosed = true;
                treatment(new GameMessage(MessagesTypes.SRV_EXCEPTION, "Connexion "
                        + "au serveur perdue!"));
                reconnexion();
            }
        }
    }
    
    /**
     * This methods sends a message to the server.
     * @param message The message to send
     */
    public void sendServerMessage(GameMessage message) {
        try {
            if(!isClosed) {
                toClient.writeObject(message);
                toClient.flush();
            }
        } catch (IOException ioe) {
            System.out.println("ThreadServer: " + ioe.getMessage());
        }
    }
    
    /**
     * This methods disconnects properly the client's socket and inform
     * the server of the disconnection.
     */
    public void deconnexion() {
        try {
            toClient.close();
            fromClient.close();
            client.close();
            isClosed = true;
            isConnected = false;
            System.out.println("ThreadServer: déconnexion du client.");
        } catch (IOException ioe) {
            System.out.println("ThreadClient: " + ioe.getMessage());
        }
    }
    
    /**
     * This methods indicates wheter the socket is or isn't connected to a
     * server.
     * @return True if the socket is connected, false otherwise.
     */
    public boolean isConnected() {
        return !isClosed;
    }
 
    private void reconnexion() {
        int i = 1;
        while (isClosed) {
            try {
                toClient.close();
                fromClient.close();
                System.out.println("ThreadServer: tentative de reconnexion " + i + "...");
                client = new Socket(host, port);
                toClient = new ObjectOutputStream(client.getOutputStream());
                fromClient = new ObjectInputStream(client.getInputStream());
                isClosed = false;
                sendServerMessage(new GameMessage(MessagesTypes.CLI_RECONNECT, playerName));
                System.out.println("ThreadServer: reconnexion réussie.");
            } catch (IOException ioe) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    ++i;
                } catch (InterruptedException ie) {
                    System.out.println("ThreadServer: attente de reconnexion "
                            + "interrompue.");
                }
            }
        }
    }

    @Override
    public void addObserver(Observer obs) {
        if (!observers.contains(obs)) {
            observers.add(obs);
        }
    }

    @Override
    public void deleteObserver(Observer obs) {
        if (observers.contains(obs)) {
            observers.remove(obs);
        }
    }

    @Override
    public void notifyObs() {
        observers.forEach((obs) -> {
            obs.update();
        });
    }
}
