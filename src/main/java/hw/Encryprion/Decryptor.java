package hw.Encryprion;

import hw.Networking.Message;
import hw.Networking.Packet;
import hw.Statics;
import hw.crc16;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

import static hw.Statics.*;

public class Decryptor {
    public static byte[] decryptPacket(byte[] message) {
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

            byte[] msg = decrypt(bMsg);


            byte[] header = ByteBuffer.allocate(14)
                    .put(bMagic)
                    .put(bSrc)
                    .putLong(bPktId)
                    .putInt(msg.length)
                    .array();

            byte[] packet = ByteBuffer.allocate(header.length + 2 + msg.length + 2)
                    .put(header)
                    .putShort(crc16.calculate(header))
                    .put(msg)
                    .putShort(crc16.calculate(msg))
                    .array();

            return packet;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] message) {
        try {
            if(message.length == 0)
            {
                return null;
            }
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);

            ByteBuffer Buffer = ByteBuffer.wrap(message);
            int cType = Buffer.getInt();
            int bUserId = Buffer.getInt();
            byte[] encrypted = new byte[Buffer.limit() - Buffer.position()];
            Buffer.get(encrypted);
            byte[] msg = cipher.doFinal(encrypted);

            return ByteBuffer.allocate(Message.HEADER_OFFSET + msg.length)
                    .putInt(cType)
                    .putInt(bUserId)
                    .put(msg).array();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
