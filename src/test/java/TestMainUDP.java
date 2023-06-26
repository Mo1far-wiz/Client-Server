import hw.Encryprion.Encryptor;
import hw.Networking.Client.StoreClientUDP;
import hw.Networking.Message;
import hw.Networking.Server.StoreServerUDP;
import hw.Shop.Product;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.DatagramSocket;

public class TestMainUDP {
    StoreServerUDP server;
    DatagramSocket ClientSocket;
    int PORT = 6666;
    Product berries = new Product(1, "Q",120, "add 100");
    Product berries2 = new Product(1, "W",120, "delete 1");
    Message message = new Message(4, 1, berries.toString().getBytes());
    Message message2 = new Message(4, 1, berries2.toString().getBytes());


    Product OK = new Product(1,"Q",1, "OK");
    Message OKK = new Message(message.getCType(), message.getBUserId(), "Processed".getBytes());

    @BeforeEach
    public void setup() throws IOException {
        ClientSocket = new DatagramSocket(PORT);
        server = new StoreServerUDP();
        server.start();
    }

    @Test
    public void SingleClientSendsMessages() throws Exception {
        System.out.println("Single Client: ");
        ClientSocket = new DatagramSocket(PORT);

        StoreClientUDP client = new StoreClientUDP(ClientSocket);
        Message response = client.SendMessage(new Message(Encryptor.encrypt(message)));
        System.out.println("upd resp : " + response);
        Assertions.assertEquals(OKK, response);
    }

    @Test
    public void MultiThreadClientSendMessages() throws InterruptedException, IOException {
        ClientSocket = new DatagramSocket(PORT);

        System.out.println("MultiClient: ");
        Thread a = new Thread(() -> {
            try {
                StoreClientUDP client = new StoreClientUDP(ClientSocket);
                Message response = client.SendMessage(new Message(Encryptor.encrypt(message)));
                System.out.println("upd resp : " + response);
                Assertions.assertEquals(OKK, response);
                client.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

        });
        Thread b = new Thread(() -> {
            try {
                StoreClientUDP client = new StoreClientUDP(ClientSocket);
                Message response = client.SendMessage(new Message(Encryptor.encrypt(message2)));
                System.out.println("upd resp : " + response);
                Assertions.assertEquals(OKK, response);
                client.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        });

        a.start();
        b.start();

        a.join();
        b.join();
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        server.join();
    }
}