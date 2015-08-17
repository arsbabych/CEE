package org.arsen.cee.medium;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thor on 17.08.2015.
 */
public class UriComparison {

    private static String compare(String[] result) throws URISyntaxException, UnsupportedEncodingException {
        result[0] = transformUri(result[0]);
        result[1] = transformUri(result[1]);

        if (result[1].equals(result[0])) {
            return "true";
        }

        return "false";
    }

    private static String transformUri(String uri) throws UnsupportedEncodingException {
        String result = URLDecoder.decode(uri, "UTF-8");
        Pattern pattern = Pattern.compile("(.*)://(.*):([0-9]*)(.*)");
        Matcher matcher = pattern.matcher(result);
        StringBuilder sb = new StringBuilder();

        while(matcher.find()) {
            if (matcher.group(3).equals("")) {
                return sb.append(matcher.group(1).toLowerCase()).append("://").append(matcher.group(2).toLowerCase())
                         .append(":80").append(matcher.group(4)).toString();
            } else {
                return sb.append(matcher.group(1).toLowerCase()).append("://").append(matcher.group(2).toLowerCase())
                         .append(matcher.group(3)).append(matcher.group(4)).toString();
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        File file = new File(args[0]);
        String line, result[];

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                result = line.split(" ");

                System.out.println(compare(result));
            }
        }
    }
}