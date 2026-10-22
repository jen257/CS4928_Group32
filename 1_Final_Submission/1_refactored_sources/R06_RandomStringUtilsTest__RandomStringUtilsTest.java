package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical use case
        String result = RandomStringUtils.random(10);
        assertNotNull("Expected random string not to be null", result);
        assertEquals("Expected random string length to be 10", 10, result.length());

        // Edge case: count is 0
        result = RandomStringUtils.random(0);
        assertNotNull("Expected random string for count 0 not to be null", result);
        assertEquals("Expected random string length to be 0", 0, result.length());

        // Typical use case
        result = RandomStringUtils.random(10, true, true);
        assertNotNull("Expected random string with letters and numbers not to be null", result);
        assertEquals("Expected random string with letters and numbers length to be 10", 10, result.length());

        // Typical use case
        result = RandomStringUtils.random(10, true, false);
        assertNotNull("Expected random string with letters not to be null", result);
        assertEquals("Expected random string with letters length to be 10", 10, result.length());

        // Typical use case
        result = RandomStringUtils.random(10, false, true);
        assertNotNull("Expected random string with numbers not to be null", result);
        assertEquals("Expected random string with numbers length to be 10", 10, result.length());

        // Typical use case
        result = RandomStringUtils.random(10, 65, 90, false, false);
        assertNotNull("Expected random string with custom range not to be null", result);
        assertEquals("Expected random string with custom range length to be 10", 10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue("Expected character to be within custom ASCII range [65, 90]", c >= 65 && c <= 90);
        }

        // Edge case: count is 0
        result = RandomStringUtils.random(0, 65, 90, false, false);
        assertNotNull("Expected random string with custom range and count 0 not to be null", result);
        assertEquals("Expected random string with custom range and count 0 length to be 0", 0, result.length());

        // Error scenario: count is negative
        try {
            RandomStringUtils.random(-1);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Error scenario: chars array is empty
        try {
            RandomStringUtils.random(10, new char[]{});
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Typical use case
        result = RandomStringUtils.random(10, "abc");
        assertNotNull("Expected random string with string chars not to be null", result);
        assertEquals("Expected random string with string chars length to be 10", 10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue("Expected character to be one of 'a', 'b', or 'c'", c == 'a' || c == 'b' || c == 'c');
        }

        // Edge case: count is 0
        result = RandomStringUtils.random(0, "abc");
        assertNotNull("Expected random string with string chars and count 0 not to be null", result);
        assertEquals("Expected random string with string chars and count 0 length to be 0", 0, result.length());

        // Error scenario: chars string is empty
        try {
            RandomStringUtils.random(10, "");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRandomAscii() {
        // Typical use case
        String result = RandomStringUtils.randomAscii(10);
        assertNotNull("Expected random ASCII string not to be null", result);
        assertEquals("Expected random ASCII string length to be 10", 10, result.length());

        // Edge case: count is 0
        result = RandomStringUtils.randomAscii(0);
        assertNotNull("Expected random ASCII string for count 0 not to be null", result);
        assertEquals("Expected random ASCII string length to be 0", 0, result.length());
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical use case
        String result = RandomStringUtils.randomAlphabetic(10);
        assertNotNull("Expected random alphabetic string not to be null", result);
        assertEquals("Expected random alphabetic string length to be 10", 10, result.length());

        // Edge case: count is 0
        result = RandomStringUtils.randomAlphabetic(0);
        assertNotNull("Expected random alphabetic string for count 0 not to be null", result);
        assertEquals("Expected random alphabetic string length to be 0", 0, result.length());
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical use case
        String result = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull("Expected random alphanumeric string not to be null", result);
        assertEquals("Expected random alphanumeric string length to be 10", 10, result.length());

        // Edge case: count is 0
        result = RandomStringUtils.randomAlphanumeric(0);
        assertNotNull("Expected random alphanumeric string for count 0 not to be null", result);
        assertEquals("Expected random alphanumeric string length to be 0", 0, result.length());
    }

    @Test
    public void testRandomNumeric() {
        // Typical use case
        String result = RandomStringUtils.randomNumeric(10);
        assertNotNull("Expected random numeric string not to be null", result);
        assertEquals("Expected random numeric string length to be 10", 10, result.length());

        // Edge case: count is 0
        result = RandomStringUtils.randomNumeric(0);
        assertNotNull("Expected random numeric string for count 0 not to be null", result);
        assertEquals("Expected random numeric string length to be 0", 0, result.length());
    }

}
