package Aufgabe_2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Date;

public class Empfänger {
    private static final int PORT = 8888;
    private static final int BUFSIZE = 512;

    public static void main(final String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);

            System.out.println("Socket geöffnet ...");

            while (true) {
                socket.receive(packetIn);
                System.out.println(
                        "Received: " + packetIn.getLength() + " bytes: " + new String(packetIn.getData()));
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
