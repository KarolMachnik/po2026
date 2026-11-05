package agh.po.lab3;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void mapAB() {
        assertEquals(Map.of("a", "Hi", "b", "There", "ab", "HiThere"), cb.mapAB(new HashMap<>(Map.of("a", "Hi", "b", "There"))));
        assertEquals(Map.of("a", "Hi"), cb.mapAB(new HashMap<>(Map.of("a", "Hi"))));
        assertEquals(Map.of("b", "There"), cb.mapAB(new HashMap<>(Map.of("b", "There"))));
    }

    @Test
    public void wordLen() {
        assertEquals(Map.of("a", 1, "bb", 2), cb.wordLen(new String[]{"a", "bb", "a", "bb"}));
        assertEquals(Map.of("this", 4, "and", 3, "that", 4), cb.wordLen(new String[]{"this", "and", "that", "and"}));
        assertEquals(Map.of("code", 4, "bug", 3), cb.wordLen(new String[]{"code", "code", "code", "bug"}));
    }

}