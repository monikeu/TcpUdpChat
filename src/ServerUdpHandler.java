import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class ServerUdpHandler extends Thread {

    private DatagramSocket datagramSocket;
    private DatagramPacket packet;
    private DatagramPacket sendPacket;
    private byte[] buff = new byte[2048];
    private List<ClientUdpData> clientsData = Collections.synchronizedList(new ArrayList<>());
    private String msg;

    public ServerUdpHandler(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public void run() {
        while (true) {
            try {
                packet = new DatagramPacket(buff, buff.length);
                datagramSocket.receive(packet);
                msg = new String(packet.getData(), 0, packet.getLength());

            } catch (IOException e) {
                e.printStackTrace();
            }


            if (msg.equals("Udp connection")) {
                System.out.println("Client connected...");
                ClientUdpData clientUdpData = new ClientUdpData(packet.getPort(), packet.getAddress());
                clientsData.add(clientUdpData);
            }else if(msg.equals("DISCONNECT")){
                clientsData = clientsData.stream().filter(e -> e.getPort() !=packet.getPort() || packet.getAddress()!=packet.getAddress()).collect(Collectors.toList());
            }
            else {
                sendToAll();
            }
        }
    }

    private void sendToAll() {
        for (ClientUdpData c : clientsData) {
            if (packet.getPort() != c.getPort()) {
                buff = msg.getBytes();
                sendPacket = new DatagramPacket(buff, buff.length, c.getAddress(), c.getPort());
                try {
                    datagramSocket.send(sendPacket);
                } catch (IOException e) {
                    System.out.println("Couldn't send message...");
                }
            }

        }
    }
}
