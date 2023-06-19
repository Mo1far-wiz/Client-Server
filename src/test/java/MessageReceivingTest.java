import hw.MessageGenerator;
import hw.Networking.DraftReceiver;
import hw.Networking.Message;
import hw.Networking.Packet;
import hw.Shop.Goods;
import hw.Shop.Groups;
import hw.Statics;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MessageReceivingTest {
    @Test
    public void testMessageReceiving() throws Exception {
        // Set up test data
        Groups.groups = new java.util.concurrent.CopyOnWriteArrayList<Goods>();
        Goods goods1 = new Goods(10, 1, 20, "Item 1");
        Goods goods2 = new Goods(15, 2, 30, "Item 2");
        Groups.groups.add(goods1);
        Groups.groups.add(goods2);

        // Set up a queue to store received messages
        Statics.resMessages = new ArrayBlockingQueue<byte[]>(2);

        // Create a test message
        byte[] messageBytes = generateRandomPacket();
        Packet packet = new Packet(messageBytes);

        byte[] messageBytes2 = generateRandomPacket();
        Packet packet2 = new Packet(messageBytes2);

        Statics.resMessages.put(packet.serialize());
        Statics.resMessages.put(packet2.serialize());

        DraftReceiver receiver = new DraftReceiver(packet2);
        receiver.receiveMessage();

        Thread.sleep(1000);
        Statics.service.shutdown();

        //Statics.processor.process(message);

        // Wait for the message to be processed


    }

    private byte[] generateRandomPacket() throws Exception {
        // Generate a random message using the provided MessageGenerator class
        return MessageGenerator.generate();
    }

    private Message deserializeMessage(byte[] messageBytes) throws Exception {
        // Deserialize the message using the Packet class from the Networking package
        Message message = new Message(messageBytes);
        return message;
    }
}
