import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagePOJOTest {

    @Test
    public void testSerialization() {
        int cType = 123;
        int bUserId = 456;
        byte[] message = "Hello, world!".getBytes();

        MessagePOJO messagePOJO = new MessagePOJO(cType, bUserId, message);
        byte[] serialized = messagePOJO.serialize();

        // Perform assertions
        ByteBuffer buffer = ByteBuffer.wrap(serialized);
        int serializedCType = buffer.getInt();
        int serializedBUserId = buffer.getInt();
        byte[] serializedMessage = new byte[serialized.length - MessagePOJO.HEADER_OFFSET];
        buffer.get(serializedMessage);

        assertEquals(cType, serializedCType);
        assertEquals(bUserId, serializedBUserId);
        assertArrayEquals(message, serializedMessage);
    }

    @Test
    public void testDeserialization() {
        int cType = 123;
        int bUserId = 456;
        byte[] message = "Hello, world!".getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(MessagePOJO.HEADER_OFFSET + message.length);
        buffer.putInt(cType);
        buffer.putInt(bUserId);
        buffer.put(message);

        MessagePOJO messagePOJO = new MessagePOJO(buffer.array());

        // Perform assertions
        assertEquals(cType, messagePOJO.getCType());
        assertEquals(bUserId, messagePOJO.getBUserId());
        assertArrayEquals(message, messagePOJO.getMessage());
    }

    @Test
    public void testChecksum() {
        int cType = 123;
        int bUserId = 456;
        byte[] message = "Hello, world!".getBytes(StandardCharsets.UTF_8);

        MessagePOJO messagePOJO = new MessagePOJO(cType, bUserId, message);
        short checksum = messagePOJO.CheckSum();

        // Perform assertions
        // TODO: Add assertions for the expected checksum value based on the specific CRC16 implementation
        assertEquals(CRC16.calculate(messagePOJO.serialize()), messagePOJO.CheckSum());
    }

    @Test
    public void testLength() {
        int cType = 123;
        int bUserId = 456;
        byte[] message = "Hello, world!".getBytes(StandardCharsets.UTF_8);

        MessagePOJO messagePOJO = new MessagePOJO(cType, bUserId, message);
        int length = messagePOJO.Length();

        // Perform assertions
        int expectedLength = MessagePOJO.HEADER_OFFSET + message.length;
        assertEquals(expectedLength, length);
    }

    @Test
    public void testToString() {
        int cType = 123;
        int bUserId = 456;
        byte[] message = "Hello, world!".getBytes(StandardCharsets.UTF_8);

        MessagePOJO messagePOJO = new MessagePOJO(cType, bUserId, message);
        String toString = messagePOJO.toString();

        // Perform assertions
        // TODO: Add assertions for the expected string representation based on the specific format
    }
}
