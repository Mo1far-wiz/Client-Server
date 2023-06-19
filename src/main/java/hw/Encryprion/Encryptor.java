package hw.Encryprion;

import hw.Networking.Message;
import hw.crc16;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

import static hw.Statics.*;

public class Encryptor {
    public static byte[] encrypt(Message command) {
        try {
            return encryptMessage(command);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] encryptMessage(Message message) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);

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
