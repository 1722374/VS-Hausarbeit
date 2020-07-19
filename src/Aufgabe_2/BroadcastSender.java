package Aufgabe_2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Date;

public class BroadcastSender {

    private static final String BROADCAST = "192.168.178.255";
    private static final int PORT = 8888;
    private static final int BUFSIZE = 512;
    private static final int TIMEOUT = 2000;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            while (true) {

                String zeit = (new Date().toString());
                InetAddress iaddr = InetAddress.getByName(BROADCAST);
                DatagramPacket packetOut = new DatagramPacket(zeit.getBytes(), zeit.getBytes().length, iaddr, PORT);
                socket.send(packetOut);
                Thread.sleep(20000);
            }

            } catch(SocketTimeoutException e){
                System.err.println("Timeout: " + e.getMessage());
            } catch(Exception e){
                System.err.println(e);
            }
        }

}
