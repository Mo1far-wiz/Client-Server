package hw.Shop;

import java.nio.ByteBuffer;

public class Goods {
    private int amount;
    private int type;
    private int price;
    private String title;

    public Goods(int amount, int type, int price, String title)
    {
        this.amount = amount;
        this.type = type;
        this.price = price;
        this.title = title;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }
    public void setType(int type)
    {
        this.type = type;
    }
    public void setPrice(int price)
    {
        this.price = price;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getAmount()
    {
        return amount;
    }
    public int getType()
    {
        return type;
    }
    public int getPrice()
    {
        return price;
    }

    public void addGoods(int amount)
    {
        if(amount > 0)
        {
            this.amount += amount;
        }
    }
    public void deleteGoods(int amount)
    {
        if(amount > 0)
        {
            this.amount -= amount;
        }
    }

    public byte[] toBytes(){
        return ByteBuffer.allocate(4 + 4 + 4)
                .putInt(amount)
                .putInt(type)
                .putInt(price)
                .array();
    }

    @Override
    public String toString() {
        return "Goods{" +
                "amount=" + amount +
                ", type=" + type +
                ", price=" + price +
                '}';
    }
}
