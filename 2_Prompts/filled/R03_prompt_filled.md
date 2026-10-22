# Master Refactoring Prompt (RQ2)

Paste this prompt to the AI, with `R03`, `13`, `11`, `172`,
`package org.jsoup.helper;

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
}` and `package org.jsoup.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * A minimal String utility class. Designed for internal jsoup use only.
 */
public final class StringUtil {
    // memoised padding up to 21
    static final String[] padding = {"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ",
        "         ", "          ", "           ", "            ", "             ", "              ", "               ",
        "                ", "                 ", "                  ", "                   ", "                    "};

    /**
     * Join a collection of strings by a separator
     * @param strings collection of string objects
     * @param sep string to place between strings
     * @return joined string
     */
    public static String join(Collection strings, String sep) {
        return join(strings.iterator(), sep);
    }

    /**
     * Join a collection of strings by a separator
     * @param strings iterator of string objects
     * @param sep string to place between strings
     * @return joined string
     */
    public static String join(Iterator strings, String sep) {
        if (!strings.hasNext())
            return "";

        String start = strings.next().toString();
        if (!strings.hasNext()) // only one, avoid builder
            return start;

        StringBuilder sb = new StringBuilder(64).append(start);
        while (strings.hasNext()) {
            sb.append(sep);
            sb.append(strings.next());
        }
        return sb.toString();
    }

    /**
     * Join an array of strings by a separator
     * @param strings collection of string objects
     * @param sep string to place between strings
     * @return joined string
     */
    public static String join(String[] strings, String sep) {
        return join(Arrays.asList(strings), sep);
    }

    /**
     * Returns space padding
     * @param width amount of padding desired
     * @return string of spaces * width
     */
    public static String padding(int width) {
        if (width < 0)
            throw new IllegalArgumentException("width must be > 0");

        if (width < padding.length)
            return padding[width];
        char[] out = new char[width];
        for (int i = 0; i < width; i++)
            out[i] = ' ';
        return String.valueOf(out);
    }

    /**
     * Tests if a string is blank: null, empty, or only whitespace (" ", \r\n, \t, etc)
     * @param string string to test
     * @return if string is blank
     */
    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0)
            return true;

        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!StringUtil.isWhitespace(string.codePointAt(i)))
                return false;
        }
        return true;
    }

    /**
     * Tests if a string is numeric, i.e. contains only digit characters
     * @param string string to test
     * @return true if only digit chars, false if empty or null or contains non-digit chars
     */
    public static boolean isNumeric(String string) {
        if (string == null || string.length() == 0)
            return false;

        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!Character.isDigit(string.codePointAt(i)))
                return false;
        }
        return true;
    }

    /**
     * Tests if a code point is "whitespace" as defined in the HTML spec. Used for output HTML.
     * @param c code point to test
     * @return true if code point is whitespace, false otherwise
     * @see #isActuallyWhitespace(int)
     */
    public static boolean isWhitespace(int c){
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
    }

    /**
     * Tests if a code point is "whitespace" as defined by what it looks like. Used for Element.text etc.
     * @param c code point to test
     * @return true if code point is whitespace, false otherwise
     */
    public static boolean isActuallyWhitespace(int c){
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r' || c == 160;
        // 160 is &nbsp; (non-breaking space). Not in the spec but expected.
    }

        // zero width sp, zw non join, zw join, soft hyphen

    /**
     * Normalise the whitespace within this string; multiple spaces collapse to a single, and all whitespace characters
     * (e.g. newline, tab) convert to a simple space
     * @param string content to normalise
     * @return normalised string
     */
    public static String normaliseWhitespace(String string) {
        StringBuilder sb = StringUtil.stringBuilder();
        appendNormalisedWhitespace(sb, string, false);
        return sb.toString();
    }

    /**
     * After normalizing the whitespace within a string, appends it to a string builder.
     * @param accum builder to append to
     * @param string string to normalize whitespace within
     * @param stripLeading set to true if you wish to remove any leading whitespace
     */
    public static void appendNormalisedWhitespace(StringBuilder accum, String string, boolean stripLeading) {
        boolean lastWasWhite = false;
        boolean reachedNonWhite = false;

        int len = string.length();
        int c;
        for (int i = 0; i < len; i+= Character.charCount(c)) {
            c = string.codePointAt(i);
            if (isActuallyWhitespace(c)) {
                if ((stripLeading && !reachedNonWhite) || lastWasWhite)
                    continue;
                accum.append(' ');
                lastWasWhite = true;
            }
            else {
                accum.appendCodePoint(c);
                lastWasWhite = false;
                reachedNonWhite = true;
            }
        }
    }

    public static boolean in(final String needle, final String... haystack) {
        final int len = haystack.length;
        for (int i = 0; i < len; i++) {
            if (haystack[i].equals(needle))
            return true;
        }
        return false;
    }

    public static boolean inSorted(String needle, String[] haystack) {
        return Arrays.binarySearch(haystack, needle) >= 0;
    }

    /**
     * Create a new absolute URL, from a provided existing absolute URL and a relative URL component.
     * @param base the existing absolute base URL
     * @param relUrl the relative URL to resolve. (If it's already absolute, it will be returned)
     * @return the resolved absolute URL
     * @throws MalformedURLException if an error occurred generating the URL
     */
    public static URL resolve(URL base, String relUrl) throws MalformedURLException {
        // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
        if (relUrl.startsWith("?"))
            relUrl = base.getPath() + relUrl;
        // workaround: //example.com + ./foo = //example.com/./foo, not //example.com/foo
        if (relUrl.indexOf('.') == 0 && base.getFile().indexOf('/') != 0) {
            base = new URL(base.getProtocol(), base.getHost(), base.getPort(), "/" + base.getFile());
        }
        return new URL(base, relUrl);
    }

    /**
     * Create a new absolute URL, from a provided existing absolute URL and a relative URL component.
     * @param baseUrl the existing absolute base URL
     * @param relUrl the relative URL to resolve. (If it's already absolute, it will be returned)
     * @return an absolute URL if one was able to be generated, or the empty string if not
     */
    public static String resolve(final String baseUrl, final String relUrl) {
        URL base;
        try {
            try {
                base = new URL(baseUrl);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute/rel may be abs on its own, so try that
                URL abs = new URL(relUrl);
                return abs.toExternalForm();
            }
            return resolve(base, relUrl).toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    /**
     * Maintains a cached StringBuilder, to minimize new StringBuilder GCs. Prevents it from growing to big per thread.
     * Care must be taken to not grab more than one in the same stack (not locked or mutexed or anything).
     * @return an empty StringBuilder
     */
    public static StringBuilder stringBuilder() {
        StringBuilder sb = stringLocal.get();
        if (sb.length() > MaxCachedBuilderSize) {
            sb = new StringBuilder(MaxCachedBuilderSize);
            stringLocal.set(sb);
        } else {
            sb.delete(0, sb.length());
        }
        return sb;

    }

    private static final int MaxCachedBuilderSize = 8 * 1024;
    private static final ThreadLocal<StringBuilder> stringLocal = new ThreadLocal<StringBuilder>(){
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder(MaxCachedBuilderSize);
        }
    };


}` replaced by the sample's actual
values. Keep every line; the "Detector rules" and "Hard constraints" sections
are what stop the model from going in the wrong direction.

---

You are an expert Java test-refactoring assistant. Refactor the JUnit test
class below to eliminate two test smells, Assertion Roulette (AR) and Lazy
Test (LT), as counted by the TsDetect tool. Preserve all original test
behaviour exactly, and produce code that compiles and passes.

## Detector rules (measured, not textbook)

These are the actual counting rules of TsDetect. Follow them literally.

**Assertion Roulette (AR):** a test method containing 2 or more assertion
statements AND at least one assertion without a message parameter is counted
once. Adding a message to EVERY assertion in a method removes that method from
the count. Do not split methods to fix AR.

**Lazy Test (LT):** for each production method M, if M is invoked from 2 or
more DIFFERENT test methods, TsDetect adds M's TOTAL invocation count (calls
inside the same method count multiple times) to the LT score. If M is invoked
from only one test method it contributes 0.

Consequences you MUST follow:

1. Splitting a test method into several smaller methods INCREASES LT, because
   more test methods then share the same production method. Do not split to fix LT.
2. To reduce LT, MERGE test methods that observe the same production method
   (typically toString(), peek(), get(), size(), etc.) so each production method
   is invoked from exactly one test method.

## Procedure

1. List each production method and count how many test methods call it.
   Identify methods called from 2+ test methods - these are the LT source.
2. For AR: add a clear, descriptive failure message to EVERY assertion.
3. For LT: group and merge test methods by the production method they observe.
   After merging, each production method must be invoked from a single test method.
4. A merged method may contain many assertions; that is acceptable, but every
   assertion must carry a message, otherwise that method trips AR.

## Hard constraints (any violation is a failure)

- Do not delete or change ANY assertion: predicate, expected value, comparison,
  or input literal. The total assertion count after refactoring must equal the original.
- Do not delete or change any test input, constructor argument, or string/char literal.
- Do not change expected-exception behaviour or the setup/act/assert order.
- Do not merge two different scenarios' expectations into one assertion.
- Do not move assertions into helper or private methods to hide them from the detector.
- Keep JUnit 4 @Test annotations and imports; no parameterized tests, no new
  dependencies; keep the package and the class name unchanged.
- Do not modify the production class.

## Output format

1. A table: production method -> test methods calling it before -> after.
2. A list of each new test method and which original methods it absorbed.
3. The COMPLETE refactored Java file, from `package` to the final `}`, with no
   `// ...` or truncation.
4. State the assertion count before and after; they must be equal.

If some LT source cannot be merged without changing semantics, say which and
why. If the semantics are ambiguous, say so; do not invent expected values.

## Files

Sample R03 - baseline AR=13, LT=11, LOC=172.

Test class:

```java
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
```

Production class:

```java
package org.jsoup.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * A minimal String utility class. Designed for internal jsoup use only.
 */
public final class StringUtil {
    // memoised padding up to 21
    static final String[] padding = {"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ",
        "         ", "          ", "           ", "            ", "             ", "              ", "               ",
        "                ", "                 ", "                  ", "                   ", "                    "};

    /**
     * Join a collection of strings by a separator
     * @param strings collection of string objects
     * @param sep string to place between strings
     * @return joined string
     */
    public static String join(Collection strings, String sep) {
        return join(strings.iterator(), sep);
    }

    /**
     * Join a collection of strings by a separator
     * @param strings iterator of string objects
     * @param sep string to place between strings
     * @return joined string
     */
    public static String join(Iterator strings, String sep) {
        if (!strings.hasNext())
            return "";

        String start = strings.next().toString();
        if (!strings.hasNext()) // only one, avoid builder
            return start;

        StringBuilder sb = new StringBuilder(64).append(start);
        while (strings.hasNext()) {
            sb.append(sep);
            sb.append(strings.next());
        }
        return sb.toString();
    }

    /**
     * Join an array of strings by a separator
     * @param strings collection of string objects
     * @param sep string to place between strings
     * @return joined string
     */
    public static String join(String[] strings, String sep) {
        return join(Arrays.asList(strings), sep);
    }

    /**
     * Returns space padding
     * @param width amount of padding desired
     * @return string of spaces * width
     */
    public static String padding(int width) {
        if (width < 0)
            throw new IllegalArgumentException("width must be > 0");

        if (width < padding.length)
            return padding[width];
        char[] out = new char[width];
        for (int i = 0; i < width; i++)
            out[i] = ' ';
        return String.valueOf(out);
    }

    /**
     * Tests if a string is blank: null, empty, or only whitespace (" ", \r\n, \t, etc)
     * @param string string to test
     * @return if string is blank
     */
    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0)
            return true;

        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!StringUtil.isWhitespace(string.codePointAt(i)))
                return false;
        }
        return true;
    }

    /**
     * Tests if a string is numeric, i.e. contains only digit characters
     * @param string string to test
     * @return true if only digit chars, false if empty or null or contains non-digit chars
     */
    public static boolean isNumeric(String string) {
        if (string == null || string.length() == 0)
            return false;

        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!Character.isDigit(string.codePointAt(i)))
                return false;
        }
        return true;
    }

    /**
     * Tests if a code point is "whitespace" as defined in the HTML spec. Used for output HTML.
     * @param c code point to test
     * @return true if code point is whitespace, false otherwise
     * @see #isActuallyWhitespace(int)
     */
    public static boolean isWhitespace(int c){
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
    }

    /**
     * Tests if a code point is "whitespace" as defined by what it looks like. Used for Element.text etc.
     * @param c code point to test
     * @return true if code point is whitespace, false otherwise
     */
    public static boolean isActuallyWhitespace(int c){
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r' || c == 160;
        // 160 is &nbsp; (non-breaking space). Not in the spec but expected.
    }

        // zero width sp, zw non join, zw join, soft hyphen

    /**
     * Normalise the whitespace within this string; multiple spaces collapse to a single, and all whitespace characters
     * (e.g. newline, tab) convert to a simple space
     * @param string content to normalise
     * @return normalised string
     */
    public static String normaliseWhitespace(String string) {
        StringBuilder sb = StringUtil.stringBuilder();
        appendNormalisedWhitespace(sb, string, false);
        return sb.toString();
    }

    /**
     * After normalizing the whitespace within a string, appends it to a string builder.
     * @param accum builder to append to
     * @param string string to normalize whitespace within
     * @param stripLeading set to true if you wish to remove any leading whitespace
     */
    public static void appendNormalisedWhitespace(StringBuilder accum, String string, boolean stripLeading) {
        boolean lastWasWhite = false;
        boolean reachedNonWhite = false;

        int len = string.length();
        int c;
        for (int i = 0; i < len; i+= Character.charCount(c)) {
            c = string.codePointAt(i);
            if (isActuallyWhitespace(c)) {
                if ((stripLeading && !reachedNonWhite) || lastWasWhite)
                    continue;
                accum.append(' ');
                lastWasWhite = true;
            }
            else {
                accum.appendCodePoint(c);
                lastWasWhite = false;
                reachedNonWhite = true;
            }
        }
    }

    public static boolean in(final String needle, final String... haystack) {
        final int len = haystack.length;
        for (int i = 0; i < len; i++) {
            if (haystack[i].equals(needle))
            return true;
        }
        return false;
    }

    public static boolean inSorted(String needle, String[] haystack) {
        return Arrays.binarySearch(haystack, needle) >= 0;
    }

    /**
     * Create a new absolute URL, from a provided existing absolute URL and a relative URL component.
     * @param base the existing absolute base URL
     * @param relUrl the relative URL to resolve. (If it's already absolute, it will be returned)
     * @return the resolved absolute URL
     * @throws MalformedURLException if an error occurred generating the URL
     */
    public static URL resolve(URL base, String relUrl) throws MalformedURLException {
        // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
        if (relUrl.startsWith("?"))
            relUrl = base.getPath() + relUrl;
        // workaround: //example.com + ./foo = //example.com/./foo, not //example.com/foo
        if (relUrl.indexOf('.') == 0 && base.getFile().indexOf('/') != 0) {
            base = new URL(base.getProtocol(), base.getHost(), base.getPort(), "/" + base.getFile());
        }
        return new URL(base, relUrl);
    }

    /**
     * Create a new absolute URL, from a provided existing absolute URL and a relative URL component.
     * @param baseUrl the existing absolute base URL
     * @param relUrl the relative URL to resolve. (If it's already absolute, it will be returned)
     * @return an absolute URL if one was able to be generated, or the empty string if not
     */
    public static String resolve(final String baseUrl, final String relUrl) {
        URL base;
        try {
            try {
                base = new URL(baseUrl);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute/rel may be abs on its own, so try that
                URL abs = new URL(relUrl);
                return abs.toExternalForm();
            }
            return resolve(base, relUrl).toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    /**
     * Maintains a cached StringBuilder, to minimize new StringBuilder GCs. Prevents it from growing to big per thread.
     * Care must be taken to not grab more than one in the same stack (not locked or mutexed or anything).
     * @return an empty StringBuilder
     */
    public static StringBuilder stringBuilder() {
        StringBuilder sb = stringLocal.get();
        if (sb.length() > MaxCachedBuilderSize) {
            sb = new StringBuilder(MaxCachedBuilderSize);
            stringLocal.set(sb);
        } else {
            sb.delete(0, sb.length());
        }
        return sb;

    }

    private static final int MaxCachedBuilderSize = 8 * 1024;
    private static final ThreadLocal<StringBuilder> stringLocal = new ThreadLocal<StringBuilder>(){
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder(MaxCachedBuilderSize);
        }
    };


}
```

