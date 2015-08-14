/**
 * Created by arsen on 11.08.15.
 */
public class SumOfPrimes {
    public static boolean isPrime(int number) {
        if (number % 2 == 0) {
            return false;
        }

        int limit = number / 2;

        for (int i = 3; i < limit; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int counter = 0;
        int num = 3;
        long sum = 2;

        for (; counter < 999 ; num++) {
            if (isPrime(num)) {
                sum += num;
                counter++;
            }
        }

        System.out.println(sum);
    }
}
