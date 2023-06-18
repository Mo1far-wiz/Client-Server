package hw.Shop;

import java.util.concurrent.ArrayBlockingQueue;

public class Groups {
    public static ArrayBlockingQueue<Goods> groups;

    public Groups(ArrayBlockingQueue<Goods> groups) {
        this.groups = groups;
    }

    public void addGoods(Goods goods) {
        groups.offer(goods);
    }

    public Goods getGoods() {
        return groups.poll();
    }
}
