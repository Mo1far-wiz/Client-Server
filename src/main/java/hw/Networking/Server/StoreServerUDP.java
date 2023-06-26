package hw.Networking.Server;

import hw.Encryprion.Decryptor;
import hw.Encryprion.Encryptor;
import hw.Networking.Message;
import hw.Statics.Statics;

import java.net.*;
import java.util.Objects;

public class StoreServerUDP extends Thread{
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public StoreServerUDP() throws SocketException {
        socket = new DatagramSocket(6666);
        socket.setSoTimeout(6969);
    }

    public void run() {
        running = true;

        while (running)
        {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try {

                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                byte[] received = packet.getData();
                Message message = new Message(Objects.requireNonNull(Decryptor.decrypt(received)));

                Message processed = Statics.processor.fakeProcess(message);
                byte[] serialized = Encryptor.encrypt(processed);

                packet = new DatagramPacket(serialized, serialized.length, address, port);
                socket.send(packet);
            }
            catch (SocketTimeoutException ex)
            {
                running = false;
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        socket.close();
    }
}
