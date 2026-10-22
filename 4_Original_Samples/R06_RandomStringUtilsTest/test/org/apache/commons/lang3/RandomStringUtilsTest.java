package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical use case
        String result = RandomStringUtils.random(10);
        assertNotNull(result);
        assertEquals(10, result.length());

        // Edge case: count is 0
        result = RandomStringUtils.random(0);
        assertNotNull(result);
        assertEquals(0, result.length());
    }

    @Test
    public void testRandomAscii() {
        // Typical use case
        String result = RandomStringUtils.randomAscii(10);
        assertNotNull(result);
        assertEquals(10, result.length());

        // Edge case: count is 0
        result = RandomStringUtils.randomAscii(0);
        assertNotNull(result);
        assertEquals(0, result.length());
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical use case
        String result = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(result);
        assertEquals(10, result.length());

        // Edge case: count is 0
        result = RandomStringUtils.randomAlphabetic(0);
        assertNotNull(result);
        assertEquals(0, result.length());
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical use case
        String result = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(result);
        assertEquals(10, result.length());

        // Edge case: count is 0
        result = RandomStringUtils.randomAlphanumeric(0);
        assertNotNull(result);
        assertEquals(0, result.length());
    }

    @Test
    public void testRandomNumeric() {
        // Typical use case
        String result = RandomStringUtils.randomNumeric(10);
        assertNotNull(result);
        assertEquals(10, result.length());

        // Edge case: count is 0
        result = RandomStringUtils.randomNumeric(0);
        assertNotNull(result);
        assertEquals(0, result.length());
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        // Typical use case
        String result = RandomStringUtils.random(10, true, true);
        assertNotNull(result);
        assertEquals(10, result.length());
    }

    @Test
    public void testRandomWithLetters() {
        // Typical use case
        String result = RandomStringUtils.random(10, true, false);
        assertNotNull(result);
        assertEquals(10, result.length());
    }

    @Test
    public void testRandomWithNumbers() {
        // Typical use case
        String result = RandomStringUtils.random(10, false, true);
        assertNotNull(result);
        assertEquals(10, result.length());
    }

    @Test
    public void testRandomWithCustomRange() {
        // Typical use case
        String result = RandomStringUtils.random(10, 65, 90, false, false);
        assertNotNull(result);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c >= 65 && c <= 90);
        }

        // Edge case: count is 0
        result = RandomStringUtils.random(0, 65, 90, false, false);
        assertNotNull(result);
        assertEquals(0, result.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        // Error scenario: count is negative
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        // Error scenario: chars array is empty
        RandomStringUtils.random(10, new char[]{});
    }

    @Test
    public void testRandomWithStringChars() {
        // Typical use case
        String result = RandomStringUtils.random(10, "abc");
        assertNotNull(result);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c');
        }

        // Edge case: count is 0
        result = RandomStringUtils.random(0, "abc");
        assertNotNull(result);
        assertEquals(0, result.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithStringEmptyChars() {
        // Error scenario: chars string is empty
        RandomStringUtils.random(10, "");
    }

}