package Aufgabe_3_c;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

//Empfängt Broadcast (Netzwerk 1) und sendet Nachricht per TCP (Netzwerk 2). Hier wird zusätzlich die Packetgröße mitgesendet.
public class TCPAdapterVarLen {

    private static final int BROADCASTPORT = 8883;
    private static final int BUFSIZE = 1024; //Erstmals höchste Datagramgröße
    private static String TCPHOST = "localhost";
    private static int TCPPORT = 9991;

    public static void main(final String[] args) {
        try (DatagramSocket socket = new DatagramSocket(BROADCASTPORT);
        Socket tcpsocket = new Socket(TCPHOST, TCPPORT);
        PrintWriter out=new PrintWriter(tcpsocket.getOutputStream(),true)){

        DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);

            System.out.println("Socket geöffnet ...");

            while (true) {
                socket.receive(packetIn);
                String data = new String(packetIn.getData()).trim();
                int länge = data.getBytes().length; // Variable Dtatagrammlänge, die mitgesendet wird.
                //int länge = 20; // Optional: Länge für alle gesendeten Datagramme hier variabel festlegen
                System.out.println(
                        "Received: " + länge + " bytes: " + data);
                out.println(länge); //Messagegröße wird übergeben. Somit ist DatagramGröße beim TCP Empfänger Variabel
                out.println(data);
                System.out.println("Wurde zum TCP empfänger gesendet: " + data);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
