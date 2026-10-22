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
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        String sep = "-";
        String expected = "Hello-World";
        assertEquals(expected, StringUtil.join(strings, sep));

        strings.clear();
        expected = "";
        assertEquals(expected, StringUtil.join(strings, sep));
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        Iterator<String> iterator = strings.iterator();
        String sep = "-";
        String expected = "Hello-World";
        assertEquals(expected, StringUtil.join(iterator, sep));

        iterator = new ArrayList<String>().iterator();
        expected = "";
        assertEquals(expected, StringUtil.join(iterator, sep));
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String sep = "-";
        String expected = "Hello-World";
        assertEquals(expected, StringUtil.join(strings, sep));

        strings = new String[0];
        expected = "";
        assertEquals(expected, StringUtil.join(strings, sep));
    }

    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        assertEquals(expected, StringUtil.padding(width));

        width = 10;
        expected = "          ";
        assertEquals(expected, StringUtil.padding(width));
    }

    @Test
    public void testIsBlank() {
        String string = null;
        assertTrue(StringUtil.isBlank(string));

        string = "";
        assertTrue(StringUtil.isBlank(string));

        string = "   ";
        assertTrue(StringUtil.isBlank(string));

        string = "Hello";
        assertFalse(StringUtil.isBlank(string));
    }

    @Test
    public void testIsNumeric() {
        String string = null;
        assertFalse(StringUtil.isNumeric(string));

        string = "";
        assertFalse(StringUtil.isNumeric(string));

        string = "12345";
        assertTrue(StringUtil.isNumeric(string));

        string = "12345a";
        assertFalse(StringUtil.isNumeric(string));
    }

    @Test
    public void testIsWhitespace() {
        int c = ' ';
        assertTrue(StringUtil.isWhitespace(c));

        c = '\t';
        assertTrue(StringUtil.isWhitespace(c));

        c = '\n';
        assertTrue(StringUtil.isWhitespace(c));

        c = '\f';
        assertTrue(StringUtil.isWhitespace(c));

        c = '\r';
        assertTrue(StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c = ' ';
        assertTrue(StringUtil.isActuallyWhitespace(c));

        c = '\t';
        assertTrue(StringUtil.isActuallyWhitespace(c));

        c = '\n';
        assertTrue(StringUtil.isActuallyWhitespace(c));

        c = '\f';
        assertTrue(StringUtil.isActuallyWhitespace(c));

        c = '\r';
        assertTrue(StringUtil.isActuallyWhitespace(c));

        c = 160;
        assertTrue(StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "   Hello   World   ";
        String expected = " Hello World ";
        assertEquals(expected, StringUtil.normaliseWhitespace(string));

        string = "Hello World";
        expected = "Hello World";
        assertEquals(expected, StringUtil.normaliseWhitespace(string));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "   Hello   World   ";
        String expected = " Hello World ";
        StringUtil.appendNormalisedWhitespace(accum, string, false);
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testIn() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.in(needle, haystack));

        needle = "Foo";
        assertFalse(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "Hello";
        String[] haystack = {"Apple", "Banana", "Hello", "World"};
        assertTrue(StringUtil.inSorted(needle, haystack));

        needle = "Foo";
        assertFalse(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relUrl = "page.html";
        String expected = "https://www.example.com/page.html";
        assertEquals(expected, StringUtil.resolve(base, relUrl).toExternalForm());

        relUrl = "/page.html";
        expected = "https://www.example.com/page.html";
        assertEquals(expected, StringUtil.resolve(base, relUrl).toExternalForm());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com/";
        String relUrl = "page.html";
        String expected = "https://www.example.com/page.html";
        assertEquals(expected, StringUtil.resolve(baseUrl, relUrl));

        relUrl = "/page.html";
        expected = "https://www.example.com/page.html";
        assertEquals(expected, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "not_an_url";
        relUrl = "page.html";
        expected = "";
        assertEquals(expected, StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertEquals(sb1, sb2);
    }
}