package npx.utilities;

/**
 *
 * @author Luis Dias Costa
 */
public class ipv4Helper {

    /** CAUTION: returns 0 by default. 0 Can be also valid in some situations. */
    public static int str2ip(String s) {
        int r = 0;


        String token[] = s.trim().split("\\.");

        try {

            if (token.length == 4) {

                int t = 0;

                r = Integer.parseInt(token[0]) << 24;

                t = Integer.parseInt(token[1]) << 16;
                r = r | t;

                t = Integer.parseInt(token[2]) << 8;
                r = r | t;

                t = Integer.parseInt(token[3]);
                r = r | t;
            }

        } catch (NumberFormatException ex) {
            r = 0;
        }

        return r;
    }

    public static int bytes2ip(int b0, int b1, int b2, int b3) {

        b0 = b0 << 24;
        b1 = b1 << 16;
        b2 = b2 << 8;

        int i = b0 + b1 + b2 + b3;

        return i;
    }

    public static String ip2str(int addr) {

        int b0 = addr & 255;
        int b1 = (addr & 0xff00) >> 8;
        int b2 = (addr & 0xff0000) >> 16;
        int b3 = (addr & 0xff000000) >> 24;

        if (b3 < 0) {
            b3 += 256;
        }
        return String.valueOf(b3) + "."
                + String.valueOf(b2) + "."
                + String.valueOf(b1) + "."
                + String.valueOf(b0);

    }

    /**
     * Counts number of bits set to 1 in a int value (when it is a netmask, 
     * returns a netmask /xy count style)  e.g.: 255.255.255.0 int ip, returns "24"
     * @return 
     * @param ip
     */
    public static int countBits(int ip) {
        int bitsSet = 0;
        int bit = 0;

        for (int i = 0; i < 32; i++) {

            bit = (ip << (31 - i)) >>> 31;

            if (bit != 0) {
                bitsSet++;
            }

        }

        return bitsSet;
    }

    public static int getNetwork(String ipStr, String maskStr) {
        int ip = ipv4Helper.str2ip(ipStr);
        int mask = ipv4Helper.str2ip(maskStr);

        return ip & mask;
    }

    /** returns a network/mask bits string. e.g. 10.1.2.3 with 255.255.255.0 will
    produce a string '10.1.2.0/24 '*/
    public static String getNetworkString(String ipStr, String maskStr) {
        int ip = ipv4Helper.str2ip(ipStr);
        int mask = ipv4Helper.str2ip(maskStr);

        return ip2str(ip & mask) + "/" + countBits(mask);
    }

    public static boolean isNumber(String s) {
        if (s.isEmpty() || s == null) {
            return false;
        }

        return s.matches("\\d+");
    }

    public static boolean isIPv4(String s) {
        if (s.isEmpty() || s == null) {
            return false;
        }

        String[] octets = s.split("\\.");

        if (octets.length != 4) {
            return false;
        }

        return IntHelper.isNumber(octets[0], 0, 255)
                && IntHelper.isNumber(octets[1], 0, 255)
                && IntHelper.isNumber(octets[2], 0, 255)
                && IntHelper.isNumber(octets[3], 0, 255);
    }
}
