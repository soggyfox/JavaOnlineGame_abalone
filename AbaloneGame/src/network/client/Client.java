package network.client;

import model.objects.Board;
import network.protocol.ProtocolMessages;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;

public class Client {
    public static final int PORT = 69;

    private Board game;

    public Client(InetAddress serverAddress) {

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
