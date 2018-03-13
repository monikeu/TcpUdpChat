import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientUdpReceiver extends Thread {

    private DatagramPacket packet;
    private String msg;
    private DatagramSocket udpSocket;
    private byte[] buffer = new byte[2048];

    public ClientUdpReceiver(DatagramSocket udpSocket) {
        this.udpSocket = udpSocket;
    }

    public void run(){
        while (true){
            packet=new DatagramPacket(buffer, buffer.length);
            try {
                udpSocket.receive(packet);
                msg = new String(packet.getData(), 0, packet.getLength());
                System.out.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
