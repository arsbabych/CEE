package org.arsen.cee.hard;

import java.io.*;

public class TextDollar {

    public static String convertToDollars(int number) {

        GeneralMillionNumerator generalMillionNumerator = new GeneralMillionNumerator(number);
        String result = generalMillionNumerator.convertToDollars();

        return result;

    }

    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {


                int number = Integer.parseInt(line.trim());

                String result = convertToDollars(number);


                System.out.println(result);
            }
        }
    }
}

interface Numerable {
    StringBuffer numerate(int number);
}

abstract class AbstractNumerator implements Numerable {
    protected int number;
}

class HundredNumerator extends AbstractNumerator {

    protected int third;
    protected int second;
    protected int first;

    public StringBuffer numerate(int number) {
        StringBuffer sb = new StringBuffer();

        this.third = (number - number % 100) / 100;

        if (third != 0) {
            for (SimpleNumbers simpleNumber : SimpleNumbers.values()) {
                if (simpleNumber.getIntValue() == third) {
                    String s = simpleNumber.toString().toLowerCase();
                    sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
                    break;
                }
            }

            String s = Orders.HUNDRED.toString().toLowerCase();
            sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
        }

        return sb;
    }
}

class TennerNumerator extends HundredNumerator {

    @Override
    public StringBuffer numerate(int number) {
        StringBuffer sb = super.numerate(number);

        this.second = ( number % 100 - number % 10 ) / 10;

        for (Tenners tenners : Tenners.values()) {
            if (tenners.getIntValue() == second * 10) {
                String s = tenners.toString().toLowerCase();
                sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
                break;
            }
        }

        return sb;
    }
}

class AfterNineNumerator extends HundredNumerator {

    @Override
    public StringBuffer numerate(int number) {
        StringBuffer sb = super.numerate(number);

        first = number % 10;
        second = (number - first) % 100 / 10;

        for (AfterNineNumbers afterNineNumbers : AfterNineNumbers.values()) {
            if (afterNineNumbers.getIntValue() == this.second * 10 + first) {
                String s = afterNineNumbers.toString().toLowerCase();
                sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
                break;
            }
        }

        return sb;
    }
}


class SimpleNumerator extends TennerNumerator {

    @Override
    public StringBuffer numerate(int number) {
        StringBuffer sb = super.numerate(number);

        this.first = number % 10;

        for (SimpleNumbers simpleNumber : SimpleNumbers.values()) {
            if (first == 0) {
                break;
            }
            if (simpleNumber.getIntValue() == first) {
                String s = simpleNumber.toString().toLowerCase();
                sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
                break;
            }
        }

        return sb;
    }
}

class GeneralNumerator {
    private AfterNineNumerator afterNineNumerator;
    private SimpleNumerator simpleNumerator;

    public StringBuffer convertToDollars(int number) {
        int first = number % 10;
        int second = (number - first) % 100 / 10;

        if (second == 1 && first != 0) {
            this.afterNineNumerator = new AfterNineNumerator();
            return this.afterNineNumerator.numerate(number);
        } else {
            this.simpleNumerator = new SimpleNumerator();
            return this.simpleNumerator.numerate(number);
        }
    }
}

class GeneralThousandNumerator {

    public StringBuffer convertToDollars(int number) {
        if (number > 999) {
            this.generalNumerator = new GeneralNumerator();
            StringBuffer sb = this.generalNumerator.convertToDollars(number / 1000);
            String s = Orders.THOUSAND.toString().toLowerCase();
            sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
            sb.append(this.generalNumerator.convertToDollars(number % 1000));
            s = Dollars.DOLLARS.toString().toLowerCase();
            sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));

            return sb;
        } else {
            this.generalNumerator = new GeneralNumerator();
            StringBuffer sb = new StringBuffer();
            sb.append(this.generalNumerator.convertToDollars(number));
            String s = Dollars.DOLLARS.toString().toLowerCase();
            sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));

            return sb;
        }
    }

    private GeneralNumerator generalNumerator;
}

class GeneralMillionNumerator {

    private int number;

    public GeneralMillionNumerator(int number) {
        this.number = number;
    }

    public String convertToDollars() {
        if (number > 999999) {
            this.generalThousandNumerator = new GeneralThousandNumerator();
            String thousands = this.generalThousandNumerator.convertToDollars(number % 1000000).toString();
            this.generalNumerator = new GeneralNumerator();
            StringBuffer sb = new StringBuffer();
            sb.append(this.generalNumerator.convertToDollars((number - number % 1000000) / 1000000));
            String s = Orders.MILLION.toString().toLowerCase();
            sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));

            return sb.toString() + thousands;

        } else {
            this.generalThousandNumerator = new GeneralThousandNumerator();
            return this.generalThousandNumerator.convertToDollars(number).toString();
        }
    }

    private GeneralNumerator generalNumerator;
    private GeneralThousandNumerator generalThousandNumerator;
}

enum SimpleNumbers {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9);

    private int intValue;

    SimpleNumbers(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}

enum AfterNineNumbers {
    ELEVEN(11), TWELVE(12), THIRTEEN(13), FOURTEEN(14), FIFTEEN(15), SIXTEEN(16), SEVENTEEN(17), EIGHTEEN(18), NINETEEN(19);

    private int intValue;

    AfterNineNumbers(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}

enum Tenners {
    TEN(10), TWENTY(20), THIRTY(30), FORTY(40), FIFTY(50), SIXTY(60), SEVENTY(70), EIGHTY(80), NINETY(90);

    private int intValue;

    Tenners(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}

enum Orders {
    HUNDRED, THOUSAND, MILLION
}

enum Dollars {
    DOLLARS
}
