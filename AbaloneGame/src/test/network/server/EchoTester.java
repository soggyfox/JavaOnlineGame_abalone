//package network.server;
//
//import org.junit.jupiter.api.Test;
//
//import java.io.*;
//import java.net.Socket;
//
//public class EchoTester extends Thread {
//    private Socket socket;
//    public EchoTester(Socket socket) {
//        this.socket = socket;
//    }
//
//    @Override
//    @Test
//    public void run() {
//        try {
//            BufferedReader input = new BufferedReader(
//                    new InputStreamReader(socket.getInputStream()));
//            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
//
//            while (true) {
//                try {
//                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//                    dos.writeUTF("welcome To the Server \nThis is the Server communicating to client\nSERVER TO CLIENT TEST PASSED");
//                }catch (Exception e){
//                }
//                String echoString = input.readLine();
////                assetEquals(echoString,"exit");
//                if (echoString.equals("exit")) {
////
////                    System.out.println("MOCK SERVER\nCLIENT TO SERVER TEST PASSED ");
////                    break;
////                }
//                output.print(echoString);
//
//                try {
//                    Thread.sleep(150);
//
//                } catch (InterruptedException e) {
//                    System.out.println("Thread interrupted");
//
//                }
//                System.out.println(echoString);
//            }
//
//        } catch (IOException e) {
//            System.out.println("IO exception Leave be gone: " + e.getMessage());//prevents double turn
//        } finally {
//            try {
//                socket.close();
//            } catch (IOException e) {
//
//            }
//        }
//
//    }
//}
//
