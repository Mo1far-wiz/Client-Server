import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagePOJOTest {

    @Test
    public void testSerialization() throws Exception {
        int cType = 123;
        int bUserId = 456;
        byte[] message = "Hello, world!".getBytes();

        MessagePOJO messagePOJO = new MessagePOJO(cType, bUserId, message);
        byte[] serialized = messagePOJO.serialize();

        // Perform assertions
        ByteBuffer buffer = ByteBuffer.wrap(serialized);
        int serializedCType = buffer.getInt();
        int serializedBUserId = buffer.getInt();

        assertEquals(cType, serializedCType);
        assertEquals(bUserId, serializedBUserId);
        assertArrayEquals(message, messagePOJO.getDecryptedMessage());
    }

    @Test
    public void testDeserialization() throws Exception {
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
    public void testChecksum() throws Exception {
        int cType = 123;
        int bUserId = 456;
        byte[] message = "Hello, world!".getBytes(StandardCharsets.UTF_8);

        MessagePOJO messagePOJO = new MessagePOJO(cType, bUserId, message);
        short checksum = messagePOJO.CheckSum();

        // Perform assertions
        // TODO: Add assertions for the expected checksum value based on the specific CRC16 implementation
        assertEquals(CRC16.calculate(messagePOJO.serialize()), checksum);
    }

    @Test
    public void testLength() throws Exception {
        int cType = 123;
        int bUserId = 456;
        byte[] message = "Hello, world!".getBytes(StandardCharsets.UTF_8);

        MessagePOJO messagePOJO = new MessagePOJO(cType, bUserId, message);
        int length = messagePOJO.Length();

        // Perform assertions
        int expectedLength = MessagePOJO.HEADER_OFFSET + messagePOJO.getMessage().length;
        assertEquals(expectedLength, length);
    }

    @Test
    public void testToString() throws Exception {
        int cType = 123;
        int bUserId = 456;
        byte[] message = "Patron (Ukrainian: Патрон, pronounced [pɐˈtrɔn]; lit. 'cartridge'; born 20 July 2019[1]) is a detection dog and mascot for the State Emergency Service of Ukraine.[2][3] He is a Jack Russell Terrier.".getBytes(StandardCharsets.UTF_8);

        MessagePOJO messagePOJO = new MessagePOJO(cType, bUserId, message);
        String toString = messagePOJO.toString();

        System.out.println(toString);
        // Perform assertions
        // TODO: Add assertions for the expected string representation based on the specific format
    }
}
