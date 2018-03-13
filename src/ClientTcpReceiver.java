import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientTcpReceiver extends Thread {
    private BufferedReader in;
    private String msg;


    public ClientTcpReceiver(Socket in) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(in.getInputStream()));
    }

    public void run(){
        while (true) {
            msg = null;

            try {
                msg = in.readLine();
            } catch (IOException e) {
//            e.printStackTrace();
            }
            if (msg != null) {
                System.out.println("received msg: " + msg);
            }
        }
    }
}
