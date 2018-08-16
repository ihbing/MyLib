package louis.general.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Louis on 2018/7/25.
 */

public class UnicodeUtil {
    public static String UnicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }
}
