package hw.Networking;

import hw.Encryprion.Decryptor;
import hw.Statics;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DraftReceiver implements Receiver {
    public DraftReceiver(Message stopMessage) {
        Statics.stopMessage = stopMessage;
    }

    @Override
    public void receiveMessage() throws Exception {
        while(true){
            byte[] msg = Statics.resMessages.poll(10, TimeUnit.SECONDS);

            if(msg == null)
            {
                break;
            }

            Decryptor.decrypt(msg);
            Packet packet = new Packet(msg);
            Message message = packet.getMessage();

            if(message.equals(Statics.stopMessage))
            {
                break;
            }
            System.out.println("Received : " + message);

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
