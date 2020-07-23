package Aufgabe_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

// EMpfängt per TCP Socket eine Nachricht aus Netzwerk 1 und broadcastet diese in das eigene Netzwerk (Netzwerk 2)
public class TCPEmpfängerBroadcast {
    private static final String BROADCAST = "192.168.178.255"; // Muss auf Broadcast Adresse des zweiten Netzwerkes geändert werden.
    private static final int BROADCASTPORT = 8884;
    private static final int BUFSIZE = 15; //Feste länge (n)
    private static int TCPPORT = 9996;


    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(TCPPORT)) {
            System.out.println("TCP Server auf " + serverSocket.getLocalSocketAddress() + " gestartet ...");
            while (true) {
                connect(serverSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                    broadcastMessages(input);
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
            packetOut.setLength(BUFSIZE);
            packetOut.setAddress(iaddr);
            System.out.println ( new String(packetOut.getData()));
            packetOut.setPort(BROADCASTPORT);
            socket.send(packetOut);
            System.out.println("Paket wurde gebroadcastet");


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Message konnte nicht gebroadcastet werden");
        }
    }
    public static void main(String[] args) {
        new TCPEmpfängerBroadcast().start();
    }
}




