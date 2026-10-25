package com.example.bignumsjavafx261009;

/**
 * Műveletek tetszőlegesen nagy természetes számokon (decimális számjegy-sztringként tárolva).
 * A műveletek soha nem módosítják a paramétereiket, mindig új DamnBigNumber-t adnak vissza.
 */
public class DamnBigNumbers {

    private static final int[] SMALL_PRIMES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53};

    // ------------------------------------------------------------------
    // Nyilvános API
    // ------------------------------------------------------------------

    /**
     * add "b" to big "a"
     */
    public static DamnBigNumber add(DamnBigNumber a, long b) {
        return add(a, fromLong(b));
    }

    /**
     * add big "b" to big "a"
     */
    public static DamnBigNumber add(DamnBigNumber a, DamnBigNumber b) {
        return new DamnBigNumber(addStr(a.getNumStr(), b.getNumStr()));
    }

    /**
     * subtract "b" from big "a" (természetes számok: a >= b kell, különben ArithmeticException)
     */
    public static DamnBigNumber subtract(DamnBigNumber a, long b) {
        return subtract(a, fromLong(b));
    }

    /**
     * subtract big "b" from big "a" (a >= b kell, különben ArithmeticException)
     */
    public static DamnBigNumber subtract(DamnBigNumber a, DamnBigNumber b) {
        if (cmp(a.getNumStr(), b.getNumStr()) < 0) {
            throw new ArithmeticException("Negative result is not a natural number");
        }
        return new DamnBigNumber(subStr(a.getNumStr(), b.getNumStr()));
    }

    /**
     * multiply "a" by "b"
     */
    public static DamnBigNumber multiply(DamnBigNumber a, long b) {
        return multiply(a, fromLong(b));
    }

    /**
     * multiply big "a" by big "b"
     */
    public static DamnBigNumber multiply(DamnBigNumber a, DamnBigNumber b) {
        return new DamnBigNumber(mulStr(a.getNumStr(), b.getNumStr()));
    }

    /**
     * integer division: big "a" / "b" (nullával osztás: ArithmeticException)
     */
    public static DamnBigNumber divide(DamnBigNumber a, long b) {
        return divide(a, fromLong(b));
    }

    /**
     * integer division: big "a" / big "b"
     */
    public static DamnBigNumber divide(DamnBigNumber a, DamnBigNumber b) {
        return new DamnBigNumber(divmod(a.getNumStr(), b.getNumStr())[0]);
    }

    /**
     * remainder of big "a" / "b"
     */
    public static DamnBigNumber remainder(DamnBigNumber a, long b) {
        return remainder(a, fromLong(b));
    }

    /**
     * remainder of big "a" / big "b"
     */
    public static DamnBigNumber remainder(DamnBigNumber a, DamnBigNumber b) {
        return new DamnBigNumber(divmod(a.getNumStr(), b.getNumStr())[1]);
    }

    /**
     * -1, 0 vagy 1 aszerint, hogy a kisebb, egyenlő vagy nagyobb, mint b
     */
    public static int compare(DamnBigNumber a, DamnBigNumber b) {
        return cmp(a.getNumStr(), b.getNumStr());
    }

    public static boolean areEqual(DamnBigNumber a, DamnBigNumber b) {
        return cmp(a.getNumStr(), b.getNumStr()) == 0;
    }

    public static boolean areEqual(DamnBigNumber a, long b) {
        return b >= 0 && a.getNumStr().equals(Long.toString(b));
    }

    public static boolean areEqual(long a, DamnBigNumber b) {
        return areEqual(b, a);
    }

    // ------------------------------------------------------------------
    // Belső segédek (a DamnBigNumber is használja őket)
    // ------------------------------------------------------------------

    private static DamnBigNumber fromLong(long b) {
        if (b < 0) {
            throw new IllegalArgumentException("Only natural numbers are supported: " + b);
        }
        return new DamnBigNumber(b);
    }

    /** Ellenőrzi, hogy csak számjegyek vannak, és levágja a vezető nullákat. */
    static String normalize(String s) {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        s = s.trim();
        if (s.isEmpty()) {
            return "0";
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                throw new NumberFormatException("Not a natural number: " + s);
            }
        }
        return strip(s);
    }

    private static String strip(String s) {
        int i = 0;
        while (i < s.length() - 1 && s.charAt(i) == '0') {
            i++;
        }
        return s.substring(i);
    }

    /** Normalizált sztringek összehasonlítása. */
    private static int cmp(String x, String y) {
        if (x.length() != y.length()) {
            return x.length() < y.length() ? -1 : 1;
        }
        int c = x.compareTo(y);
        return Integer.compare(c, 0);
    }

    private static String addStr(String x, String y) {
        StringBuilder sb = new StringBuilder(Math.max(x.length(), y.length()) + 1);
        int i = x.length() - 1;
        int j = y.length() - 1;
        int carry = 0;
        while (i >= 0 || j >= 0 || carry > 0) {
            int s = carry;
            if (i >= 0) s += x.charAt(i--) - '0';
            if (j >= 0) s += y.charAt(j--) - '0';
            sb.append((char) ('0' + s % 10));
            carry = s / 10;
        }
        return sb.reverse().toString();
    }

    /** x >= y feltétellel. */
    private static String subStr(String x, String y) {
        StringBuilder sb = new StringBuilder(x.length());
        int j = y.length() - 1;
        int borrow = 0;
        for (int i = x.length() - 1; i >= 0; i--) {
            int d = x.charAt(i) - '0' - borrow;
            if (j >= 0) d -= y.charAt(j--) - '0';
            if (d < 0) {
                d += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }
            sb.append((char) ('0' + d));
        }
        return strip(sb.reverse().toString());
    }

    private static String mulStr(String x, String y) {
        if (x.equals("0") || y.equals("0")) {
            return "0";
        }
        int[] res = new int[x.length() + y.length()];
        for (int i = x.length() - 1; i >= 0; i--) {
            int dx = x.charAt(i) - '0';
            for (int j = y.length() - 1; j >= 0; j--) {
                res[i + j + 1] += dx * (y.charAt(j) - '0');
            }
        }
        int carry = 0;
        for (int k = res.length - 1; k >= 0; k--) {
            int v = res[k] + carry;
            res[k] = v % 10;
            carry = v / 10;
        }
        StringBuilder sb = new StringBuilder(res.length);
        for (int d : res) {
            sb.append((char) ('0' + d));
        }
        return strip(sb.toString());
    }

    /** Hosszú osztás: {hányados, maradék}. */
    private static String[] divmod(String x, String y) {
        if (y.equals("0")) {
            throw new ArithmeticException("Division by zero");
        }
        if (cmp(x, y) < 0) {
            return new String[]{"0", x};
        }
        // y 0..9-szeresei, hogy minden hányados-számjegy gyorsan megtalálható legyen
        String[] m = new String[10];
        m[0] = "0";
        for (int k = 1; k < 10; k++) {
            m[k] = addStr(m[k - 1], y);
        }
        StringBuilder q = new StringBuilder(x.length());
        String r = "0";
        for (int i = 0; i < x.length(); i++) {
            r = r.equals("0") ? String.valueOf(x.charAt(i)) : r + x.charAt(i);
            int k = 0;
            while (k < 9 && cmp(m[k + 1], r) <= 0) {
                k++;
            }
            if (k > 0) {
                r = subStr(r, m[k]);
            }
            q.append((char) ('0' + k));
        }
        return new String[]{strip(q.toString()), r};
    }

    private static int modSmall(String x, int m) {
        long r = 0;
        for (int i = 0; i < x.length(); i++) {
            r = (r * 10 + (x.charAt(i) - '0')) % m;
        }
        return (int) r;
    }

    private static boolean isEven(String x) {
        return ((x.charAt(x.length() - 1) - '0') & 1) == 0;
    }

    private static String halve(String x) {
        StringBuilder sb = new StringBuilder(x.length());
        int rem = 0;
        for (int i = 0; i < x.length(); i++) {
            int cur = rem * 10 + (x.charAt(i) - '0');
            sb.append((char) ('0' + cur / 2));
            rem = cur % 2;
        }
        return strip(sb.toString());
    }

    private static String modPow(String base, String exp, String mod) {
        String result = "1";
        String b = divmod(base, mod)[1];
        while (!exp.equals("0")) {
            if (!isEven(exp)) {
                result = divmod(mulStr(result, b), mod)[1];
            }
            exp = halve(exp);
            if (!exp.equals("0")) {
                b = divmod(mulStr(b, b), mod)[1];
            }
        }
        return result;
    }

    private static boolean millerRabinRound(String n, String nm1, String d, int s, int base) {
        String x = modPow(String.valueOf(base), d, n);
        if (x.equals("1") || x.equals(nm1)) {
            return true;
        }
        for (int r = 1; r < s; r++) {
            x = divmod(mulStr(x, x), n)[1];
            if (x.equals(nm1)) {
                return true;
            }
            if (x.equals("1")) {
                return false;
            }
        }
        return false;
    }

    /**
     * Prímteszt: kis prímekkel való osztás, majd Miller–Rabin az első 16 prímmal mint alapokkal.
     * 3,3·10^24 alatt determinisztikus, afölött rendkívül nagy valószínűségű prímteszt.
     */
    static boolean isPrime(String n) {
        if (cmp(n, "2") < 0) {
            return false;
        }
        for (int p : SMALL_PRIMES) {
            if (n.equals(String.valueOf(p))) {
                return true;
            }
            if (modSmall(n, p) == 0) {
                return false;
            }
        }
        String nm1 = subStr(n, "1");
        String d = nm1;
        int s = 0;
        while (isEven(d)) {
            d = halve(d);
            s++;
        }
        for (int base : SMALL_PRIMES) {
            if (!millerRabinRound(n, nm1, d, s, base)) {
                return false;
            }
        }
        return true;
    }
}