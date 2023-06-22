package hw.Networking.Client;

import hw.Encryprion.Decryptor;
import hw.Encryprion.Encryptor;
import hw.Networking.Message;
import hw.Statics;

import java.io.IOException;
import java.net.*;

import static hw.Encryprion.Encryptor.encrypt;

public class StoreClientUDP{
    private DatagramSocket ClientSocket;
    private InetAddress Address;

    private byte[] Buffer;

    public StoreClientUDP(DatagramSocket ClientSocket) throws SocketException, UnknownHostException {
        this.ClientSocket = ClientSocket;
        ClientSocket.setSoTimeout(6969);
        Address = InetAddress.getByName("localhost");
    }
    public Message SendMessage(Message msg) throws Exception {
        Buffer = encrypt(msg);

        DatagramPacket packet = new DatagramPacket(Buffer, Buffer.length, Address, 6666);
        ClientSocket.send(packet);
        packet = new DatagramPacket(Buffer, Buffer.length);
        ClientSocket.receive(packet);

        byte[] ByteMessage = Decryptor.decrypt(packet.getData());

        if(ByteMessage == null){
            System.out.println("Retry...");
            SendMessage(msg);
        }

        return Statics.processor.fakeProcess(new Message(ByteMessage));
    }

    public void close() {
        ClientSocket.close();
    }
}
