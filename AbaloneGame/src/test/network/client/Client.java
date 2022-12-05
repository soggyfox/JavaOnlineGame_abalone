package network.client;

import controller.agent.Agent;
import controller.agent.HumanAgent;
import controller.agent.NaiveAgent;
import controller.game.Game;
import controller.game.IllegalMoveException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import model.objects.Board;
import model.player.Move;
import model.player.Player;
import network.protocol.Protocol;
import network.protocol.ProtocolMessages;
import network.server.ArgumentException;
import view.Tui;
import view.UI;

public class Client {
    public static final int PORT = 69;
    private Agent agent;
    private UI ui;
    private Game game;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Creates a new Client.
     * @param serverAddress the address of the server to connect to.
     */
    public Client(InetAddress serverAddress) {
        try {
            Socket socket = new Socket(serverAddress.getHostAddress(), PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method of the Client, starts a Client object and handles the client-side networking.
     * @param args any Program Arguments
     * @throws IOException if the socket fails
     */
    public static void main(String[] args) throws IOException {
        InetAddress ip = null;
        String nameHost = "";
        BufferedReader in;
        PrintWriter out;
        Client cl;
        while (ip == null) {
            InputStreamReader getIP = new InputStreamReader(System.in);
            BufferedReader inn = new BufferedReader(getIP);//r.close br.close
            System.out.println("Enter hosts to which you wish to connect to");
            nameHost = inn.readLine();
            try {
                ip = InetAddress.getByName(nameHost);
            } catch (UnknownHostException e) {
                System.out.println("Connecting to host: " + nameHost + "  Failed -> \n Please try again");
                inn.close();
                getIP.close();
                continue;
            }

            try {
                cl = new Client(InetAddress.getLocalHost());
                cl.ui = new Tui();
                cl.agent = args[0].equals("r") ? new NaiveAgent() : new HumanAgent(cl.ui);
                Scanner scanner = new Scanner(System.in);
                String echoString;
                String response;
                boolean a = false;
                do {
                    System.out.println("Enter nickname ");
                    String nick = scanner.nextLine();
                    cl.send(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + nick);
                    cl.agent.setPlayer(new Player(nick));
                    a = true;
                } while (a == false);

                do {
                    String msg = cl.in.readLine();
                    if (msg != null) {
                        cl.handleMessage(msg);
                    }
                } while (true);//Closes application on client wanting to exit
            } catch (SocketTimeoutException e) {
                System.out.println("The socket timed out");
            } catch (IOException e) {
                System.out.println("Client Error: ");
                e.printStackTrace();
                System.out.println("OR Failed to connect to host name:" + nameHost);
            }
        }
    }

    /**
     * Handles incoming messages, appropriately responding to them if applicable.
     *
     * @param message the incoming message
     */
    public void handleMessage(String message) {
        String[] splitMessage = message.split(ProtocolMessages.DELIMITER);
        String command = splitMessage[0];
        String[] args = new String[splitMessage.length - 1];
        System.arraycopy(splitMessage, 1, args, 0, args.length);
        try {
            this.getClass().getMethod(command, String[].class).invoke(this, (Object) args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles ProtocolMessages.FIRST_PLAYER .
     *
     * @param args the arguments that come with the message
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void f(String[] args) {
        int playerAmount = 2;
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many players do do you want?");
        playerAmount = scanner.nextInt();
        send(ProtocolMessages.PLAYER_AMOUNT + ProtocolMessages.DELIMITER + playerAmount);
    }

    /**
     * Handles ProtocolMessages.SUCCESS .
     *
     * @param args the arguments that come with the message
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void s(String[] args) {
        // Auto-respond with READY
        send(ProtocolMessages.READY);
    }

    /**
     * Handles ProtocolMessages.NUM_OF_PLAYERS .
     *
     * @param args the arguments that come with the message
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void g(String[] args) {
        List<Player> players = new ArrayList<>(Integer.parseInt(args[1]));
        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            players.add(null);
            if (i != Integer.parseInt(args[1])) {
                players.set(i, new Player("Player " + i));
            } else {
                players.set(i, agent.getPlayer());
            }
        }
        game = new Game(players);
        // Set the color accordingly
        agent.setGame(game);
        System.out.println("You are player " + agent.getPlayer().getColor().toString() + "⏺\u001b[0m");
        System.out.println(game.getBoard().toString());
    }

    /**
     * Handles ProtocolMessages.TURN .
     *
     * @param args the arguments that come with the message
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void t(String[] args) {
        send(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + Protocol.encodeMove(agent.makeMove()));
    }

    /**
     * Handles ProtocolMessages.MOVE .
     *
     * @param args the arguments that come with the message
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void m(String[] args) throws ArgumentException {
        // Validation: check first argument is in {'1','2','3'}, args.length == 2 * first argument + 2,
        //     last argument is in {'R','T','D','G','C','V'} and rest makes valid coords
        if (Integer.parseInt(args[1]) < 1 || Integer.parseInt(args[1]) > 3) {
            throw new ArgumentException(String.format("First argument must be an int between 1 and 3. Given: %s",
                args[1]));
        } else if (args.length != 2 * Integer.parseInt(args[1]) + 3) {
            throw new ArgumentException(String.format("Amount of arguments does not match. "
                + "Given: %d arguments, %s marbles", args.length, args[1]));
        } else if (!ProtocolMessages.directions.contains(args[args.length - 1])) {
            throw new ArgumentException(String.format("Direction invalid. Given: %s", args[args.length - 1]));
        }
        for (int i = 2; i <= Integer.parseInt(args[1]); i += 2) {
            if (!Board.isField(args[i] + args[i + 1])) {
                throw new ArgumentException(String.format("%s, %s is no valid field on the board.",
                    args[i], args[i + 1]));
            }
        }
        String[] from = new String[Integer.parseInt(args[1])];
        for (int i = 2; i < args.length - 1; i += 2) {
            from[(int) (i / 2) - 1] = args[i] + "" + args[i + 1];
        }
        Move move = new Move(game.getPlayers().size(), game.getPlayers().get(Integer.parseInt(args[0])),
            Arrays.asList(from), Protocol.toVector(args[args.length - 1]));
        try {
            game.move(move);
            System.out.println(game.getBoard().toString());
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles ProtocolMessages.NOT_VALID .
     *
     * @param args the arguments that come with the message
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void n(String[] args) {
        ui.invalidMove();
        t(new String[] {});
    }

    /**
     * Handles ProtocolMessages.VALID .
     *
     * @param args the arguments that come with the message
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void v(String[] args) {
        //Do nothing
    }

    /**
     * Handles ProtocolMessages.WINNER .
     *
     * @param args the arguments that come with the message
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void w(String[] args) {
        if (Integer.parseInt(args[0]) == 0) {
            ui.draw();
        } else {
            ui.winner(game.getPlayers().get(Integer.parseInt(args[0]) - 1));
        }
    }

    /**
     * Handles ProtocolMessages.UNEXPECTED_MESSAGE .
     *
     * @param args the arguments that come with the message
     */
    @SuppressWarnings("checkstyle:MethodName")
    public void u(String[] args) {
        try {
            System.out.println(String.format("An error occurred: %s", args[0]));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("An error occurred.");
        }
    }

    /**
     * Sends a message to the other side of the socket.
     * @param message the message to be sent
     */
    public synchronized void send(String message) {
        System.out.println(message);
        out.println(message);
        out.flush();
    }
}
