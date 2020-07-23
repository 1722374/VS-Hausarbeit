package Aufgabe_3_c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

// Empfängt per TCP Socket eine Nachricht aus Netzwerk 1 und broadcastet diese in das eigene Netzwerk (Netzwerk 2)
public class TCPEmpfängerBroadcastVarLen {
    private static final String BROADCAST = "192.168.178.255";
    private static final int BROADCASTPORT = 8882;
    private static int TCPPORT = 9991;


    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(TCPPORT)) {
            System.out.println("TCP Server auf " + serverSocket.getLocalSocketAddress() + " gestartet ...");
            while (true) {
                getMessages(serverSocket);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void getMessages(ServerSocket server) {
        SocketAddress socketAddress = null;
        try (Socket socket = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            socketAddress = socket.getRemoteSocketAddress();
            System.out.println("Verbindung zu " + socketAddress + " aufgebaut");


            String input;
            int länge = Integer.parseInt(in.readLine());
            int counter =0;// Da erste Nachricht immer die Länge ist.
            while ((input = in.readLine()) != null) {
                counter++;
                if ((counter %2) != 1) {
                    länge = Integer.parseInt(input);
                } else {
                    System.out.println("Message von: " + socketAddress + ">>" + input);
                    broadcastMessages(input, länge);
                 }
                }




        } catch (IOException e) {
            System.err.println(e);
        } finally {
            System.out.println("Verbindung zu " + socketAddress + " abgebaut");
        }
    }


    private static void broadcastMessages(String data, int length){
        try (DatagramSocket socket = new DatagramSocket()) {

            socket.setBroadcast(true);
            InetAddress iaddr = InetAddress.getByName(BROADCAST);
            DatagramPacket packetOut = new DatagramPacket(new byte[length], length);
            packetOut.setLength(length); // Länge des Datagrams nochmal festlegen
            packetOut.setData(data.getBytes());
            packetOut.setAddress(iaddr);
            packetOut.setPort(BROADCASTPORT);
            socket.send(packetOut);
            System.out.println("Daten wurden gebroadcastet");



        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Message konnte nicht gebroadcastet werden");
        }
    }
    public static void main(String[] args) {
        new TCPEmpfängerBroadcastVarLen().start();
    }
}




