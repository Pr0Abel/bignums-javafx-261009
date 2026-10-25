package com.example.bignumsjavafx261009;

/**
 * Tetszőlegesen nagy természetes szám, decimális számjegy-sztringként tárolva.
 */
public class DamnBigNumber {
    private String numStr = "0";

    public String getNumStr() {
        return numStr;
    }

    /** Csak számjegyeket fogad el (NumberFormatException különben); a vezető nullákat levágja. */
    public void setNumStr(String numStr) {
        this.numStr = DamnBigNumbers.normalize(numStr);
    }

    public DamnBigNumber() {
        setNumStr("0");
    }

    public DamnBigNumber(int x) {
        this((long) x);
    }

    public DamnBigNumber(long x) {
        if (x < 0) {
            throw new IllegalArgumentException("Only natural numbers are supported: " + x);
        }
        setNumStr(Long.toString(x));
    }

    public DamnBigNumber(String xStr) {
        setNumStr(xStr);
    }

    public static final DamnBigNumber DBN_ZERO = new DamnBigNumber(0);
    public static final DamnBigNumber DBN_ONE = new DamnBigNumber(1);

    @Override
    public String toString() {
        return getNumStr();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DamnBigNumber)) return false;
        return numStr.equals(((DamnBigNumber) obj).numStr);
    }

    @Override
    public int hashCode() {
        return numStr.hashCode();
    }

    /** Hibát (NumberFormatException) jelez, ha a szám nem fér el long-ban. */
    public long getLong() {
        return Long.parseLong(getNumStr());
    }

    /**
     * @return új DamnBigNumber, értéke this + 1 (az eredeti objektum nem változik)
     */
    public DamnBigNumber increment() {
        return DamnBigNumbers.add(this, DBN_ONE);
    }

    /**
     * @return új DamnBigNumber, értéke this - 1 (0-ra ArithmeticException)
     */
    public DamnBigNumber decrement() {
        return DamnBigNumbers.subtract(this, DBN_ONE);
    }

    /**
     * Prímteszt (Miller–Rabin; 3,3·10^24 alatt determinisztikus, afölött valószínűségi).
     * @return true, ha a szám prím
     */
    public boolean isPrime() {
        return DamnBigNumbers.isPrime(getNumStr());
    }
}