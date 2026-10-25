package com.example.bignumsjavafx261009;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DamnBigNumberTest {
    private static final DamnBigNumber A = new DamnBigNumber("676767676767676767676767");
    private static final DamnBigNumber B = new DamnBigNumber("6969696969696969696969");

    @Test
    public void testIncrement() {
        assertEquals("1000", new DamnBigNumber(999).increment().toString());
        assertEquals("100000000000000000000000000000",
                new DamnBigNumber("99999999999999999999999999999").increment().toString());
        assertEquals("1", DamnBigNumber.DBN_ZERO.increment().toString());
        assertEquals("0", DamnBigNumber.DBN_ZERO.toString()); // a konstans nem változott
    }

    @Test
    public void testDecrement() {
        assertEquals("999", new DamnBigNumber(1000).decrement().toString());
        assertEquals("99999999999999999999999999999",
                new DamnBigNumber("100000000000000000000000000000").decrement().toString());
        assertEquals("0", DamnBigNumber.DBN_ONE.decrement().toString());
    }

    @Test(expected = ArithmeticException.class)
    public void testDecrementZero() {
        DamnBigNumber.DBN_ZERO.decrement();
    }

    @Test
    public void testAdd() {
        assertEquals("683737373737373737373736", DamnBigNumbers.add(A, B).toString());
        // a bemenetek nem változhatnak
        assertEquals("676767676767676767676767", A.toString());
        assertEquals("6969696969696969696969", B.toString());
        assertEquals("1000", DamnBigNumbers.add(new DamnBigNumber(999), 1).toString());
    }

    @Test
    public void testSubtract() {
        assertEquals("669797979797979797979798", DamnBigNumbers.subtract(A, B).toString());
        assertEquals("0", DamnBigNumbers.subtract(A, A).toString());
        assertEquals("999", DamnBigNumbers.subtract(new DamnBigNumber(1000), 1).toString());
    }

    @Test(expected = ArithmeticException.class)
    public void testSubtractNegative() {
        DamnBigNumbers.subtract(B, A);
    }

    @Test
    public void testMultiply() {
        assertEquals("46696969696969696969696923", DamnBigNumbers.multiply(A, 69).toString());
        // A feladatleírásban szereplő 4716972306947163392451323722992227315812227223 elírás (nem A*B);
        // a helyes szorzat (BigInteger-rel és Pythonnal is ellenőrizve):
        assertEquals("4716865625956535047443661949801040710131619223", DamnBigNumbers.multiply(A, B).toString());
        assertEquals("0", DamnBigNumbers.multiply(A, 0).toString());
    }

    @Test
    public void testDivide() {
        assertEquals("97", DamnBigNumbers.divide(A, B).toString());
        assertEquals("9808227199531547357634", DamnBigNumbers.divide(A, 69).toString());
        assertEquals("0", DamnBigNumbers.divide(B, A).toString());
        assertEquals("6969696969696969696969", DamnBigNumbers.remainder(B, A).toString());
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        DamnBigNumbers.divide(A, 0);
    }

    @Test
    public void testEquals() {
        assertEquals(new DamnBigNumber("000123"), new DamnBigNumber(123));
        assertNotEquals(A, B);
        assertTrue(DamnBigNumbers.areEqual(A, new DamnBigNumber("676767676767676767676767")));
        assertFalse(DamnBigNumbers.areEqual(A, B));
        assertTrue(DamnBigNumbers.areEqual(new DamnBigNumber(42), 42L));
        assertTrue(DamnBigNumbers.areEqual(42L, new DamnBigNumber("42")));
        assertFalse(DamnBigNumbers.areEqual(A, 42L));
    }

    @Test
    public void testPrime() {
        assertFalse(new DamnBigNumber(0).isPrime());
        assertFalse(new DamnBigNumber(1).isPrime());
        assertTrue(new DamnBigNumber(2).isPrime());
        assertTrue(new DamnBigNumber(97).isPrime());
        assertFalse(new DamnBigNumber(91).isPrime());
        assertTrue(new DamnBigNumber(1000000007).isPrime());
        assertTrue(new DamnBigNumber("618970019642690137449562111").isPrime());   // 2^89 - 1
        assertFalse(new DamnBigNumber("618970019642690137449562113").isPrime());  // 2^89 + 1 (osztható 3-mal)
        // a googol utáni első prím: 10^100 + 267
        assertTrue(new DamnBigNumber("1" + "0".repeat(97) + "267").isPrime());
        assertFalse(new DamnBigNumber("1" + "0".repeat(99) + "1").isPrime());
        assertFalse(A.isPrime());
    }
}