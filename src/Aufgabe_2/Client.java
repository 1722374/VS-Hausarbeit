package Aufgabe_2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Client {

    private static final String BROADCAST = "192.168.178.255";
    private static final int PORT = 8888;
    private static final int BUFSIZE = 512;
    private static final int TIMEOUT = 2000;

    public static void main(String[] args) {
        byte[] data = "Broadcast-Nachricht".getBytes();
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            InetAddress iaddr = InetAddress.getByName(BROADCAST);
            DatagramPacket packetOut = new DatagramPacket(data, data.length, iaddr, PORT);
            socket.send(packetOut);
            DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
            socket.receive(packetIn);
            String received = new String(packetIn.getData(), 0, packetIn.getLength());
            System.out.println("Received: " + received);
        } catch (SocketTimeoutException e) {
            System.err.println("Timeout: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
