package Aufgabe_3_c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class TCPEmpfängerBroadcast {
    private static final String BROADCAST = "192.168.178.255";
    private static final int BROADCASTPORT = 8887;
    private static int TCPPORT = 9998;


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
            int länge = Integer.parseInt(in.readLine()); // Da erste Nachricht immer die Länge ist.
            System.out.println("Länge des Datagrams " + länge);
            while ((input = in.readLine()) != null) {
                    System.out.println("Message von: " + socketAddress + ">>" + input);
                    broadcastMessages(input, länge);
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




