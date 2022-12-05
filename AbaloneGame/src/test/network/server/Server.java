package network.server;

import controller.game.Game;
import controller.game.GameRules;
import controller.game.IllegalMoveException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.objects.Marble;
import model.player.Move;
import model.player.Player;
import network.protocol.Protocol;
import network.protocol.ProtocolMessages;

public class Server implements Runnable {
    public static final int PORT = 69;
    private Map<Player, Boolean> players = new HashMap<>();
    public PrintStream out;
    private int maxPlayers;
    private Game game;
    public Move moveToMake;
    private List<ConnectionHandler> connections = new ArrayList<>();

    public Server() {
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
    public synchronized void addPlayer(Player player) {
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

    public synchronized void setMove(Move move) {
        moveToMake = move;
    }

    /**
     * Getter for the game.
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Creates and starts a game.
     */
    public void startGame() {
        game = new Game(new ArrayList<>(players.keySet()));
        Map<Player, ConnectionHandler> clients = new HashMap<>();
        for (ConnectionHandler ch : connections) {
            clients.put(ch.getPlayer(), ch);
            ch.send(ProtocolMessages.NUM_OF_PLAYERS + ProtocolMessages.DELIMITER + game.getPlayers().size()
                    + ProtocolMessages.DELIMITER + game.getPlayers().indexOf(ch.getPlayer()));
        }
        moveToMake = new Move(game.getPlayers().size(), game.getPlayers().get(0), Collections.emptyList(), new int[0]);
        while (!game.finished()) {
            System.out.println("start");
            ConnectionHandler current = clients.get(game.getPlayers().get(game.getTurn()));
            print(game.getBoard().toString());
            Move oldMove;
            synchronized (moveToMake) {
                oldMove = moveToMake;
            }
            current.send(ProtocolMessages.TURN);
            boolean turnOver = false;
            while (true) {
                while (moveToMake == oldMove) {
                    continue;
                }
                try {
                    game.move(moveToMake);
                    current.send(ProtocolMessages.VALID);
                    broadcast(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER
                            + game.getPlayers().indexOf(current.getPlayer()) + ProtocolMessages.DELIMITER
                            + Protocol.encodeMove(moveToMake));
                    break;
                } catch (IllegalMoveException e) {
                    moveToMake = oldMove;
                    current.send(ProtocolMessages.NOT_VALID);
                }
            }
        }
        Marble winner = GameRules.getWinner(game);
        if (winner == null) {
            print("Draw!");
        } else {
            print(String.format("Player %s ⏺\u001b[0m has won!", winner.toString()));
        }
    }

    /**
     * Main method of the Server.
     * @param args any Program arguments
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.out = System.out;
        new Thread(server).start();
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

    /**
     * Handles the disconnection of a player.
     * @param player the player that disconnected
     */
    public void disconnect(Player player) {
        int winner;
        if (game.getPlayers().size() == 4) {
            winner = player.getColor().isFriendly(Marble.WHITE) ? 3 : 1;
        }
        Map<Integer, Player> scores = new HashMap<>();
        int hiScore = -1;
        boolean equal = false;
        for (Player p : game.getPlayers()) {
            if (!p.equals(player)) {
                scores.put(p.getScore(), p);
                equal = hiScore == p.getScore();
                hiScore = Math.max(p.getScore(), hiScore);
            }
        }
        winner = equal ? 0 : game.getPlayers().indexOf(scores.get(hiScore));
        broadcast(ProtocolMessages.DISCONNECT_WINNER + ProtocolMessages.DELIMITER + winner);
    }

    public synchronized void print(String message) {
        System.out.println(message);
    }

    @Override
    public void run() {
        try {
            while (true) {
                try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                    serverSocket.setSoTimeout(10000);
                    Socket s = serverSocket.accept();
                    this.print("Client connected");
                    ConnectionHandler ch = new ConnectionHandler(new BufferedReader(new InputStreamReader(
                        s.getInputStream())),
                            new PrintWriter(s.getOutputStream()), this);
                    this.connections.add(ch);
                    new Thread(ch).start();
                } catch (SocketTimeoutException e) {
                    Thread.yield();
                }
            }
        } catch (IOException e) {
            this.print("Server exception " + e.getMessage());
        }
    }
}
