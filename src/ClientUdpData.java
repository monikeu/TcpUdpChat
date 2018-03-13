import java.net.InetAddress;

public class ClientUdpData {
    private int port;
    private InetAddress address;

    public ClientUdpData(int port, InetAddress address) {
        this.port = port;
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }
}
