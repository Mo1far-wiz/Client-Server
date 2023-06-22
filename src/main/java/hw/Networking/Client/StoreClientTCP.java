package hw.Networking.Client;

import hw.Encryprion.Decryptor;
import hw.Encryprion.Encryptor;
import hw.Networking.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public class StoreClientTCP {
    private Socket ClientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    private int FailedConnections = 0;

    public void InitConnection(int port) {
        try
        {
            String Address = "127.0.0.1";
            ClientSocket = new Socket(Address, port);
            out = new DataOutputStream(ClientSocket.getOutputStream());
            in = new DataInputStream(ClientSocket.getInputStream());
        }
        catch (IOException ex)
        {
            System.out.println("Failed tries to connect : " + FailedConnections);

            FailedConnections++;
            if(FailedConnections > 10) {
                throw new RuntimeException("Connection failed");
            }
            InitConnection(port);
        }
    }

    public Message SendMessage(Message Message) throws Exception {
        byte[] enc = Encryptor.encrypt(Message);
        out.writeInt(enc.length);
        out.write(enc);

        int MessageLength = in.readInt();
        byte[] m = new byte[MessageLength];
        in.readFully(m, 0, m.length);
        return new Message(Objects.requireNonNull(Decryptor.decrypt(m)));
    }

    public void StopConnection() {
        try {
            out.writeInt(0);
            in.close();
            out.close();
            ClientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


