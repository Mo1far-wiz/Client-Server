package hw.Statics;

public class Main {
    public static void main(String[] Args)
    {
        byte[] arr = {0x13, 0x14, 0x16};

        transform(arr);

        for(int q = 0; q < arr.length; ++q)
        {
            System.out.println(arr[q]);
        }
    }

    public static void transform(byte[] arr) {
        arr = new byte[]{0x1, 0x2, 0x3, 0x4};
    }
}
