package hw.Shop;

import hw.Statics.Statics;

import java.nio.ByteBuffer;

public class Product {
    private int id;
    private int amount;
    private String category;
    private double price;
    private String title;

    public Product(int amount, String category, double price, String title)
    {
        this.amount = amount;
        this.category = category;
        this.price = price;
        this.title = title;
        id = Statics.ID++;
    }

    public Product(String title, double price, int amount, String category)
    {
        this.amount = amount;
        this.category = category;
        this.price = price;
        this.title = title;
        id = Statics.ID++;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }
    public void setPrice(double price)
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
    public String getCategory()
    {
        return category;
    }
    public double getPrice()
    {
        return price;
    }
    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
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
        return ByteBuffer.allocate(4 + 4 + category.getBytes().length)
                .putInt(amount)
                .putDouble(price)
                .put(category.getBytes())
                .array();
    }

    @Override
    public String toString() {
        return "Goods{" +
                "amount=" + amount +
                ", type=" + category +
                ", price=" + price +
                '}';
    }
}
