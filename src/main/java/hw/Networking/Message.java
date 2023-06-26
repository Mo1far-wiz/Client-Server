package hw.Networking;

import hw.Statics.crc16;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class Message {
    public final static short HEADER_OFFSET = 8;
    private int cType;
    private int bUserId;
    private byte[] Message;

    public Message(int cType, int bUserId, byte[] Msg)  {
        this.cType = cType;
        this.bUserId = bUserId;
        this.Message = Msg;
    }
    public Message(byte[] msg) throws Exception {
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
    public short CheckSum() {
        return crc16.calculate(serialize());
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
                    + "Message\t:\t" + StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Message)) + "\n}";
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

    public String getStringMessage() {
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Message)).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Message message1 = (Message) o;
        return cType == message1.cType && bUserId == message1.bUserId && Arrays.equals(Message, message1.getMessage());
    }
}
