package hw;

import java.net.InetAddress;

public interface Sender {
    public void sendMessage(byte[] message, InetAddress target);
}
