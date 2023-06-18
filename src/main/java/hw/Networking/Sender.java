package hw.Networking;

import java.net.InetAddress;

public class Sender {
    public void sendMessage(byte[] message, InetAddress target) throws Exception {
        Message msg = new Message(message);
        String response = "Send response : Ok! " + msg.getStringMessage();
        System.out.println(response + " " + target.toString());
    }
}
