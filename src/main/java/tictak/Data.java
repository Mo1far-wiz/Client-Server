package tictak;

public class Data {
    private int state = 1;

    public synchronized void Tic()
    {
        while (state != 1)
        {
            try
            {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("Tic-");
        state = 2;
        notify();
    }

    public synchronized void Tak()
    {
        while (state != 2)
        {
            try
            {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Tak");
        state = 1;
        notify();
    }
}
