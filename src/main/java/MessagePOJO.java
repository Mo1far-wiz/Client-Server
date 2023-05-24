import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class MessagePOJO {
    public final static short HEADER_OFFSET = 8;
    private int cType;
    private int bUserId;
    private byte[] Message;

    public MessagePOJO(int cType, int bUserId, byte[] Msg) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.Message = Msg;
    }
    public MessagePOJO (byte[] msg) {
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

    public short CheckSum()
    {
        return CRC16.calculate(serialize());
    }

    public int Length()
    {
        return HEADER_OFFSET + Message.length;
    }

    @Override
    public String toString()
    {
        return "{\n\tcType\t:\t" + cType + "\n\t"
                + "bUserId\t:\t" + bUserId + "\n\t"
                + "Message\t:\t" + StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Message)).toString() + "\n}";
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
}
