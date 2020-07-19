package Aufgabe_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class TCPEmpfängerBroadcast {
    private static final String BROADCAST = "192.168.178.255";
    private static final int BROADCASTPORT = 8888;
    private static final int BUFSIZE = 29;
    private static int TCPPORT = 9998;


    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(TCPPORT)) {
            System.out.println("TCP Server auf " + serverSocket.getLocalSocketAddress() + " gestartet ...");
            while (true) {
                connect(serverSocket);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void connect(ServerSocket server) {
        SocketAddress socketAddress = null;
        try (Socket socket = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            socketAddress = socket.getRemoteSocketAddress();
            System.out.println("Verbindung zu " + socketAddress + " aufgebaut");


            String input;
            while ((input = in.readLine()) != null) {
                    System.out.println("Message von: " + socketAddress + ">>" + input);
                }




        } catch (IOException e) {
            System.err.println(e);
        } finally {
            System.out.println("Verbindung zu " + socketAddress + " abgebaut");
        }
    }


    private static void broadcastMessages(String data){
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            InetAddress iaddr = InetAddress.getByName(BROADCAST);
            DatagramPacket packetOut = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
            packetOut.setData(data.getBytes());
            packetOut.setAddress(iaddr);
            packetOut.setPort(BROADCASTPORT);
            socket.send(packetOut);


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Message konnte nicht gebroadcastet werden");
        }
    }
    public static void main(String[] args) {
        new TCPEmpfängerBroadcast().start();
    }
}




