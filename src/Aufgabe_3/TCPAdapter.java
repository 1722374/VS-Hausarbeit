package Aufgabe_3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

//Empfängt Broadcast (Netzwerk 1) und sendet Nachricht per TCP (Netzwerk 2)
public class TCPAdapter {

    private static final int BROADCASTPORT = 8883;
    private static final int BUFSIZE = 29;
    private static String TCPHOST = "192.168.178.25";
    private static int TCPPORT = 9996;

    public static void main(final String[] args) {
        try (DatagramSocket socket = new DatagramSocket(BROADCASTPORT);
        Socket tcpsocket = new Socket(TCPHOST, TCPPORT);
        PrintWriter out=new PrintWriter(tcpsocket.getOutputStream(),true)) {

        DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);

            System.out.println("Socket geöffnet ...");

             while (true) {
                socket.receive(packetIn);
                String data = new String(packetIn.getData());
                int länge = packetIn.getLength();
                System.out.println(
                        "Received: " + länge + " bytes: " + data);

                out.println(data);
                System.out.println("Wurde zum TCP empfänger gesendet: " + data);
                }





        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
