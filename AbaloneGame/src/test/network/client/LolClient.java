package network.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class LolClient {
    BufferedReader in;
    BufferedWriter out;
    Socket socket;
    public boolean isConnect;

//    public LolClient(Socket socket) {
//        socket = null;
//        //this.socket = socket;
//
//        public boolean checkConnected(Socket socket){
//            //boolean isConnect = false;
//            this.isConnect = false;
//            if (socket.isConnected()) {
//                isConnect = true;
//                try {
//                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    isConnect = false;
//                }catch (Exception e){
//                    System.out.println("some exception");
//                    e.printStackTrace();
//                    isConnect = false;
//                }
//            }
//        }
//    }

    public static void main(String[] args) throws IOException {
        final int PORT = 5000;
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
                        continue;
                    }
                }while(a == false);
                do {
                    System.out.println("Enter Control");
                    echoString = scanner.nextLine();
                    stringToEcho.println(echoString);
                    if (!echoString.equals("exit")) {
                        response = echoes.readLine();
                        System.out.println("i found you");
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
//Socket connection stuff
    private static class Connection {

    }
}


//once connectedinvoke board creation
//player waits in lobby
/*
References
https://www.oracle.com/technetwork/java/socket-140484.html#client
 */