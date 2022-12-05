package network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import model.objects.Board;
import model.player.Move;
import model.player.Player;
import network.protocol.Protocol;
import network.protocol.ProtocolMessages;

/**
 * Handles connection between client and server.
 * Every client->server message has its own method,
 * named by the sent string to support Java's Reflection system.
 */
public class ConnectionHandler implements Runnable {
    private BufferedReader in;
    private PrintWriter out;
    private Server server;
    private Player player;

    /**
     * Creates a ConnectionHandler.
     * @param in the input side (client->server)
     * @param out the output side (server->client)
     * @param server the server it belongs to
     */
    public ConnectionHandler(BufferedReader in, PrintWriter out, Server server) {
        this.in = in;
        this.out = out;
        this.server = server;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Handles client->server messages by invoking the corresponding method, generating a response if applicable.
     * @param message the incoming message
     * @throws ArgumentException if the arguments are malformed (incorrect number, incorrect syntax, etc.)
     *     or if a RuntimeException occurs in the method
     * @throws CommandException if the command does not exist or is forbidden from usage
     */
    public void handleMessage(String message) throws ArgumentException, CommandException {
        try {
            server.print(player.getName() + ": " + message);
        } catch (NullPointerException e) {
            server.print(message);
        }
        String[] splitMessage = message.split(ProtocolMessages.DELIMITER);
        String command = splitMessage[0];
        String[] args = new String[splitMessage.length - 1];
        System.arraycopy(splitMessage, 1, args, 0, args.length);
        try {
            this.getClass().getMethod(command, String[].class).invoke(this, (Object) args);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new CommandException(String.format("Command %s is invalid", command));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new ArgumentException(e.getMessage());
        }
    }

    /**
     * Handles ProtocolMessages.JOIN .
     * @param args the arguments that come with the message
     * @throws ArgumentException if the amount of arguments is unequal to 1
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void j(String[] args) throws ArgumentException {
        // Validation: make sure there is exactly 1 argument
        if (args.length != 1) {
            throw new ArgumentException(String.format("Method j must have exactly 1 argument. Given: %d", args.length));
        } else {
            // Administration: add the player
            player = new Player(args[0]);
            server.addPlayer(player);
            // Response: ProtocolMessages.FIRST_PLAYER or ProtocolMessages.SUCCESS
            if (server.getPlayerSet().size() == 1) {
                send(ProtocolMessages.FIRST_PLAYER);
            } else {
                send(ProtocolMessages.SUCCESS);
            }
        }
    }

    /**
     * Handles ProtocolMessages.PLAYER_AMOUNT .
     * @param args the arguments that come with the message
     * @throws ArgumentException if the amount of arguments is unequal to 1
     *     or the argument is not an integer between 1 and 4
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void p(String[] args) throws ArgumentException {
        // Validation: make sure there is exactly 1 argument that is an integer between 1 and 4
        if (args.length != 1 || Integer.parseInt(args[0]) < 1 || Integer.parseInt(args[0]) > 4) {
            String message = args.length != 1 ? String.format("Method p must have exactly 1 argument. Given: %d",
                    args.length) : String.format("Argument for p must be in {'1','2','3','4'}. Given: %s", args[0]);
            throw new ArgumentException(message);
        } else {
            // Administration: set the max players to given value
            server.setMaxPlayers(Integer.parseInt(args[0]));
            // Response: ProtocolMessages.SUCCESS
            send(ProtocolMessages.SUCCESS);
        }
    }

    /**
     * Handles ProtocolMessages.READY .
     * @param args the arguments that come with the message
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void r(String[] args) {
        // Validation: none (args is ignored)
        // Administration: set the player to be ready, start game if all players are ready
        server.setReadyPlayer(player);
        if (server.getPlayerSet().size() == server.getMaxPlayers()) {
            boolean allReady = true;
            for (Player p : server.getPlayerSet()) {
                allReady &= server.getPlayers().get(p);
            }
            if (allReady) {
                new Thread(() -> server.startGame()).start();
            }
        }
    }

    /**
     * Handles ProtocolMessages.MOVE .
     * @param args the arguments that come with the message
     * @throws ArgumentException if the first argument is not an integer between 1 and 3
     *     or the amount of arguments does not correspond to the first argument
     *     or the last argument is not in the set {'R', 'T', 'D', 'G', 'C', 'V'}
     *     or a pair of the other arguments do not make a valid field
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void m(String[] args) throws ArgumentException {
        // Validation: check first argument is in {'1','2','3'}, args.length == 2 * first argument + 2,
        //     last argument is in {'R','T','D','G','C','V'} and rest makes valid coords
        if (Integer.parseInt(args[0]) < 1 || Integer.parseInt(args[0]) > 3) {
            throw new ArgumentException(String.format("First argument must be an int between 1 and 3. Given: %s",
                    args[0]));
        } else if (args.length != 2 * Integer.parseInt(args[0]) + 2) {
            throw new ArgumentException(String.format("Amount of arguments does not match. "
                    + "Given: %d arguments, %s marbles", args.length, args[0]));
        } else if (!ProtocolMessages.directions.contains(args[args.length - 1])) {
            throw new ArgumentException(String.format("Direction invalid. Given: %s", args[args.length - 1]));
        }
        for (int i = 1; i <= Integer.parseInt(args[0]); i += 2) {
            if (!Board.isField(args[i] + args[i + 1])) {
                throw new ArgumentException(String.format("%s, %s is no valid field on the board.",
                    args[i], args[i + 1]));
            }
        }
        // Administration: check the move and apply if it is valid
        String[] from = new String[Integer.parseInt(args[0])];
        for (int i = 1; i < args.length - 1; i += 2) {
            from[(int) (i / 2)] = args[i] + "" + args[i + 1];
        }
        Move move = new Move(server.getPlayers().size(), player, Arrays.asList(from),
                Protocol.toVector(args[args.length - 1]));
        int turn = server.getGame().getTurn();
        server.setMove(move);
        // Response A: broadcast move to all clients and send ProtocolMessages.VALID to client
        send(ProtocolMessages.VALID);
        server.broadcast(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + turn
                + ProtocolMessages.DELIMITER + Protocol.encodeMove(move));
    }

    /**
     * Sends a message to the Client.
     * @param message the message to be sent
     */
    public synchronized void send(String message) {
        try {
            server.print(player.getName() + ": " + message);
        } catch (NullPointerException e) {
            server.print(message);
        }
        out.println(message);
        out.flush();
    }

    /**
     * Handles messages continuously.
     */
    @Override
    public void run() {
        while (true) {
            try {
                String msg = in.readLine();
                if (msg != null) {
                    handleMessage(msg);
                }
            } catch (IOException e) {
                server.disconnect(player);
            } catch (ArgumentException | CommandException e) {
                out.print(ProtocolMessages.UNEXPECTED_MESSAGE + ProtocolMessages.DELIMITER + e.getMessage());
            }
        }
    }
}
