package hw;

import hw.Encryprion.Encryptor;
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
        try {
            Random random = new Random();
            int command = random.nextInt(cActionType.values().length);
            String commandMsg = (cActionType.values()[command]).toString();
            Message testMessage = new Message(command, 1, commandMsg.getBytes());
            long bPktId = random.nextLong();
            Packet packet = new Packet((byte) 0x1, bPktId, Encryptor.encrypt(testMessage));
            return packet.serialize();
        } catch (Exception e) {
            // Handle the exception or log an appropriate error message
            e.printStackTrace();
            return null;  // Return null or an appropriate value indicating failure
        }
    }

}
