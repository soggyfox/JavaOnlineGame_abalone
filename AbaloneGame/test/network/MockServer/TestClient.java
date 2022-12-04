/*
NOTE 
1) RUN test server first and then run an instance of testClient.
*/
package network.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestClient {


    public static void main(String[] args) throws IOException {
        final int PORT = 5000;
       // ConnectionHandler connectionHandler() = null;
        InetAddress ip = null;
        while (ip == null) {
//            InputStreamReader getIP = new InputStreamReader(System.in);
//            BufferedReader inn = new BufferedReader(getIP);//r.close br.close
//            System.out.println("Enter hosts to which you wish to connect to");
            String name_host = "localhost";//setup local connection on same machine
            // inn.readLine();
            try {
                ip = InetAddress.getByName(name_host);
            } catch (UnknownHostException e) {
                System.out.println("Connecting to host: " + name_host +"  Failed -> \n Please try again");
//                inn.close();
//                getIP.close();
                continue;
            }

            try {
                Socket socket = new Socket(name_host, PORT);
                //connection = new(LolClient(Socket socket));
                BufferedReader echoes = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter stringToEcho = new PrintWriter(socket.getOutputStream(), true);
//                Scanner scanner = new Scanner(System.in);

                String echoString = "hello test";
                String response;
                boolean a = false;
                do{
//                    InputStreamReader getNick = new InputStreamReader(System.in);
//                    BufferedReader in = new BufferedReader(getNick);//r.close br.close
//                    System.out.println("Enter nickname ");
//                    try {
                        String nick = "jon  ";//in.readLine();
                        a =true;
                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        String FromServer = dis.readUTF();
                        System.out.println(FromServer);
//                    } catch (UnknownHostException e) {
//                        System.out.println("error with host test failed");
//                    }
                }while(!a);
                //                    System.out.println("Enter Control");
                for(int i=0;i<3;i++){
                    if(i==1){
                        echoString = "Test Hello "+i;//scanner.nextLine();
                    }

                    if(i ==2){
                        echoString = "exit";
                    }
                }

                stringToEcho.println(echoString);
                if (!echoString.equals("exit")) {
                    response = echoes.readLine();
                    System.out.println(response);
                }
            } catch (SocketTimeoutException e) {
                System.out.println("The socket timed out");
            } catch (IOException e) {
                System.out.println("Client Error: " + e.getMessage());
                System.out.println("OR Failed to connect to host name:" + name_host);
            }
        }
    }

}


/*once connected invoke a string to send7
If the server receives the same string from the client communication was successfully engaged
The server responds to confirm this success.
This tests that the client can communicate to the server and vice versa
References
https://www.oracle.com/technetwork/java/socket-140484.html#client
 */