package network.server;

import controller.game.Game;
import model.objects.Marble;
import model.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MergeServer {
    public static final int PORT = 69;
    private Map<Player, Boolean> players = new HashMap<>();
    private int maxPlayers;
    private Game game;
    private List<ConnectionHandler> connections;

    public MergeServer() {

    }

    /**
     * Getter for players.
     * @return a map of players and their ready values.
     */
    public Map<Player, Boolean> getPlayers() {
        return players;
    }

    /**
     * Getter for the set of players.
     * @return the set of players
     */
    public Set<Player> getPlayerSet() {
        return players.keySet();
    }

    /**
     * Adds a new player to the players Map.
     * @param player the player to be added
     */
    public void addPlayer(Player player) {
        this.players.put(player, false);
    }

    /**
     * Getter for the maxPlayers configuration.
     * @return maxPlayers as configured
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Sets a player to be ready.
     * @param player the player to be set ready
     */
    public void setReadyPlayer(Player player) {
        players.put(player, true);
    }

    /**
     * Sets the maxPlayers configuration.
     * @param maxPlayers the new value of the maxPlayers setting.
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * Getter for the game.
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    public void startGame() {
        game = new Game(new ArrayList<>(players.keySet()));
    }

    public static void main(String[] args) throws IOException {
        MergeServer server = new MergeServer();
        server.begin();
    }

    public void begin() throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket s = serverSocket.accept();
                System.out.println("Client connected");
                ConnectionHandler ch = new ConnectionHandler(new BufferedReader(new InputStreamReader(s.getInputStream())),
                        new PrintWriter(s.getOutputStream()), this);
                new Thread(ch).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    }

    /**
     * Sends a message to all connected clients.
     * @param message the message to be broadcast
     */
    public void broadcast(String message) {
        for (ConnectionHandler ch : connections) {
            ch.send(message);
        }
    }
}
