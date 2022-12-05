package network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class EchoHelper extends Thread {
    private Socket socket;

    public EchoHelper(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            while(true) {
                String echoString = input.readLine();
                System.out.println("Received  input: " + echoString);
                if(echoString.equals("exit")) {
                    break;
                }

                try {
                    Thread.sleep(15000);

                } catch(InterruptedException e) {
                    System.out.println("Thread interrupted");

                }

                output.println(echoString);
            }

        } catch(IOException e) {
            System.out.println("L Leave be gone: " + e.getMessage());//prevents double turn
        } finally {
            try {
                socket.close();
            } catch(IOException e) {

            }
        }

    }
}

