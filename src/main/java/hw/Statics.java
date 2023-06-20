package hw;

import hw.Networking.Packet;
import hw.Networking.Processor;
import hw.Networking.Sender;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Statics {
    public static final byte PACKET_MAGIC = 0x13;
    public static final Key SECRET_KEY = getSecureRandomKey(128);
    public static final String CIPHER = "AES";

    public static Processor processor = new Processor();
    public static Sender sender = new Sender();


    public static ExecutorService receiveService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 4);
    public static ExecutorService responseService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 4);
    public static ArrayBlockingQueue<byte[]> resMessages;
    public static Packet stopPacket;

    private static Key getSecureRandomKey(int keySize) {
        byte[] secureRandomKeyBytes = new byte[keySize / 8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secureRandomKeyBytes);
        return new SecretKeySpec(secureRandomKeyBytes, CIPHER);
    }
}
