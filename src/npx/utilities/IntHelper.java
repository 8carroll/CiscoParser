package npx.utilities;

/**
 *
 * @author Luis Dias Costa
 */
public class IntHelper {

    public static boolean isNumber(String s) {
        if (s.isEmpty() || s == null) {
            return false;
        }

        boolean r = false;

        try {
            Integer.valueOf(s);
            r = true;
        } catch (NumberFormatException ex) {
            r = false;
        }

        return r;
    }

    public static boolean isNumber(String s, int lowerbound, int upperbound) {
        if (s.isEmpty() || s == null) {
            return false;
        }

        boolean r = false;

        try {
            int i = Integer.valueOf(s);

            if (i >= lowerbound && i <= upperbound) {
                r = true;
            }
        } catch (NumberFormatException ex) {
            r = false;
        }

        return r;
    }
}
