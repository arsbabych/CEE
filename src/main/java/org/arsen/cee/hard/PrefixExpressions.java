package org.arsen.cee.hard;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by arsen on 20.08.15.
 */
public class PrefixExpressions {

    public static final String REGEX = "[/|\\*|\\-|\\+]{1}\\x20{1}[0-9\\.]+\\x20{1}[0-9\\.]+";

    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        String line;

        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher;

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {

                matcher = pattern.matcher(line);

                while (matcher.find()) {
                    String statement = matcher.group();
                    String[] members = statement.split(" ");
                    line = matcher.replaceFirst(execStatement(members));
                    matcher = pattern.matcher(line);
                }

                System.out.println(((Double)Double.parseDouble(line)).intValue());
            }
        }
    }

    private static String execStatement(String[] members) {
        Character operation = members[0].charAt(0);
        Double mem1 = Double.parseDouble(members[1]);
        Double mem2 = Double.parseDouble(members[2]);

        switch (operation) {
            case '+':
                return mem1 + mem2 + "";
            case '-':
                return mem1 - mem2 + "";
            case '/':
                return mem1 / mem2 + "";
            case '*':
                return mem1 * mem2 + "";
        }

        return null;
    }
}
