import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageHandlerTest {

    @Test
    public void testSerialization() {
        byte bSrc = 1;
        long bPktId = 123456789L;
        byte[] messageData = "Hello, world!".getBytes();

        MessagePOJO messagePOJO = new MessagePOJO(123, 456, messageData);
        MessageHandler messageHandler = new MessageHandler(bSrc, bPktId, messagePOJO);
        byte[] serialized = messageHandler.serialize();

        // Perform assertions
        ByteBuffer buffer = ByteBuffer.wrap(serialized);
        byte serializedMagic = buffer.get();
        byte serializedSrc = buffer.get();
        long serializedPktId = buffer.getLong();
        int serializedLen = buffer.getInt();
        short serializedCrc16Header = buffer.getShort();
        byte[] serializedMsg = new byte[serializedLen];
        buffer.get(serializedMsg);
        short serializedCrc16Msg = buffer.getShort();

        assertEquals(MessageHandler.HANDLER_MAGIC, serializedMagic);
        assertEquals(bSrc, serializedSrc);
        assertEquals(bPktId, serializedPktId);
        assertEquals(messageData.length, serializedLen - messagePOJO.HEADER_OFFSET);
        // TODO: Add assertions for the checksum values and the serialized message contents
    }

    @Test
    public void testDeserialization() {
        byte bSrc = 1;
        long bPktId = 123456789L;
        byte[] messageData = "Hello, world!".getBytes();

        ByteBuffer buffer = ByteBuffer.allocate(18 + 8 + messageData.length);
        ByteBuffer CheckSumbuffer = ByteBuffer.allocate(16);
        buffer.put(MessageHandler.HANDLER_MAGIC);
        buffer.put(bSrc);
        buffer.putLong(bPktId);
        buffer.putInt(messageData.length + 8);

        CheckSumbuffer.put(MessageHandler.HANDLER_MAGIC);
        CheckSumbuffer.put(bSrc);
        CheckSumbuffer.putLong(bPktId);
        CheckSumbuffer.putInt(messageData.length + 8);
        short CheckSum = CRC16.calculate(CheckSumbuffer.array());

        buffer.putShort(CheckSum); // Placeholder for CRC16 header

        MessagePOJO messagePOJO = new MessagePOJO(123, 456, messageData);
        buffer.put(messagePOJO.serialize());
        buffer.putShort(messagePOJO.CheckSum()); // Placeholder for CRC16 message

        byte[] arr = buffer.array();
        MessageHandler messageHandler = new MessageHandler(arr);
        System.out.println(messagePOJO);
        // Perform assertions
        assertEquals(MessageHandler.HANDLER_MAGIC, messageHandler.bMagic);
        assertEquals(bSrc, messageHandler.bSrc);
        assertEquals(bPktId, messageHandler.bPktId);
        assertEquals(messageData.length + 8, messageHandler.wLen);
        // TODO: Add assertions for the checksum values and the deserialized message contents
    }
}
