package Aufgabe_3_c;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class Empfänger {
    private static final int PORT = 8882;
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
            e.printStackTrace();
        }
    }
}
