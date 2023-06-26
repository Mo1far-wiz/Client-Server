package hw.Networking;

import hw.Encryprion.Decryptor;
import hw.Statics.Statics;

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

            if(message.getStringMessage().equals("STOP"))
            {
                System.out.println("Stop Packet");
                break;
            }

            Statics.receiveService.submit(()-> {
                try {
                    Statics.processor.process(message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
