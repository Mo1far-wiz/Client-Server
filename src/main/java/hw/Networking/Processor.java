package hw.Networking;

import hw.Shop.Goods;
import hw.Shop.Groups;
import hw.Statics;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Processor implements Runnable{
    public void process(Message message) throws InterruptedException {
        String[] command = message.getStringMessage().split(" ");
        int type = Integer.parseInt(command[1]);

        switch (command[0])
        {
            case    "GET_PRODUCTS_AMOUNT":
            {
                for (Goods group : Groups.groups) {
                    if(group.getType() == type)
                    {
                        System.out.println(group.getAmount());
                    }
                }

                break;
            }
            case    "DELETE_PRODUCTS_AMOUNT" :
            {
                int amount = Integer.parseInt(command[2]);
                for (Goods group : Groups.groups) {
                    if(group.getType() == type)
                    {
                        group.deleteGoods(amount);
                    }
                }

                break;
            }
            case    "ADD_PRODUCTS_AMOUNT" :
            {
                int amount = Integer.parseInt(command[2]);
                for (Goods group : Groups.groups) {
                    if(group.getType() == type)
                    {
                        group.addGoods(amount);
                    }
                }
                break;
            }
            case    "ADD_GROUP" :
            {
                for (Goods group : Groups.groups) {
                    if(group.getType() == type)
                    {
                        break;
                    }
                }

                Groups.groups.put(new Goods(0, type, 0, "New Group"));
                break;
            }
            case    "ADD_TITLE" :
            {

                break;
            }
            case    "GET_PRICE" :
            {
                for (Goods group : Groups.groups) {
                    if(group.getType() == type)
                    {
                        System.out.println(group.getPrice());
                    }
                }
                break;
            }
        }

        Statics.sender.sendMessage(message.serialize(), new InetSocketAddress(1488).getAddress());
    }

    @Override
    public void run() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
