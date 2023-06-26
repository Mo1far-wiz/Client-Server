package hw.Networking;

import hw.Shop.Product;
import hw.Shop.Groups;
import hw.Statics.Statics;

import java.net.InetSocketAddress;

public class Processor {
    public void process(Message message) throws InterruptedException {
        String[] command = message.getStringMessage().split(" ");

        switch (command[0]) {
            case "GET_PRODUCTS_AMOUNT": {

                break;
            }
            case "DELETE_PRODUCTS_AMOUNT": {

                break;
            }
            case "ADD_PRODUCTS_AMOUNT": {
                break;
            }
            case "ADD_GROUP": {
                break;
            }
            case "ADD_TITLE": {

                break;
            }
            case "GET_PRICE": {
                for (Product group : Groups.groups) {
                    {
                        System.out.println(group.getPrice());
                    }
                }
                break;
            }
        }

        //System.out.println(Thread.currentThread().getName() + " Message processed");
        Statics.responseService.submit(() -> {
            try {
                Statics.sender.sendMessage(message.serialize(), new InetSocketAddress(1488).getAddress());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Message fakeProcess (Message message) throws InterruptedException {
        return new Message(message.getCType(), message.getBUserId(), "Processed".getBytes());
    }
}
