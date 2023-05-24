import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args)
    {
        byte[] str = "hihi haha".getBytes();
        byte[] msg = new byte[8 + str.length];
        byte[] arr = new byte[18 + msg.length];
        ByteBuffer a = ByteBuffer.wrap(arr);
        a.put(0, new byte[]{1});
        a.put(16, msg);
        a.put(24, str);
        a.putInt(10, msg.length);
        MessageHandler h = new MessageHandler(a.array());
    }
}
