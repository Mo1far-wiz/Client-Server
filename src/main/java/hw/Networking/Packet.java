package hw.Networking;

import hw.MessageGenerator;
import hw.Networking.Message;
import hw.crc16;

import java.nio.ByteBuffer;
import static hw.Statics.*;

public class Packet {
    byte bMagic;
    byte bSrc;
    long bPktId;
    int wLen;
    short wCrc16Header;
    byte[] bMsg;
    short wCrc16Msg;
    hw.Networking.Message Message;
    public Packet(byte bSrc, long bPktId, hw.Networking.Message Message) {
        bMagic = PACKET_MAGIC;
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.wLen = Message.Length();
        wCrc16Header = CheckSum();
        this.bMsg = Message.serialize();
        this.Message = Message;
        wCrc16Msg = this.Message.CheckSum();
    }
    public Packet(byte bSrc, long bPktId, byte[] Message) throws Exception {
        bMagic = PACKET_MAGIC;
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.wLen = Message.length;
        wCrc16Header = CheckSum();
        this.bMsg = Message;
        this.Message = new Message(Message);
        wCrc16Msg = this.Message.CheckSum();
    }
    public Packet(byte[] arr) throws Exception {
        if(arr.length < 18)
        {
            throw new ExceptionInInitializerError("Invalid Header length");
        }

        ByteBuffer Buffer = ByteBuffer.wrap(arr);

        bMagic          = Buffer.get();
        if(bMagic != PACKET_MAGIC)
        {
            throw new ExceptionInInitializerError("Wrong Magic");
        }
        bSrc            = Buffer.get();
        bPktId          = Buffer.getLong();
        wLen            = Buffer.getInt();
        wCrc16Header    = Buffer.getShort();
        bMsg            = new byte[wLen];
        Buffer          .get(bMsg, 0, wLen);
        wCrc16Msg       = Buffer.getShort();
        Message         = new Message(bMsg);

        if(wCrc16Header != CheckSum())
        {
            //throw new ExceptionInInitializerError("Wrong CheckSum");
        }
        else if(wCrc16Msg != Message.CheckSum())
        {
            //throw new ExceptionInInitializerError("Wrong CheckSum");
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
        return crc16.calculate(HeaderBuffer.array());
    }

    public Message getMessage()
    {
        return Message;
    }
}
