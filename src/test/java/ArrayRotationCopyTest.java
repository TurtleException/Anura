import java.util.Arrays;

public class ArrayRotationCopyTest {
    public static void main(String[] args) {
        long[] arr1 = {0, 1, 2, 3, 4};
        long[] arr2 = new long[arr1.length];

        print(arr1, arr2);

        System.out.println("###");
        System.arraycopy(arr1, 0, arr2, 0, arr1.length);

        print(arr1, arr2);
    }

    private static void print(long[]... arr) {
        for (long[] longs : arr) {
            System.out.println(Arrays.toString(longs));
        }
    }
}
