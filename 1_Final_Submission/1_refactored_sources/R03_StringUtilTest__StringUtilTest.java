package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        String sep = "-";
        String expected = "Hello-World";
        assertEquals("Expected join(Collection, String) to join non-empty list with separator", expected, StringUtil.join(strings, sep));

        strings.clear();
        expected = "";
        assertEquals("Expected join(Collection, String) on empty list to return empty string", expected, StringUtil.join(strings, sep));

        List<String> iteratorStrings = new ArrayList<>();
        iteratorStrings.add("Hello");
        iteratorStrings.add("World");
        Iterator<String> iterator = iteratorStrings.iterator();
        sep = "-";
        expected = "Hello-World";
        assertEquals("Expected join(Iterator, String) to join iterator elements with separator", expected, StringUtil.join(iterator, sep));

        iterator = new ArrayList<String>().iterator();
        expected = "";
        assertEquals("Expected join(Iterator, String) on empty iterator to return empty string", expected, StringUtil.join(iterator, sep));

        String[] stringArray = {"Hello", "World"};
        sep = "-";
        expected = "Hello-World";
        assertEquals("Expected join(String[], String) to join array elements with separator", expected, StringUtil.join(stringArray, sep));

        stringArray = new String[0];
        expected = "";
        assertEquals("Expected join(String[], String) on empty array to return empty string", expected, StringUtil.join(stringArray, sep));
    }

    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        assertEquals("Expected padding(5) to return 5 spaces", expected, StringUtil.padding(width));

        width = 10;
        expected = "          ";
        assertEquals("Expected padding(10) to return 10 spaces", expected, StringUtil.padding(width));
    }

    @Test
    public void testIsBlank() {
        String string = null;
        assertTrue("Expected isBlank(null) to return true", StringUtil.isBlank(string));

        string = "";
        assertTrue("Expected isBlank(\"\") to return true", StringUtil.isBlank(string));

        string = "   ";
        assertTrue("Expected isBlank(\"   \") to return true", StringUtil.isBlank(string));

        string = "Hello";
        assertFalse("Expected isBlank(\"Hello\") to return false", StringUtil.isBlank(string));
    }

    @Test
    public void testIsNumeric() {
        String string = null;
        assertFalse("Expected isNumeric(null) to return false", StringUtil.isNumeric(string));

        string = "";
        assertFalse("Expected isNumeric(\"\") to return false", StringUtil.isNumeric(string));

        string = "12345";
        assertTrue("Expected isNumeric(\"12345\") to return true", StringUtil.isNumeric(string));

        string = "12345a";
        assertFalse("Expected isNumeric(\"12345a\") to return false", StringUtil.isNumeric(string));
    }

    @Test
    public void testIsWhitespace() {
        int c = ' ';
        assertTrue("Expected space character to be recognized as whitespace", StringUtil.isWhitespace(c));

        c = '\t';
        assertTrue("Expected tab character to be recognized as whitespace", StringUtil.isWhitespace(c));

        c = '\n';
        assertTrue("Expected newline character to be recognized as whitespace", StringUtil.isWhitespace(c));

        c = '\f';
        assertTrue("Expected form feed character to be recognized as whitespace", StringUtil.isWhitespace(c));

        c = '\r';
        assertTrue("Expected carriage return character to be recognized as whitespace", StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c = ' ';
        assertTrue("Expected space character to be recognized as actual whitespace", StringUtil.isActuallyWhitespace(c));

        c = '\t';
        assertTrue("Expected tab character to be recognized as actual whitespace", StringUtil.isActuallyWhitespace(c));

        c = '\n';
        assertTrue("Expected newline character to be recognized as actual whitespace", StringUtil.isActuallyWhitespace(c));

        c = '\f';
        assertTrue("Expected form feed character to be recognized as actual whitespace", StringUtil.isActuallyWhitespace(c));

        c = '\r';
        assertTrue("Expected carriage return character to be recognized as actual whitespace", StringUtil.isActuallyWhitespace(c));

        c = 160;
        assertTrue("Expected non-breaking space (160) to be recognized as actual whitespace", StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "   Hello   World   ";
        String expected = " Hello World ";
        assertEquals("Expected multiple spaces to collapse into single spaces", expected, StringUtil.normaliseWhitespace(string));

        string = "Hello World";
        expected = "Hello World";
        assertEquals("Expected already normalised string to remain unchanged", expected, StringUtil.normaliseWhitespace(string));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "   Hello   World   ";
        String expected = " Hello World ";
        StringUtil.appendNormalisedWhitespace(accum, string, false);
        assertEquals("Expected normalised whitespace to be appended to StringBuilder", expected, accum.toString());
    }

    @Test
    public void testIn() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        assertTrue("Expected in() to return true when needle is in haystack", StringUtil.in(needle, haystack));

        needle = "Foo";
        assertFalse("Expected in() to return false when needle is not in haystack", StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "Hello";
        String[] haystack = {"Apple", "Banana", "Hello", "World"};
        assertTrue("Expected inSorted() to return true when needle is in sorted haystack", StringUtil.inSorted(needle, haystack));

        needle = "Foo";
        assertFalse("Expected inSorted() to return false when needle is not in sorted haystack", StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relUrl = "page.html";
        String expected = "https://www.example.com/page.html";
        assertEquals("Expected resolve(URL, String) to resolve relative path against base URL", expected, StringUtil.resolve(base, relUrl).toExternalForm());

        relUrl = "/page.html";
        expected = "https://www.example.com/page.html";
        assertEquals("Expected resolve(URL, String) to resolve root-relative path against base URL", expected, StringUtil.resolve(base, relUrl).toExternalForm());

        String baseUrl = "https://www.example.com/";
        relUrl = "page.html";
        expected = "https://www.example.com/page.html";
        assertEquals("Expected resolve(String, String) to resolve relative path against base URL string", expected, StringUtil.resolve(baseUrl, relUrl));

        relUrl = "/page.html";
        expected = "https://www.example.com/page.html";
        assertEquals("Expected resolve(String, String) to resolve root-relative path against base URL string", expected, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "not_an_url";
        relUrl = "page.html";
        expected = "";
        assertEquals("Expected resolve(String, String) with malformed base and relative URL to return empty string", expected, StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertEquals("Expected thread-local stringBuilder() calls to return the same StringBuilder instance", sb1, sb2);
    }
}
