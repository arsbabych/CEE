import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by arsen on 13.08.15.
 */
public class ReverseAndAdd {

    public static int iteration = 1;

    public static int reverse(int number) {
        List<Integer> list = new LinkedList();
        int result = 0;

        while (true) {
            list.add(number % 10);
            number /= 10;

            if (number == 0) {
                break;
            }
        }

        for (int i = 0; i < list.size(); i++) {
            result += list.get(i) * Math.pow(10, list.size() - i - 1);
        }

        return result;
    }

    public static boolean isPalindrom(int number) {
        if (number == reverse(number)) {
            return true;
        }

        return false;
    }

    public static void findPalindrom(int number) {
        if (isPalindrom(number)) {
            System.out.println(iteration + " " + number);
            iteration = 1;
        } else {
            iteration++;
            findPalindrom(number + reverse(number));
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        int number, summ;

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                number = Integer.parseInt(line.trim());
                summ = number + reverse(number);

                findPalindrom(summ);
            }
        }
    }
}
