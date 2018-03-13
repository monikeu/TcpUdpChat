import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ClientUdpMulticastReceiver extends Thread {

    private MulticastSocket multicastSocket;
    private DatagramPacket packet;
    private byte[] bufffer = new byte[2048];
    private int groupPort;
    private String groupIP;
    private InetAddress groupAddr;
    private String msg;

    public ClientUdpMulticastReceiver(int groupPort, String groupIP) {
        this.groupPort = groupPort;
        this.groupIP = groupIP;

    }

    public void run() {
        try {
            multicastSocket = new MulticastSocket(groupPort);
            groupAddr = InetAddress.getByName(groupIP);
            multicastSocket.joinGroup(groupAddr);

            if (multicastSocket != null) {
                while (true) {
                    packet = new DatagramPacket(bufffer, bufffer.length);
                    multicastSocket.receive(packet);
                    msg = new String(packet.getData(), 0, packet.getLength());
                    System.out.println(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
