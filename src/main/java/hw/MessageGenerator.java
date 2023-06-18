package hw;

import hw.Networking.Message;
import hw.Networking.Packet;

import java.util.Random;

enum cActionType
{
    GET_PRODUCTS_AMOUNT,
    DELETE_PRODUCTS_AMOUNT,
    ADD_PRODUCTS_AMOUNT,
    ADD_GROUP,
    ADD_TITLE,
    GET_PRICE
}
public class MessageGenerator {
    public static byte[] generate() {
        Random random = new Random();
        int command = random.nextInt(cActionType.values().length);
        String commandMsg = (cActionType.values()[command]).toString();
        Message testMessage = new Message(command,1, commandMsg.getBytes());
        long bPktId = random.nextLong();
        Packet packet = new Packet((byte)1, bPktId, testMessage);
        return packet.serialize();
    }
}
