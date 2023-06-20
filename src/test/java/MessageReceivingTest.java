import hw.Encryprion.Encryptor;
import hw.MessageGenerator;
import hw.Networking.DraftReceiver;
import hw.Networking.Message;
import hw.Networking.Packet;
import hw.Shop.Goods;
import hw.Shop.Groups;
import hw.Statics;
import org.junit.jupiter.api.Test;

import java.util.Random;
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
        Statics.resMessages = new ArrayBlockingQueue<byte[]>(3);

        // Create a test message
        byte[] messageBytes = generateRandomPacket();
        Packet packet = new Packet(messageBytes);

        byte[] messageBytes2 = generateRandomPacket();
        Packet packet2 = new Packet(messageBytes2);

        byte[] stopMessageBytes = generateStopPacket();
        Packet stopPacket = new Packet(stopMessageBytes);

        Statics.resMessages.put(packet.serialize());
        Statics.resMessages.put(packet2.serialize());
        Statics.resMessages.put(stopPacket.serialize());

        DraftReceiver receiver = new DraftReceiver(packet2);
        receiver.receiveMessage();

        Thread.sleep(1000);
        Statics.receiveService.shutdown();
        Statics.responseService.shutdown();
        //Statics.processor.process(message);

        // Wait for the message to be processed


    }

    private byte[] generateRandomPacket() throws Exception {
        // Generate a random message using the provided MessageGenerator class
        return MessageGenerator.generate();
    }

    private byte[] generateStopPacket() throws Exception {
        Random random = new Random();
        String commandMsg = "STOP";
        Message testMessage = new Message(-1, 1, commandMsg.getBytes());
        long bPktId = random.nextLong();
        Packet packet = new Packet((byte) 0x1, bPktId, Encryptor.encrypt(testMessage));
        return packet.serialize();
    }

    private Message deserializeMessage(byte[] messageBytes) throws Exception {
        // Deserialize the message using the Packet class from the Networking package
        Message message = new Message(messageBytes);
        return message;
    }
}
