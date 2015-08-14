package org.arsen.cee.easy;

import java.io.*;
import java.util.*;

/**
 * Created by arsen on 14.08.15.
 */
public class ReverseWords {
    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        List<String> list;
        ArsenList<String> arsenList;



        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                list = Arrays.asList(line.split(" "));
                arsenList = toArsenList(list);
                Collections.reverse(arsenList);
                System.out.println(arsenList);
            }
        }
    }

    public static ArsenList toArsenList(List list) {
        ArsenList arsenList = new ArsenList();
        Iterator iterator = list.iterator();

        for (;;) {
            if (iterator.hasNext()) {
                arsenList.add(iterator.next());
                continue;
            }
            return arsenList;
        }
    }
}

class ArsenList<T> extends ArrayList<T> {

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator iterator = this.iterator();

        while (iterator.hasNext()) {
            sb.append(iterator.next()).append(" ");
        }

        return sb.toString().substring(0, sb.toString().length() - 1);
    }
}
