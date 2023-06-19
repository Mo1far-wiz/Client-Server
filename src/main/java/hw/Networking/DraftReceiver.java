package hw.Networking;

import hw.Encryprion.Decryptor;
import hw.Statics;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DraftReceiver implements Receiver {
    public DraftReceiver(Packet stopPacket) {
        Statics.stopPacket = stopPacket;
    }

    @Override
    public void receiveMessage() throws Exception {
        while(true){
            byte[] msg = Statics.resMessages.poll(10, TimeUnit.SECONDS);

            if(msg == null)
            {
                break;
            }

            Packet packet = new Packet(Decryptor.decrypt(msg));
            Message message = packet.getMessage();

            System.out.println("Received : " + message);

            if(new Packet(msg).equals(Statics.stopPacket))
            {
                System.out.println("Stop Packet");
                break;
            }

            Statics.service.submit(()-> {
                try {
                    Statics.processor.process(message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
