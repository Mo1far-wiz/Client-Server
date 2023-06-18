package hw.Encryprion;

import hw.Networking.Message;
import hw.crc16;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

import static hw.Statics.*;

public class Decryptor {
    public static byte[] decrypt(byte[] message) {
        try {
            ByteBuffer Buffer = ByteBuffer.wrap(message);

            byte bMagic = Buffer.get();
            byte bSrc = Buffer.get();
            long bPktId = Buffer.getLong();
            int wLen = Buffer.getInt();
            short wCrc16Header = Buffer.getShort();
            byte[] bMsg = new byte[wLen];
            Buffer.get(bMsg, 0, wLen);
            short wCrc16Msg = Buffer.getShort();

            byte[] header = ByteBuffer.allocate(14)
                    .put(bMagic)
                    .put(bSrc).putLong(bPktId)
                    .putInt(wLen)
                    .array();

            if (crc16.calculate(header) != wCrc16Header) {
                //throw new RuntimeException("Invalid crc16");
            }
            if (crc16.calculate(bMsg) != wCrc16Msg) {
                //throw new RuntimeException("Invalid crc16");
            }

            byte[] msg = decryptMessage(new Message(bMsg));

            header = ByteBuffer.wrap(header).putInt(10, msg.length).array();

            byte[] res = ByteBuffer.allocate(header.length + 2 + msg.length + 2)
                    .put(header)
                    .putShort(crc16.calculate(header))
                    .put(msg)
                    .putShort(crc16.calculate(msg))
                    .array();

            return res;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] decryptMessage(Message message) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);

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
