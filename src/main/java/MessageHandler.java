import java.nio.ByteBuffer;

public class MessageHandler {
    public static final byte HANDLER_MAGIC = 0x13;

    byte bMagic;
    byte bSrc;
    long bPktId;
    int wLen;
    short wCrc16Header;
    byte[] bMsg;
    short wCrc16Msg;
    MessagePOJO Message;
    public MessageHandler(byte bSrc, long bPktId, MessagePOJO Message)
    {
        bMagic = HANDLER_MAGIC;
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.wLen = Message.Length();
        wCrc16Header = CheckSum();
        this.bMsg = Message.serialize();
        this.Message = Message;
        wCrc16Msg = this.Message.CheckSum();
    }
    public MessageHandler(byte[] arr)
    {
        if(arr.length < 18)
        {
            throw new ExceptionInInitializerError("Invalid Header length");
        }

        ByteBuffer Buffer = ByteBuffer.wrap(arr);

        bMagic      = Buffer.get();
        if(bMagic != HANDLER_MAGIC)
        {
            throw new ExceptionInInitializerError("Wrong Magic");
        }
        bSrc        = Buffer.get();
        bPktId      = Buffer.getLong();
        wLen        = Buffer.getInt();
        wCrc16Header = Buffer.getShort();
        bMsg        = new byte[wLen];
        Buffer      .get(bMsg, 0, wLen);
        wCrc16Msg   = Buffer.getShort();
        Message = new MessagePOJO(bMsg);

        if(wCrc16Header != CheckSum())
        {
            throw new ExceptionInInitializerError("Wrong CheckSum");
        }
        else if(wCrc16Msg != Message.CheckSum())
        {
            throw new ExceptionInInitializerError("Wrong CheckSum");
        }
    }

    public byte[] serialize() {
        ByteBuffer Buffer = ByteBuffer.allocate(18 + wLen);
        Buffer.put(bMagic);
        Buffer.put(bSrc);
        Buffer.putLong(bPktId);
        Buffer.putInt(wLen);
        Buffer.putShort(wCrc16Header);
        Buffer.put(bMsg);
        Buffer.putShort(wCrc16Msg);
        return Buffer.array();
    }
    private short CheckSum()
    {
        ByteBuffer HeaderBuffer = ByteBuffer.allocate(16);
        HeaderBuffer.put(bMagic);
        HeaderBuffer.put(bSrc);
        HeaderBuffer.putLong(bPktId);
        HeaderBuffer.putInt(wLen);
        return CRC16.calculate(HeaderBuffer.array());
    }
}
