package network.client;

import model.objects.Board;
import network.protocol.ProtocolMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MergeClient {
    public static final int PORT = 69;

    private Board game;



    public static void main(String[] args) throws IOException {
        // ConnectionHandler connectionHandler() = null;
        InetAddress ip = null;
        while (ip == null) {
            InputStreamReader getIP = new InputStreamReader(System.in);
            BufferedReader inn = new BufferedReader(getIP);//r.close br.close
            System.out.println("Enter hosts to which you wish to connect to");
            String name_host = inn.readLine();
            try {
                ip = InetAddress.getByName(name_host);
            } catch (UnknownHostException e) {
                System.out.println("Connecting to host: " + name_host +"  Failed -> \n Please try again");
                inn.close();
                getIP.close();
                continue;
            }

            try {
                Socket socket = new Socket(name_host, PORT);
                //connection = new(LolClient(Socket socket));
                BufferedReader echoes = new BufferedReader(

                        new InputStreamReader(socket.getInputStream()));
                PrintWriter stringToEcho = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in);
                String echoString;
                String response;
                boolean a = false;
                do{
                    InputStreamReader getNick = new InputStreamReader(System.in);
                    BufferedReader in = new BufferedReader(getNick);//r.close br.close
                    System.out.println("Enter nickname ");
                    try {
                        String nick = in.readLine();
                        a =true;
                    } catch (UnknownHostException e) {
                    }
                }while(!a);
                do {
                    System.out.println("Enter Control");
                    echoString = scanner.nextLine();
                    stringToEcho.println(echoString);
                    if (!echoString.equals("exit")) {
                        response = echoes.readLine();
                        System.out.println(response);
                    }
                } while (!echoString.equals("exit"));//Closes application on client wanting to exit
            } catch (SocketTimeoutException e) {
                System.out.println("The socket timed out");
            } catch (IOException e) {
                System.out.println("Client Error: " + e.getMessage());
                System.out.println("OR Failed to connect to host name:" + name_host);
            }
        }
    }
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
     * @param args the arguments that come with the message
     */
    public void f(String[] args) {
        //TODO implement
    }

    /**
     * Handles ProtocolMessages.SUCCESS .
     * @param args the arguments that come with the message
     */
    public void s(String[] args) {
        //TODO implement
    }

    /**
     * Handles ProtocolMessages.NUM_OF_PLAYERS .
     * @param args the arguments that come with the message
     */
    public void g(String[] args) {
        //TODO implement
    }

    /**
     * Handles ProtocolMessages.TURN .
     * @param args the arguments that come with the message
     */
    public void t(String[] args) {
        //TODO implement
    }

    /**
     * Handles ProtocolMessages.MOVE .
     * @param args the arguments that come with the message
     */
    public void m(String[] args) {
        //TODO implement
    }

    /**
     * Handles ProtocolMessages.NOT_VALID .
     * @param args the arguments that come with the message
     */
    public void n(String[] args) {
        //TODO implement
    }

    /**
     * Handles ProtocolMessages.VALID .
     * @param args the arguments that come with the message
     */
    public void v(String[] args) {
        //TODO implement
    }

    /**
     * Handles ProtocolMessages.WINNER .
     * @param args the arguments that come with the message
     */
    public void w(String[] args) {
        //TODO implement
    }

    /**
     * Handles ProtocolMessages.UNEXPECTED_MESSAGE .
     * @param args the arguments that come with the message
     */
    public void u(String[] args) {
        //TODO implement
    }
}
