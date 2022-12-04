/*
*A network is a system of pcs connected to share resources. Use java.net for this
*Threads and IOStreams
*We will
*Running on different machines for example
Eg.Client server
One host is a client the other is a server
Client server interactions can be pn the same host or different ones
Each  pc has one physical connection and can be routed to the target via a PORT
each host should have a unique ip address
If same host use localhost or 127.0.0.1

Java.net has low and high level api via abstractions
We will use TCP/ip as it is reliable unlike UDP
START:
0)Server opens
LOOP:
1)Client opens a connection to server
2)Client requests to send data
3)server sends response
END LOOP
 4)client closes the connection
END START
 */
package network.server;
import model.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class LolServer {
    //init vars
    final int PORT = 5000;
    //main
    public static void main(String[] args) throws IOException {
        //new object of class lolServer created
        LolServer server = new LolServer();
        server.begin();
    }

    //start server
    public void begin() throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                new EchoHelper(serverSocket.accept()).start();
                System.out.println("Client connected");
                //new ConnectionHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    }

    public void addPlayer(Player player) {

    }

    public Collection<Object> getPlayerSet() {
        int a =2;
        return getPlayerSet();
    }

    public void setMaxPlayers(int parseInt) {
    }

    public void setReadyPlayer(Player player) {
    }

    public int getMaxPlayers() {
        return 4;
    }



    public void startGame() {
    }

    public int getPlayers() {
        return getPlayers();
    }
}
/*
  Socket socket = serverSocket.accept();
                System.out.println("Client Connected");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);//flush = sends
                String echoString = in.readLine();
                if (echoString.equals("exit")){
                    break;
                }
                else{
                    out.println("Server parrots: " + echoString);
                }
            }
 */
