package tictak;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Data    d = new Data();

        Worker w1=new Worker(0, d);
        Worker w2=new Worker(1, d);
        Worker w3=new Worker(2, d);

        w1.join();
        w2.join();
        w3.join();
        System.out.println("\nend of mian...");
    }
}
