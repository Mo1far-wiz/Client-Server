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
        Statics.resMessages = new ArrayBlockingQueue<>(10);

        // Create a test message
        byte[] messageBytes = generateRandomMessage();
        Message message = deserializeMessage(messageBytes);

        DraftReceiver receiver = new DraftReceiver(message);
        receiver.receiveMessage();
        // Simulate receiving a message
        //Statics.sender.receiveMessage(message.serialize(), new InetSocketAddress(1488).getAddress());

        // Process the received message
        Statics.processor.process(message);

        // Wait for the message to be processed
        Thread.sleep(100);

        // Check if the response message is available in the resMessages queue
        byte[] responseBytes = Statics.resMessages.poll();
        assertNotNull(responseBytes);

        // Deserialize the response message
        Message response = deserializeMessage(responseBytes);

        // Assert the correctness of the response
        // Add your own assertions based on the expected behavior of the processed message
        assertEquals("Ok!", response.getStringMessage());
    }

    private byte[] generateRandomMessage() {
        // Generate a random message using the provided MessageGenerator class
        return MessageGenerator.generate();
    }

    private Message deserializeMessage(byte[] messageBytes) throws Exception {
        // Deserialize the message using the Packet class from the Networking package
        Packet packet = new Packet(messageBytes);
        return packet.getMessage();
    }
}
