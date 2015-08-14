package org.arsen.cee.medium;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by arsen on 12.08.15.
 */
public class LongestLines {
    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        List<String> list = new LinkedList<String>();

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            int number = Integer.parseInt(line.trim());

            while ((line = br.readLine()) != null) {
                line = line.trim();
                list.add(line);
            }

            Collections.sort(list, new Comparator<String>() {
                public int compare(String o1, String o2) {
                    if (o1.length() > o2.length()) {
                        return -1;
                    } else if (o1.length() < o2.length()) {
                        return 1;
                    }

                    return 0;
                }
            });

            int counter = 0;
            while (number-- != 0) {
                if (counter < list.size()) {
                    System.out.println(list.get(counter++));
                }
            }

        }
    }
}
