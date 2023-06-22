package hw.Networking.Server;

import hw.Networking.Handler.StoreClientTCPHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class StoreServerTCP extends Thread{
    private ServerSocket ServerSocket;

    public void StartupServer(int port) throws IOException {
        ServerSocket = new ServerSocket(port);
        ServerSocket.setSoTimeout(6969);

        Socket ClientSocket;
        while (true)
        {
            try {
                ClientSocket = ServerSocket.accept();
            } catch (SocketTimeoutException e) {
                ServerSocket.close();
                break;
            }

            new StoreClientTCPHandler(ClientSocket).start();
        }
    }

    public void run() {
        StoreServerTCP server = new StoreServerTCP();
        try {
            server.StartupServer(6666);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
