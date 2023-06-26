package hw.Shop;

import java.util.concurrent.*;

public class Groups {
    public static CopyOnWriteArrayList<Product> groups;

    public Groups() {
    }
    public Groups(CopyOnWriteArrayList<Product> groups) {
        this.groups = groups;
    }

    public void addGoods(Product product) {
        groups.add(product);
    }

    public Product getGoods(int idx) {
        return groups.get(idx);
    }
}
