package hw.Networking;

import java.net.InetAddress;

public class Sender {
    public void sendMessage(byte[] message, InetAddress target)
    {
        String response = "Send response : Ok!";
        System.out.println(response + " " + target.toString());
    }
}
