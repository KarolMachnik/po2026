package agh.po.lab3;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodingBat_TestJUnit {

    private CodingBat cb;

    @Before
    public void setUp() {
        cb = new CodingBat();
    }

    @Test
    public void or35() {
        assertTrue(cb.or35(3));
        assertTrue(cb.or35(10));
        assertFalse(cb.or35(8));
    }

    @Test
    public void close10() {
        assertEquals(8, cb.close10(8, 13));
        assertEquals(8, cb.close10(13, 8));
        assertEquals(0, cb.close10(13, 7));
    }

    @Test
    public void fizzBuzz() {
        assertArrayEquals(new String[]{"1", "2", "Fizz", "4", "Buzz"}, cb.fizzBuzz(1, 6));
        assertArrayEquals(new String[]{"1", "2", "Fizz", "4", "Buzz", "Fizz", "7"}, cb.fizzBuzz(1, 8));
        assertArrayEquals(new String[]{"1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8", "Fizz", "Buzz"}, cb.fizzBuzz(1, 11));
    }

    @Test
    public void startWord() {
        assertEquals("hi", cb.startWord("hippo", "hi"));
        assertEquals("hip", cb.startWord("hippo", "xip"));
        assertEquals("h", cb.startWord("hippo", "i"));
    }
}