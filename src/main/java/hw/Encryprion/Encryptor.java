package hw.Encryprion;

import hw.Networking.Message;
import hw.crc16;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

import static hw.Statics.*;

public class Encryptor {
    public static byte[] encrypt(Message command) {
        try {
            byte[] message = encryptMessage(command);
            byte[] header = ByteBuffer.allocate(14)
                    .put(PACKET_MAGIC)
                    .put((byte) 14)
                    .putLong(88L)
                    .putInt(message.length)
                    .array();

            return ByteBuffer.allocate(header.length + 2 + message.length + 2)
                    .put(header)
                    .putShort(crc16.calculate(header))
                    .put(message)
                    .putShort(crc16.calculate(message))
                    .array();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] encryptMessage(Message message) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            //message.doAction();
            byte[] msg = cipher.doFinal(message.getMessage());
            return ByteBuffer.allocate(Message.HEADER_OFFSET + msg.length)
                    .putInt(message.getCType())
                    .putInt(message.getBUserId())
                    .put(msg).array();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
