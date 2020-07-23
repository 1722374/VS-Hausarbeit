package Aufgabe_1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

public class Sender { // In diesem Fall wird der Client als Sender definiert

    private final String HOST = "192.168.178.25";
    private final int PORT = 4495;
    private final int BUFSIZE = 512;
    private final int TIMEOUT = 2000;
    private DatagramSocket socket;
    private DatagramPacket packetOut;
    private DatagramPacket packetIn;
    private boolean handshakeReceived = false;
    int handshakeCounter = 0; //Anzahl der zu erwartenden Anfragen


    Sender() {

    }

    public void start() {

        try {
            InetAddress iaddr = InetAddress.getByName(HOST);
            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);
            packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
            packetOut = new DatagramPacket(new byte[BUFSIZE], BUFSIZE, iaddr, PORT);
            if (!handshakeReceived) { // / Wenn noch kein Handshake aushewertet wurde -> Handshake auswerten (Anzahl der zu sendenden Daten)
                handshake();
            }

            sendData(handshakeCounter); // Daten anhand von Handshakecounter senden

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private byte[] randomData() {
        double random = 1.2 + Math.random() * (41.23 - 1.2);
        return new String(random +"").getBytes();
    }


    private void handshake () throws IOException {
        //Empfänger (Server) anpingen
        byte [] ping = new byte[1]; // Byte ohne Inhalt senden, um Handshake zu initialisieren.
        packetOut.setData(ping);
        packetOut.setLength(ping.length);
        socket.send(packetOut);

        socket.receive(packetIn); // Auf Handshakeanzahl warten
        int data = Integer.parseInt(new String (packetIn.getData()).trim());
        this.handshakeCounter = data;
        this.handshakeReceived = true;
        System.out.println("Übertragener Handshake Counter: " + data);
    }

    private void sendData(int handshakeCounter) throws IOException, InterruptedException { // Daten senden
        for (int i = 0; i< handshakeCounter; i++) {
            byte [] data = randomData();
            Thread.sleep(200);
            byte[] outputData = addChecksum(data);
            packetOut.setData(outputData);
            packetOut.setLength(outputData.length);
            socket.send(packetOut);
            System.out.println("Gesendet :" + new String(outputData) + "Datenlänge: " + outputData.length + " Index "+ i + 1);
        }
    }



    private byte[] addChecksum (byte [] data) throws IOException {
        //Daten mit Checksum Wert anreichern
        Checksum adler32 = new Adler32();
        adler32.update(data,0, data.length);
        long checksum = adler32.getValue();

        byte [] checksumBytes = new String(checksum+ "").trim().getBytes();
        System.out.print(checksumBytes.length);
        // Datenarry + Checksumarray
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write( data );
        outputStream.write( checksumBytes);
        byte dataWithCheck[] = outputStream.toByteArray( );
        adler32.reset();

        System.out.println("Checksum: " + checksum);
        return dataWithCheck;
    }


    public static void main(String[] args) {
        Sender client = new Sender();
        client.start(); // Client starten
    }





}
