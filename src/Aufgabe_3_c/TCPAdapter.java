package Aufgabe_3_c;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

//Empfängt Broadcast (Netzwerk 1) und sendet Nachricht per TCP (Netzwerk 2)
public class TCPAdapter {

    private static final int BROADCASTPORT = 8886;
    private static final int BUFSIZE = 1024; //Erstmals höchste Datagramgröße
    private static String TCPHOST = "localhost";
    private static int TCPPORT = 9998;

    public static void main(final String[] args) {
        try (DatagramSocket socket = new DatagramSocket(BROADCASTPORT)) {

        DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);

            System.out.println("Socket geöffnet ...");

            while (true) {
                socket.receive(packetIn);
                String data = new String(packetIn.getData()).trim();
                int länge = data.getBytes().length;
                System.out.println(
                        "Received: " + länge + " bytes: " + data);
               sendData(data, länge);


            }
        } catch (IOException e) {
            System.err.println(e);
        }


    }


    private static void sendData(String data, int länge) {
        try (Socket tcpsocket = new Socket(TCPHOST, TCPPORT);
             PrintWriter out=new PrintWriter(tcpsocket.getOutputStream(),true)) {
                System.out.println("Mit zweitem Netzwerk verbunden");
                out.println(länge); //Messagegröße wird übergeben. Somit ist DatagramGröße beim TCP Empfänger Variabel
                out.println(data);
                System.out.println("Wurde zum TCP empfänger gesendet: " + data);

        } catch (IOException e) {
            System.err.println("TCP Verbindung konnte nicht herrgestellt werden");
        }
    }

}
