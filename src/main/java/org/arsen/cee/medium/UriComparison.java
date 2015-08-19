package org.arsen.cee.medium;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
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
            return "True";
        }

        return "False";
    }

    private static String transformUri(String uri) throws UnsupportedEncodingException {
        String result = replaceWrongPercentEncodingCandidatesPresent(uri);
        result = URLDecoder.decode(result, "UTF-8");
        Pattern pattern = Pattern.compile("(.*)://(.*)(:[0-9]*)(/.*)");
        Matcher matcher = pattern.matcher(result);
        StringBuilder sb = new StringBuilder();

        if (matcher.find()) {
            if (matcher.group(3).equals("")) {
                result = sb.append(matcher.group(1).toLowerCase()).append("://").append(matcher.group(2).toLowerCase())
                         .append(":80").append(matcher.group(4)).toString();

                pattern = Pattern.compile(" ");
                matcher = pattern.matcher(result);

                while(matcher.find()) {
                    result = matcher.replaceAll("%");
                }

                return result;
            } else {
                result = sb.append(matcher.group(1).toLowerCase()).append("://").append(matcher.group(2).toLowerCase())
                         .append(matcher.group(3)).append(matcher.group(4)).toString();

                pattern = Pattern.compile(" ");
                matcher = pattern.matcher(result);

                while(matcher.find()) {
                    result = matcher.replaceAll("%");
                }

                return result;
            }
        } else {
            pattern = Pattern.compile("(.*)://([a-zA-Z0-9\\.]*)(.*)");
            matcher = pattern.matcher(result);
            matcher.find();
            result = sb.append(matcher.group(1).toLowerCase()).append("://").append(matcher.group(2).toLowerCase())
                    .append(":80").append(matcher.group(3)).toString();

            pattern = Pattern.compile(" ");
            matcher = pattern.matcher(result);

            while(matcher.find()) {
                result = matcher.replaceAll("%");
            }

            return result;
        }
    }

    private static String replaceWrongPercentEncodingCandidatesPresent(String uri) {
        List<Integer> wrongIndexes = new LinkedList<Integer>();
        String result = uri;
        int prevIndex = 0;
        int counter = 0;

        do {
            int index = uri.indexOf("%");
            if (index == -1) {
                break;
            }
            String candidate = uri.substring(index, index + 3);
            uri = uri.substring(index + 1);

            Pattern pattern = Pattern.compile("%[\\dA-Z|A-Z\\d]{2}");
            Matcher matcher = pattern.matcher(candidate);

            if (!matcher.matches()) {
                wrongIndexes.add(index + prevIndex);
            }
            prevIndex = result.length() - uri.length();
        } while (uri.length() > 2);

        for (int i : wrongIndexes) {
            StringBuilder sb = new StringBuilder();
            sb.append(result.substring(counter, i)).append(" ");

            if (i != result.length() - 1) {
                sb.append(result.substring(i + 1));
            }

            result = sb.toString();
        }

        return result;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        File file = new File(args[0]);
        String line, result[];

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                result = line.split(";");

                System.out.println(compare(result));
            }
        }
    }
}