package org.arsen.cee.easy;

import java.io.*;

public class WordToDigit {

    enum Numbers {
        ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9);

        private int intValue;

        Numbers(int intValue) {
            this.intValue = intValue;
        }

        public int getIntValue() {
            return intValue;
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                StringBuffer sb = new StringBuffer();
                String[] numbers = line.split(";");

                for (String number : numbers) {
                    Numbers num = Numbers.valueOf(number.toUpperCase());
                    sb.append(num.getIntValue());
                }

                System.out.println(sb.toString());
            }
        }
    }
}
