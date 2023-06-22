import hw.Networking.Client.StoreClientTCP;
import hw.Networking.Message;
import hw.Networking.Server.StoreServerTCP;
import hw.Shop.Goods;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestMainTCP {
    StoreServerTCP server;
    int PORT = 6666;
    Goods berries = new Goods(1, 1, 120, "add 100");
    Goods berries2 = new Goods(2, 2, 220, "delete 1");
    Message message = new Message(4, 1, berries.toString().getBytes());
    Message message2 = new Message(4, 1, berries2.toString().getBytes());

    Goods OK = new Goods(1, 1, 1, "OK");
    Message OKK = new Message(message.getCType(), message.getBUserId(), "Processed".toString().getBytes());

    @BeforeEach
    public void setup() {
        server = new StoreServerTCP();
        server.start();
    }

    @Test
    public void SingleClientSendsMessages() throws Exception {
        System.out.println("Single Client: ");
        StoreClientTCP client = new StoreClientTCP();
        client.InitConnection(PORT);
        Message response = client.SendMessage(message);
        System.out.println("tcp resp : " + response);
        Assertions.assertEquals(OKK, response);
        client.StopConnection();

    }


    @Test
    public void MultiThreadClientSendMessages() throws InterruptedException {
        System.out.println("MultiClient: ");
        Thread a = new Thread(() -> {
            try {
                StoreClientTCP client = new StoreClientTCP();
                client.InitConnection(PORT);;
                Message response = client.SendMessage(message);
                System.out.println("resp : " + response);
                Assertions.assertEquals(OKK, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        Thread b = new Thread(() -> {

            try {
                StoreClientTCP client = new StoreClientTCP();
                client.InitConnection(PORT);
                Message response = client.SendMessage(message2);
                System.out.println("resp : " + response);
                Assertions.assertEquals(OKK, response);
            } catch (Exception e) {
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