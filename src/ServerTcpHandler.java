import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerTcpHandler extends Thread {

    private List<Socket> clientsSockets;
    private Socket mySocket;
    private final int id;
    private BufferedReader in;
    private PrintWriter out;


    public ServerTcpHandler(Socket socket, int i, List<Socket> sockets) {
        this.mySocket = socket;
        this.id = i;
        this.clientsSockets = sockets;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String msg = null;
        try {
            msg = in.readLine();
            sendToAll(msg);
//            removeSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToAll(String msg) {

        // sync is not required, cause here the thread is just reading
            for(Socket receverSocket : clientsSockets){
                if(receverSocket != mySocket){
                    try {
                        out = new PrintWriter(receverSocket.getOutputStream(), true);

                        if(msg.equals("DISCONNECT")){
                            removeSocket();
                            break;
                        }
                        else {
                            out.println("Client " + id + " sent " + msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

    }

    private void removeSocket() {
        clientsSockets.remove(mySocket);
    }



}
