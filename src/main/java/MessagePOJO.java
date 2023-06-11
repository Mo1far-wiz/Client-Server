import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class MessagePOJO {
    public final static short HEADER_OFFSET = 8;
    private static final String ALGORITHM = "AES";
    private final SecretKey secretKey;
    private int cType;
    private int bUserId;
    private byte[] Message;

    public MessagePOJO() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128, new SecureRandom());
        secretKey = keyGenerator.generateKey();
    }
    public MessagePOJO(int cType, int bUserId, byte[] Msg) throws Exception {
        this();

        this.cType = cType;
        this.bUserId = bUserId;
        this.Message = encrypt(Msg);
    }
    public MessagePOJO (byte[] msg) throws Exception {
        this();

        if(msg.length < HEADER_OFFSET)
        {
            throw new ExceptionInInitializerError("Invalid Header length");
        }

        ByteBuffer Buffer = ByteBuffer.wrap(msg);
        cType   = Buffer.getInt();
        bUserId = Buffer.getInt();
        Message = new byte[msg.length - HEADER_OFFSET];
        Buffer.get(Message);
    }

    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_OFFSET + Message.length);
        buffer.putInt(cType);
        buffer.putInt(bUserId);
        buffer.put(Message);

        return buffer.array();
    }

    public byte[] encrypt(byte[] message) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(message);
    }

    public byte[] decrypt(byte[] encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(encryptedMessage);
    }

    public short CheckSum() {
        return CRC16.calculate(serialize());
    }

    public int Length()
    {
        return HEADER_OFFSET + Message.length;
    }

    @Override
    public String toString()
    {
        try {
            return "{\n\tcType\t:\t" + cType + "\n\t"
                    + "bUserId\t:\t" + bUserId + "\n\t"
                    + "Message\t:\t" + StandardCharsets.UTF_8.decode(ByteBuffer.wrap(decrypt(Message))) + "\n}";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getCType() {
        return cType;
    }

    public int getBUserId() {
        return bUserId;
    }

    public byte[] getMessage() {
        return Message;
    }

    public byte[] getDecryptedMessage() throws Exception {
        return decrypt(Message);
    }
}
