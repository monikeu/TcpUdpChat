import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class JavaTcpClient {
    private String multicastIP = "224.0.113.0";
    private int groupPort = new Random().nextInt(3) + 4444;
    private int portNumber = 12345;
    private Socket socket = null;
    private byte[] sendBuffer = new byte[2048];
    private DatagramSocket udpSocket;
    private DatagramPacket datagramPacket;
    private Thread receiveTcpThread;
    private Thread receiveUdpThread;
    private Thread receiveUdpMulticastThread;
    private PrintWriter out;
    private String hostName = "localhost";
    private String udpMessage = "Udp connection";
    private String msg;
    private String shruggle = " ¯\\_(ツ)_/¯";
    private MulticastSocket multicastSocket;



    public void runClient() throws IOException {
        System.out.println("JAVA TCP CLIENT");
        System.out.println("Member of group " + groupPort);

        InetAddress inetGroupAddress = InetAddress.getByName(multicastIP);
        InetAddress inetAddress = InetAddress.getByName(hostName);


        try {
            multicastSocket = new MulticastSocket();
            socket = new Socket(hostName, portNumber);
            udpSocket = new DatagramSocket();

            registerUdpConnection(inetAddress);
            startHandlers();


            while (true) {
                Scanner scanner = new Scanner(System.in);
                msg = scanner.nextLine();

                switch (msg) {
                    case "TCP":
                        out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(msg);
                        break;
                    case "UDP":
                        sendBuffer = shruggle.getBytes();
                        datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, inetAddress, portNumber);
                        udpSocket.send(datagramPacket);
                        break;
                    case "MULTI":
                        sendBuffer = shruggle.getBytes();
                        datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, inetGroupAddress, groupPort);
                        multicastSocket.send(datagramPacket);
                        break;
                    case "DISCONNECT":
                        out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("DISCONNECT");

                        sendBuffer = "DISCONNECT".getBytes();
                        datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, inetAddress, portNumber);
                        udpSocket.send(datagramPacket);

                        break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }

    }

    private void startHandlers() throws IOException {
        receiveTcpThread = new ClientTcpReceiver(socket);
        receiveTcpThread.start();

        receiveUdpThread = new ClientUdpReceiver(udpSocket);
        receiveUdpThread.start();

        receiveUdpMulticastThread = new ClientUdpMulticastReceiver(groupPort, multicastIP);
        receiveUdpMulticastThread.start();
    }

    private void registerUdpConnection(InetAddress inetAddress) throws IOException {
        sendBuffer = udpMessage.getBytes();
        datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, inetAddress, portNumber);
        udpSocket.send(datagramPacket);
    }

    public static void main(String[] args) throws IOException {
        new JavaTcpClient().runClient();
    }

}
