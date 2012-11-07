package npx.utilities;

import java.util.StringTokenizer;


/**
 * 
 * @author Luis Dias Costa
 */
public class StringHelper {

    public static String removeSpaces(String s) {
        StringTokenizer st = new StringTokenizer(s, " ", false);
        String t = "";
        while (st.hasMoreElements()) {
            t += st.nextElement();
        }
        return t;
    }

    public static boolean emptyOrNull(String s) {
        if (s != null && !s.isEmpty()) {
            return false;
        }

        return true;
    }

    public static boolean equals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }

        if (a.isEmpty() && b.isEmpty()) {
            return true;
        }
        
        return a.equals(b);
    }


}
