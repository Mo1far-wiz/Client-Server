import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageHandlerTest {

    @Test
    public void testSerialization() throws Exception {
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
        assertEquals(messagePOJO.getMessage().length + messagePOJO.HEADER_OFFSET, serializedLen);
        // TODO: Add assertions for the checksum values and the serialized message contents
    }

    @Test
    public void testDeserialization() throws Exception {
        byte bSrc = 1;
        long bPktId = 123456789L;
        byte[] messageData = "Patron first came to prominence during the 2022 Russian invasion of Ukraine, during which Ukrainian president Volodymyr Zelenskyy awarded him the Order for Courage for his work in locating and defusing unexploded ordnance left behind by Russian troops.[4] As of 8 May 2022, Patron has found 236 such devices.".getBytes();

        MessagePOJO messagePOJO = new MessagePOJO(123, 456, messageData);

        ByteBuffer buffer = ByteBuffer.allocate(18 + 8 + messagePOJO.getMessage().length);
        ByteBuffer CheckSumBuffer = ByteBuffer.allocate(16);
        buffer.put(MessageHandler.HANDLER_MAGIC);
        buffer.put(bSrc);
        buffer.putLong(bPktId);
        buffer.putInt(messagePOJO.getMessage().length + 8);

        CheckSumBuffer.put(MessageHandler.HANDLER_MAGIC);
        CheckSumBuffer.put(bSrc);
        CheckSumBuffer.putLong(bPktId);
        CheckSumBuffer.putInt(messagePOJO.getMessage().length + 8);
        short CheckSum = CRC16.calculate(CheckSumBuffer.array());

        buffer.putShort(CheckSum); // Placeholder for CRC16 header

        buffer.put(messagePOJO.serialize());
        buffer.putShort(messagePOJO.CheckSum()); // Placeholder for CRC16 message
        byte[] arr = buffer.array();
        MessageHandler messageHandler = new MessageHandler(arr);
        System.out.println(messagePOJO);
        // Perform assertions
        assertEquals(MessageHandler.HANDLER_MAGIC, messageHandler.bMagic);
        assertEquals(bSrc, messageHandler.bSrc);
        assertEquals(bPktId, messageHandler.bPktId);
        assertEquals(messagePOJO.getMessage().length + 8, messageHandler.wLen);
        assertEquals(messagePOJO.getDecryptedMessage().length, messageData.length);
        // TODO: Add assertions for the checksum values and the deserialized message contents
    }
}
