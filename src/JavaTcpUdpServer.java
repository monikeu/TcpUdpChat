import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaTcpUdpServer {

    private static final int maxThreads = 100;
    private static int numbOfClientThreads = 0;
    private PrintWriter out;
    private ExecutorService executorService;
    private static List<Socket> clientsSockets = Collections.synchronizedList(new ArrayList<>());


    private void runServer() throws IOException {

        System.out.println("JAVA TCP SERVER");
        int portNumber = 12345;


        ServerSocket serverSocket = null;
        executorService = Executors.newFixedThreadPool(10);
        DatagramSocket datagramSocket;

        try {
            // create  socket

            datagramSocket = new DatagramSocket(portNumber);
            ServerUdpHandler threadFoClientUdp = new ServerUdpHandler(datagramSocket);
            threadFoClientUdp.start();

            serverSocket = new ServerSocket(portNumber);
            while (true) {
                // accept client
                Socket clientSocket = serverSocket.accept();
                addSocket(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    private void addSocket(Socket clientSocket) {


        System.out.println("Creating client " + clientsSockets.size() + " thread for " + clientSocket);

        ServerTcpHandler threadForClient = new ServerTcpHandler(clientSocket, ++numbOfClientThreads, clientsSockets);

        executorService.execute(threadForClient);
        clientsSockets.add(clientSocket);
    }

    public static void main(String[] args) throws IOException {

        JavaTcpUdpServer javaTcpServer = new JavaTcpUdpServer();
        javaTcpServer.runServer();

    }
}
