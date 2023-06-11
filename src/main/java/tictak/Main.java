package tictak;

public class Main {
    public static void main(String[] args) throws InterruptedException
    {

        Data    d = new Data();

        Worker w1=new Worker(1, d);
        Worker w2=new Worker(2, d);

        w2.join();
        w1.join();

        System.out.println("\nend of mian...");
    }
}
