import java.io.*;

/**
 * Created by arsen on 13.08.15.
 */
public class PrimeNumbers {
    public static boolean isPrime(int number) {
        if (number % 2 == 0 && number != 2) {
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

    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        int number;
        String line, result;

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                number = Integer.parseInt(line.trim());
                StringBuffer sb = new StringBuffer();

                for (int counter = 2; counter < number; counter++) {
                    if (isPrime(counter)) {
                        sb.append(counter);
                        sb.append(",");
                    }
                }

                result = sb.toString();
                System.out.println(result.substring(0, result.length() - 1));
            }
        }
    }
}
