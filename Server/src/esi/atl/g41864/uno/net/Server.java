
package esi.atl.g41864.uno.net;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is the server. It's waits for clients and sends them in a
 * specific thread.
 * @author mike
 */
class Server {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        if(args.length > 0){
            int port = 55678;
            try {
                port = Integer.valueOf(args[0]);
                if (port < 1024) {System.out.println("Le port doit être > 1024"); 
                System.exit(1);}
            } catch (NumberFormatException nfe) {
                System.out.println("Le port doit être un entier.");
                System.exit(1);
            }
            Server server = new Server(port);
            server.listening();
        } else {
            Server server = new Server(55678);
            server.listening();
        }
    }
    
    private final int port;
    private final File log;
    private final List<Socket> clients;
    
    /**
     * The constructor for the server.
     * @param port The port on which the server waits for clients.
     */
    Server(int port) {
        clients = new ArrayList<>();
        this.port = port;
        log = new File("logServeurUno.txt");        
    }
    
    /**
     * This methods listens to the port and waits for clients. As soon as
     * a client arrives, it sends the client in a specific thread.
     */
    private void listening() {
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while(true){
                System.out.println("Serveur UNO disponible sur le port: " + port);
                Socket client = serverSocket.accept();
                addConnexion(client);
                try {
                    new ThreadClient(client, this).start();
                } catch (IllegalArgumentException iae) {
                    System.out.println(iae.getMessage());
                    clients.remove(clients.size()-1);
                }
            }
        } catch (IOException ioe) {
                System.out.println("Serveur UNO: Impossible d'écouter le "
                        + "port " + port);
        }
    }
    
    /**
     * This methods adds a line in the log file as soon as a client is
     * connected to the server.
     * @param client The new client
     */
    private synchronized void addConnexion(Socket client) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(log, true), "UTF-8"));){
            
            writer.append(new Date().toString() + ": " + 
                    client.getInetAddress().getHostAddress() + " s'est "
                            + "connecté.\n");
            writer.close();
            clients.add(client);
        } catch (IOException ioe) {
            System.out.println("Serveur UNO: " + ioe.getMessage());
        }
    }
    
    /**
     * This methods adds a line in the log file as soon as a client
     * disconnectes from the server.
     * @param client The leaving client.
     */
    synchronized void addDeconnexion(Socket client) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(log, true), "UTF-8"));){
            
            writer.append(new Date().toString() + ": " + 
                    client.getInetAddress().getHostAddress() + " s'est "
                            + "déconnecté.\n");
            writer.close();
            
            boolean found = false;
            int i = 0;
            while (!found && i<clients.size()) {
                if(clients.get(i).hashCode() == client.hashCode()) {
                    clients.remove(i);
                    found = true;
                }
                ++i;
            }
        } catch (IOException ioe) {
            System.out.println("Serveur UNO: " + ioe.getMessage());
        }
    }
    
    synchronized void addMessage(Socket client, String msg) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(log, true), "UTF-8"));){
            
            writer.append(new Date().toString() + ": " + 
                    client.getInetAddress().getHostAddress() + " -> " + msg +"\n");
            writer.close();
        } catch (IOException ioe) {
            System.out.println("Serveur UNO: " + ioe.getMessage());
        }
    }
}
