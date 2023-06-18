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

            Packet packet = new Packet(msg);
            Message oldMessage = packet.getMessage();

            msg = Decryptor.decrypt(msg);
            packet = new Packet(msg);
            Message message = packet.getMessage();

            System.out.println("Received : " + message);

            Message finalMessage = message;
            Statics.service.submit(()-> {
                try {
                    Statics.processor.process(finalMessage);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            if(oldMessage.equals(Statics.stopMessage))
            {
                System.out.println("Stop Message");
                break;
            }
        }
    }
}
