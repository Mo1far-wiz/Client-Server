package hw.Shop;

import java.util.concurrent.*;

public class Groups {
    public static CopyOnWriteArrayList<Goods> groups;

    public Groups() {
    }
    public Groups(CopyOnWriteArrayList<Goods> groups) {
        this.groups = groups;
    }

    public void addGoods(Goods goods) {
        groups.add(goods);
    }

    public Goods getGoods(int idx) {
        return groups.get(idx);
    }
}
