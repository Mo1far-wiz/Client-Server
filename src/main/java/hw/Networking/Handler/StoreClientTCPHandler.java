package hw.Networking.Handler;

import hw.Encryprion.Decryptor;
import hw.Encryprion.Encryptor;
import hw.Networking.Client.StoreClientTCP;
import hw.Networking.Message;
import hw.Networking.Packet;
import hw.Statics;

import javax.swing.plaf.TableHeaderUI;
import java.io.*;
import java.net.Socket;

public class StoreClientTCPHandler extends Thread {
    private Socket ClientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public StoreClientTCPHandler(Socket ClientSocket)
    {
        this.ClientSocket = ClientSocket;
    }

    @Override
    public void run()
    {
        try {
            out = new DataOutputStream(ClientSocket.getOutputStream());
            in = new DataInputStream(ClientSocket.getInputStream());

            while (true)
            {
                int MessageLength = in.readInt();
                if(MessageLength <= 0)
                {
                    break;
                }

                byte[] ByteMessage = new byte[MessageLength];
                in.readFully(ByteMessage, 0, MessageLength);

                //Packet Packet = new Packet(Decryptor.decrypt(ByteMessage));
                Message Message = new Message(Decryptor.decrypt(ByteMessage));

                System.out.println("Handled : " + Message);
                Message end = Statics.processor.fakeProcess(Message);
                byte[] serialized = Encryptor.encrypt(end);

                out.writeInt(serialized.length);
                out.write(serialized);
            }

            in.close();
            out.close();
            // new String(ch).getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
