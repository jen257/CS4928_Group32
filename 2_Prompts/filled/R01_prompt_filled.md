# Master Refactoring Prompt (RQ2)

Paste this prompt to the AI, with `R01`, `23`, `21`, `172`,
`package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tq = new TokenQueue("");
        assertTrue(tq.isEmpty());

        tq = new TokenQueue("abcdef");
        assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertEquals('a', tq.peek());

        tq = new TokenQueue("");
        assertEquals(0, tq.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("world");
        tq.addFirst("hello ");
        assertEquals("hello world", tq.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue(tq.matches("abc"));
        assertFalse(tq.matches("123"));

        tq = new TokenQueue("");
        assertFalse(tq.matches("abc"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue(tq.matchesCS("abc"));
        assertFalse(tq.matchesCS("ABC"));

        tq = new TokenQueue("");
        assertFalse(tq.matchesCS("abc"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue(tq.matchesAny("abc", "123"));
        assertFalse(tq.matchesAny("def", "123"));

        tq = new TokenQueue("");
        assertFalse(tq.matchesAny("abc", "123"));
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue(tq.matchChomp("abc"));
        assertEquals("xyz", tq.toString());

        tq = new TokenQueue("abcxyz");
        assertFalse(tq.matchChomp("xyz"));
        assertEquals("abcxyz", tq.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tq = new TokenQueue(" abc xyz");
        assertTrue(tq.matchesWhitespace());

        tq = new TokenQueue("abcxyz");
        assertFalse(tq.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue(tq.matchesWord());

        tq = new TokenQueue("123xyz");
        assertTrue(tq.matchesWord());

        tq = new TokenQueue("-xyz");
        assertFalse(tq.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tq = new TokenQueue("abc");
        tq.advance();
        assertEquals('b', tq.peek());

        tq = new TokenQueue("");
        tq.advance();
        assertEquals(0, tq.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue tq = new TokenQueue("abc");
        assertEquals('a', tq.consume());
        assertEquals('b', tq.peek());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertEquals("abc", tq.consumeTo("x"));
        assertEquals("xyz", tq.toString());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tq = new TokenQueue("abcXYZ");
        assertEquals("abc", tq.consumeToIgnoreCase("X"));
        assertEquals("XYZ", tq.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertEquals("abc", tq.consumeToAny("x", "123"));
        assertEquals("xyz", tq.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertEquals("abc", tq.chompTo("x"));
        assertEquals("yz", tq.toString());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tq = new TokenQueue("abcXYZ");
        assertEquals("abc", tq.chompToIgnoreCase("X"));
        assertEquals("YZ", tq.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tq = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tq.chompBalanced('(', ')'));
        assertEquals(" four", tq.toString());
    }

    @Test
    public void testUnescape() {
        String s = TokenQueue.unescape("hello\\\"world\\\"");
        assertEquals("hello\"world\"", s);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tq = new TokenQueue("   abc");
        assertTrue(tq.consumeWhitespace());
        assertEquals("abc", tq.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tq = new TokenQueue("abc_123");
        assertEquals("abc", tq.consumeWord());
        assertEquals("_123", tq.toString());
    }
    
    @Test
    public void testConsumeTagName() {
        TokenQueue tq = new TokenQueue("div3_5");
        assertEquals("div3_5", tq.consumeTagName());
        assertEquals("", tq.toString());
    }
    
    @Test
    public void testConsumeElementSelector() {
        TokenQueue tq = new TokenQueue("div3_5");
        assertEquals("div3_5", tq.consumeElementSelector());
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tq = new TokenQueue("abc-123");
        assertEquals("abc-123", tq.consumeCssIdentifier());
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tq = new TokenQueue("data-jsoup=\"true\"");
        assertEquals("data-jsoup", tq.consumeAttributeKey());
        assertEquals("=\"true\"", tq.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tq = new TokenQueue("abc");
        assertEquals("abc", tq.remainder());
        assertEquals("", tq.toString());
    }
}` and `package org.jsoup.parser;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

/**
 * A character queue with parsing helpers.
 *
 * @author Jonathan Hedley
 */
public class TokenQueue {
    private String queue;
    private int pos = 0;
    
    private static final char ESC = '\\'; // escape char for chomp balanced.

    /**
     Create a new TokenQueue.
     @param data string of data to back queue.
     */
    public TokenQueue(String data) {
        Validate.notNull(data);
        queue = data;
    }

    /**
     * Is the queue empty?
     * @return true if no data left in queue.
     */
    public boolean isEmpty() {
        return remainingLength() == 0;
    }
    
    private int remainingLength() {
        return queue.length() - pos;
    }

    /**
     * Retrieves but does not remove the first character from the queue.
     * @return First character, or 0 if empty.
     */
    public char peek() {
        return isEmpty() ? 0 : queue.charAt(pos);
    }

    /**
     Add a character to the start of the queue (will be the next character retrieved).
     @param c character to add
     */
    public void addFirst(Character c) {
        addFirst(c.toString());
    }

    /**
     Add a string to the start of the queue.
     @param seq string to add.
     */
    public void addFirst(String seq) {
        // not very performant, but an edge case
        queue = seq + queue.substring(pos);
        pos = 0;
    }

    /**
     * Tests if the next characters on the queue match the sequence. Case insensitive.
     * @param seq String to check queue for.
     * @return true if the next characters match.
     */
    public boolean matches(String seq) {
        return queue.regionMatches(true, pos, seq, 0, seq.length());
    }

    /**
     * Case sensitive match test.
     * @param seq string to case sensitively check for
     * @return true if matched, false if not
     */
    public boolean matchesCS(String seq) {
        return queue.startsWith(seq, pos);
    }
    

    /**
     Tests if the next characters match any of the sequences. Case insensitive.
     @param seq list of strings to case insensitively check for
     @return true of any matched, false if none did
     */
    public boolean matchesAny(String... seq) {
        for (String s : seq) {
            if (matches(s))
                return true;
        }
        return false;
    }

    public boolean matchesAny(char... seq) {
        if (isEmpty())
            return false;

        for (char c: seq) {
            if (queue.charAt(pos) == c)
                return true;
        }
        return false;
    }

    public boolean matchesStartTag() {
        // micro opt for matching "<x"
        return (remainingLength() >= 2 && queue.charAt(pos) == '<' && Character.isLetter(queue.charAt(pos+1)));
    }

    /**
     * Tests if the queue matches the sequence (as with match), and if they do, removes the matched string from the
     * queue.
     * @param seq String to search for, and if found, remove from queue.
     * @return true if found and removed, false if not found.
     */
    public boolean matchChomp(String seq) {
        if (matches(seq)) {
            pos += seq.length();
            return true;
        } else {
            return false;
        }
    }

    /**
     Tests if queue starts with a whitespace character.
     @return if starts with whitespace
     */
    public boolean matchesWhitespace() {
        return !isEmpty() && StringUtil.isWhitespace(queue.charAt(pos));
    }

    /**
     Test if the queue matches a word character (letter or digit).
     @return if matches a word character
     */
    public boolean matchesWord() {
        return !isEmpty() && Character.isLetterOrDigit(queue.charAt(pos));
    }

    /**
     * Drops the next character off the queue.
     */
    public void advance() {
        if (!isEmpty()) pos++;
    }

    /**
     * Consume one character off queue.
     * @return first character on queue.
     */
    public char consume() {
        return queue.charAt(pos++);
    }

    /**
     * Consumes the supplied sequence of the queue. If the queue does not start with the supplied sequence, will
     * throw an illegal state exception -- but you should be running match() against that condition.
     <p>
     Case insensitive.
     * @param seq sequence to remove from head of queue.
     */
    public void consume(String seq) {
        if (!matches(seq))
            throw new IllegalStateException("Queue did not match expected sequence");
        int len = seq.length();
        if (len > remainingLength())
            throw new IllegalStateException("Queue not long enough to consume sequence");
        
        pos += len;
    }

    /**
     * Pulls a string off the queue, up to but exclusive of the match sequence, or to the queue running out.
     * @param seq String to end on (and not include in return, but leave on queue). <b>Case sensitive.</b>
     * @return The matched data consumed from queue.
     */
    public String consumeTo(String seq) {
        int offset = queue.indexOf(seq, pos);
        if (offset != -1) {
            String consumed = queue.substring(pos, offset);
            pos += consumed.length();
            return consumed;
        } else {
            return remainder();
        }
    }
    
    public String consumeToIgnoreCase(String seq) {
        int start = pos;
        String first = seq.substring(0, 1);
        boolean canScan = first.toLowerCase().equals(first.toUpperCase()); // if first is not cased, use index of
        while (!isEmpty()) {
            if (matches(seq))
                break;
            
            if (canScan) {
                int skip = queue.indexOf(first, pos) - pos;
                if (skip == 0) // this char is the skip char, but not match, so force advance of pos
                    pos++;
                else if (skip < 0) // no chance of finding, grab to end
                    pos = queue.length();
                else
                    pos += skip;
            }
            else
                pos++;
        }

        return queue.substring(start, pos);
    }

    /**
     Consumes to the first sequence provided, or to the end of the queue. Leaves the terminator on the queue.
     @param seq any number of terminators to consume to. <b>Case insensitive.</b>
     @return consumed string   
     */
    // todo: method name. not good that consumeTo cares for case, and consume to any doesn't. And the only use for this
    // is is a case sensitive time...
    public String consumeToAny(String... seq) {
        int start = pos;
        while (!isEmpty() && !matchesAny(seq)) {
            pos++;
        }

        return queue.substring(start, pos);
    }

    /**
     * Pulls a string off the queue (like consumeTo), and then pulls off the matched string (but does not return it).
     * <p>
     * If the queue runs out of characters before finding the seq, will return as much as it can (and queue will go
     * isEmpty() == true).
     * @param seq String to match up to, and not include in return, and to pull off queue. <b>Case sensitive.</b>
     * @return Data matched from queue.
     */
    public String chompTo(String seq) {
        String data = consumeTo(seq);
        matchChomp(seq);
        return data;
    }
    
    public String chompToIgnoreCase(String seq) {
        String data = consumeToIgnoreCase(seq); // case insensitive scan
        matchChomp(seq);
        return data;
    }

    /**
     * Pulls a balanced string off the queue. E.g. if queue is "(one (two) three) four", (,) will return "one (two) three",
     * and leave " four" on the queue. Unbalanced openers and closers can quoted (with ' or ") or escaped (with \). Those escapes will be left
     * in the returned string, which is suitable for regexes (where we need to preserve the escape), but unsuitable for
     * contains text strings; use unescape for that.
     * @param open opener
     * @param close closer
     * @return data matched from the queue
     */
    public String chompBalanced(char open, char close) {
        int start = -1;
        int end = -1;
        int depth = 0;
        char last = 0;

        do {
            if (isEmpty()) break;
            Character c = consume();
            if (last == 0 || last != ESC) {
                if (c.equals(open)) {
                    depth++;
                    if (start == -1)
                        start = pos;
                }
                else if (c.equals(close))
                    depth--;
            }

            if (depth > 0 && last != 0)
                end = pos; // don't include the outer match pair in the return
            last = c;
        } while (depth > 0);
        return (end >= 0) ? queue.substring(start, end) : "";
    }
    
    /**
     * Unescaped a \ escaped string.
     * @param in backslash escaped string
     * @return unescaped string
     */
    public static String unescape(String in) {
        StringBuilder out = new StringBuilder();
        char last = 0;
        for (char c : in.toCharArray()) {
            if (c == ESC) {
                if (last != 0 && last == ESC)
                    out.append(c);
            }
            else 
                out.append(c);
            last = c;
        }
        return out.toString();
    }

    /**
     * Pulls the next run of whitespace characters of the queue.
     * @return Whether consuming whitespace or not
     */
    public boolean consumeWhitespace() {
        boolean seen = false;
        while (matchesWhitespace()) {
            pos++;
            seen = true;
        }
        return seen;
    }

    /**
     * Retrieves the next run of word type (letter or digit) off the queue.
     * @return String of word characters from queue, or empty string if none.
     */
    public String consumeWord() {
        int start = pos;
        while (matchesWord())
            pos++;
        return queue.substring(start, pos);
    }
    
    /**
     * Consume an tag name off the queue (word or :, _, -)
     * 
     * @return tag name
     */
    public String consumeTagName() {
        int start = pos;
        while (!isEmpty() && (matchesWord() || matchesAny(':', '_', '-')))
            pos++;
        
        return queue.substring(start, pos);
    }
    
    /**
     * Consume a CSS element selector (tag name, but | instead of : for namespaces, to not conflict with :pseudo selects).
     * 
     * @return tag name
     */
    public String consumeElementSelector() {
        int start = pos;
        while (!isEmpty() && (matchesWord() || matchesAny('|', '_', '-')))
            pos++;
        
        return queue.substring(start, pos);
    }

    /**
     Consume a CSS identifier (ID or class) off the queue (letter, digit, -, _)
     http://www.w3.org/TR/CSS2/syndata.html#value-def-identifier
     @return identifier
     */
    public String consumeCssIdentifier() {
        int start = pos;
        while (!isEmpty() && (matchesWord() || matchesAny('-', '_')))
            pos++;

        return queue.substring(start, pos);
    }

    /**
     Consume an attribute key off the queue (letter, digit, -, _, :")
     @return attribute key
     */
    public String consumeAttributeKey() {
        int start = pos;
        while (!isEmpty() && (matchesWord() || matchesAny('-', '_', ':')))
            pos++;
        
        return queue.substring(start, pos);
    }

    /**
     Consume and return whatever is left on the queue.
     @return remained of queue.
     */
    public String remainder() {
        final String remainder = queue.substring(pos, queue.length());
        pos = queue.length();
        return remainder;
    }
    
    @Override
    public String toString() {
        return queue.substring(pos);
    }
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

Sample R01 - baseline AR=23, LT=21, LOC=172.

Test class:

```java
package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tq = new TokenQueue("");
        assertTrue(tq.isEmpty());

        tq = new TokenQueue("abcdef");
        assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertEquals('a', tq.peek());

        tq = new TokenQueue("");
        assertEquals(0, tq.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("world");
        tq.addFirst("hello ");
        assertEquals("hello world", tq.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue(tq.matches("abc"));
        assertFalse(tq.matches("123"));

        tq = new TokenQueue("");
        assertFalse(tq.matches("abc"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue(tq.matchesCS("abc"));
        assertFalse(tq.matchesCS("ABC"));

        tq = new TokenQueue("");
        assertFalse(tq.matchesCS("abc"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue(tq.matchesAny("abc", "123"));
        assertFalse(tq.matchesAny("def", "123"));

        tq = new TokenQueue("");
        assertFalse(tq.matchesAny("abc", "123"));
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue(tq.matchChomp("abc"));
        assertEquals("xyz", tq.toString());

        tq = new TokenQueue("abcxyz");
        assertFalse(tq.matchChomp("xyz"));
        assertEquals("abcxyz", tq.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tq = new TokenQueue(" abc xyz");
        assertTrue(tq.matchesWhitespace());

        tq = new TokenQueue("abcxyz");
        assertFalse(tq.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue(tq.matchesWord());

        tq = new TokenQueue("123xyz");
        assertTrue(tq.matchesWord());

        tq = new TokenQueue("-xyz");
        assertFalse(tq.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tq = new TokenQueue("abc");
        tq.advance();
        assertEquals('b', tq.peek());

        tq = new TokenQueue("");
        tq.advance();
        assertEquals(0, tq.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue tq = new TokenQueue("abc");
        assertEquals('a', tq.consume());
        assertEquals('b', tq.peek());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertEquals("abc", tq.consumeTo("x"));
        assertEquals("xyz", tq.toString());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tq = new TokenQueue("abcXYZ");
        assertEquals("abc", tq.consumeToIgnoreCase("X"));
        assertEquals("XYZ", tq.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertEquals("abc", tq.consumeToAny("x", "123"));
        assertEquals("xyz", tq.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertEquals("abc", tq.chompTo("x"));
        assertEquals("yz", tq.toString());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tq = new TokenQueue("abcXYZ");
        assertEquals("abc", tq.chompToIgnoreCase("X"));
        assertEquals("YZ", tq.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tq = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tq.chompBalanced('(', ')'));
        assertEquals(" four", tq.toString());
    }

    @Test
    public void testUnescape() {
        String s = TokenQueue.unescape("hello\\\"world\\\"");
        assertEquals("hello\"world\"", s);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tq = new TokenQueue("   abc");
        assertTrue(tq.consumeWhitespace());
        assertEquals("abc", tq.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tq = new TokenQueue("abc_123");
        assertEquals("abc", tq.consumeWord());
        assertEquals("_123", tq.toString());
    }
    
    @Test
    public void testConsumeTagName() {
        TokenQueue tq = new TokenQueue("div3_5");
        assertEquals("div3_5", tq.consumeTagName());
        assertEquals("", tq.toString());
    }
    
    @Test
    public void testConsumeElementSelector() {
        TokenQueue tq = new TokenQueue("div3_5");
        assertEquals("div3_5", tq.consumeElementSelector());
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tq = new TokenQueue("abc-123");
        assertEquals("abc-123", tq.consumeCssIdentifier());
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tq = new TokenQueue("data-jsoup=\"true\"");
        assertEquals("data-jsoup", tq.consumeAttributeKey());
        assertEquals("=\"true\"", tq.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tq = new TokenQueue("abc");
        assertEquals("abc", tq.remainder());
        assertEquals("", tq.toString());
    }
}
```

Production class:

```java
package org.jsoup.parser;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

/**
 * A character queue with parsing helpers.
 *
 * @author Jonathan Hedley
 */
public class TokenQueue {
    private String queue;
    private int pos = 0;
    
    private static final char ESC = '\\'; // escape char for chomp balanced.

    /**
     Create a new TokenQueue.
     @param data string of data to back queue.
     */
    public TokenQueue(String data) {
        Validate.notNull(data);
        queue = data;
    }

    /**
     * Is the queue empty?
     * @return true if no data left in queue.
     */
    public boolean isEmpty() {
        return remainingLength() == 0;
    }
    
    private int remainingLength() {
        return queue.length() - pos;
    }

    /**
     * Retrieves but does not remove the first character from the queue.
     * @return First character, or 0 if empty.
     */
    public char peek() {
        return isEmpty() ? 0 : queue.charAt(pos);
    }

    /**
     Add a character to the start of the queue (will be the next character retrieved).
     @param c character to add
     */
    public void addFirst(Character c) {
        addFirst(c.toString());
    }

    /**
     Add a string to the start of the queue.
     @param seq string to add.
     */
    public void addFirst(String seq) {
        // not very performant, but an edge case
        queue = seq + queue.substring(pos);
        pos = 0;
    }

    /**
     * Tests if the next characters on the queue match the sequence. Case insensitive.
     * @param seq String to check queue for.
     * @return true if the next characters match.
     */
    public boolean matches(String seq) {
        return queue.regionMatches(true, pos, seq, 0, seq.length());
    }

    /**
     * Case sensitive match test.
     * @param seq string to case sensitively check for
     * @return true if matched, false if not
     */
    public boolean matchesCS(String seq) {
        return queue.startsWith(seq, pos);
    }
    

    /**
     Tests if the next characters match any of the sequences. Case insensitive.
     @param seq list of strings to case insensitively check for
     @return true of any matched, false if none did
     */
    public boolean matchesAny(String... seq) {
        for (String s : seq) {
            if (matches(s))
                return true;
        }
        return false;
    }

    public boolean matchesAny(char... seq) {
        if (isEmpty())
            return false;

        for (char c: seq) {
            if (queue.charAt(pos) == c)
                return true;
        }
        return false;
    }

    public boolean matchesStartTag() {
        // micro opt for matching "<x"
        return (remainingLength() >= 2 && queue.charAt(pos) == '<' && Character.isLetter(queue.charAt(pos+1)));
    }

    /**
     * Tests if the queue matches the sequence (as with match), and if they do, removes the matched string from the
     * queue.
     * @param seq String to search for, and if found, remove from queue.
     * @return true if found and removed, false if not found.
     */
    public boolean matchChomp(String seq) {
        if (matches(seq)) {
            pos += seq.length();
            return true;
        } else {
            return false;
        }
    }

    /**
     Tests if queue starts with a whitespace character.
     @return if starts with whitespace
     */
    public boolean matchesWhitespace() {
        return !isEmpty() && StringUtil.isWhitespace(queue.charAt(pos));
    }

    /**
     Test if the queue matches a word character (letter or digit).
     @return if matches a word character
     */
    public boolean matchesWord() {
        return !isEmpty() && Character.isLetterOrDigit(queue.charAt(pos));
    }

    /**
     * Drops the next character off the queue.
     */
    public void advance() {
        if (!isEmpty()) pos++;
    }

    /**
     * Consume one character off queue.
     * @return first character on queue.
     */
    public char consume() {
        return queue.charAt(pos++);
    }

    /**
     * Consumes the supplied sequence of the queue. If the queue does not start with the supplied sequence, will
     * throw an illegal state exception -- but you should be running match() against that condition.
     <p>
     Case insensitive.
     * @param seq sequence to remove from head of queue.
     */
    public void consume(String seq) {
        if (!matches(seq))
            throw new IllegalStateException("Queue did not match expected sequence");
        int len = seq.length();
        if (len > remainingLength())
            throw new IllegalStateException("Queue not long enough to consume sequence");
        
        pos += len;
    }

    /**
     * Pulls a string off the queue, up to but exclusive of the match sequence, or to the queue running out.
     * @param seq String to end on (and not include in return, but leave on queue). <b>Case sensitive.</b>
     * @return The matched data consumed from queue.
     */
    public String consumeTo(String seq) {
        int offset = queue.indexOf(seq, pos);
        if (offset != -1) {
            String consumed = queue.substring(pos, offset);
            pos += consumed.length();
            return consumed;
        } else {
            return remainder();
        }
    }
    
    public String consumeToIgnoreCase(String seq) {
        int start = pos;
        String first = seq.substring(0, 1);
        boolean canScan = first.toLowerCase().equals(first.toUpperCase()); // if first is not cased, use index of
        while (!isEmpty()) {
            if (matches(seq))
                break;
            
            if (canScan) {
                int skip = queue.indexOf(first, pos) - pos;
                if (skip == 0) // this char is the skip char, but not match, so force advance of pos
                    pos++;
                else if (skip < 0) // no chance of finding, grab to end
                    pos = queue.length();
                else
                    pos += skip;
            }
            else
                pos++;
        }

        return queue.substring(start, pos);
    }

    /**
     Consumes to the first sequence provided, or to the end of the queue. Leaves the terminator on the queue.
     @param seq any number of terminators to consume to. <b>Case insensitive.</b>
     @return consumed string   
     */
    // todo: method name. not good that consumeTo cares for case, and consume to any doesn't. And the only use for this
    // is is a case sensitive time...
    public String consumeToAny(String... seq) {
        int start = pos;
        while (!isEmpty() && !matchesAny(seq)) {
            pos++;
        }

        return queue.substring(start, pos);
    }

    /**
     * Pulls a string off the queue (like consumeTo), and then pulls off the matched string (but does not return it).
     * <p>
     * If the queue runs out of characters before finding the seq, will return as much as it can (and queue will go
     * isEmpty() == true).
     * @param seq String to match up to, and not include in return, and to pull off queue. <b>Case sensitive.</b>
     * @return Data matched from queue.
     */
    public String chompTo(String seq) {
        String data = consumeTo(seq);
        matchChomp(seq);
        return data;
    }
    
    public String chompToIgnoreCase(String seq) {
        String data = consumeToIgnoreCase(seq); // case insensitive scan
        matchChomp(seq);
        return data;
    }

    /**
     * Pulls a balanced string off the queue. E.g. if queue is "(one (two) three) four", (,) will return "one (two) three",
     * and leave " four" on the queue. Unbalanced openers and closers can quoted (with ' or ") or escaped (with \). Those escapes will be left
     * in the returned string, which is suitable for regexes (where we need to preserve the escape), but unsuitable for
     * contains text strings; use unescape for that.
     * @param open opener
     * @param close closer
     * @return data matched from the queue
     */
    public String chompBalanced(char open, char close) {
        int start = -1;
        int end = -1;
        int depth = 0;
        char last = 0;

        do {
            if (isEmpty()) break;
            Character c = consume();
            if (last == 0 || last != ESC) {
                if (c.equals(open)) {
                    depth++;
                    if (start == -1)
                        start = pos;
                }
                else if (c.equals(close))
                    depth--;
            }

            if (depth > 0 && last != 0)
                end = pos; // don't include the outer match pair in the return
            last = c;
        } while (depth > 0);
        return (end >= 0) ? queue.substring(start, end) : "";
    }
    
    /**
     * Unescaped a \ escaped string.
     * @param in backslash escaped string
     * @return unescaped string
     */
    public static String unescape(String in) {
        StringBuilder out = new StringBuilder();
        char last = 0;
        for (char c : in.toCharArray()) {
            if (c == ESC) {
                if (last != 0 && last == ESC)
                    out.append(c);
            }
            else 
                out.append(c);
            last = c;
        }
        return out.toString();
    }

    /**
     * Pulls the next run of whitespace characters of the queue.
     * @return Whether consuming whitespace or not
     */
    public boolean consumeWhitespace() {
        boolean seen = false;
        while (matchesWhitespace()) {
            pos++;
            seen = true;
        }
        return seen;
    }

    /**
     * Retrieves the next run of word type (letter or digit) off the queue.
     * @return String of word characters from queue, or empty string if none.
     */
    public String consumeWord() {
        int start = pos;
        while (matchesWord())
            pos++;
        return queue.substring(start, pos);
    }
    
    /**
     * Consume an tag name off the queue (word or :, _, -)
     * 
     * @return tag name
     */
    public String consumeTagName() {
        int start = pos;
        while (!isEmpty() && (matchesWord() || matchesAny(':', '_', '-')))
            pos++;
        
        return queue.substring(start, pos);
    }
    
    /**
     * Consume a CSS element selector (tag name, but | instead of : for namespaces, to not conflict with :pseudo selects).
     * 
     * @return tag name
     */
    public String consumeElementSelector() {
        int start = pos;
        while (!isEmpty() && (matchesWord() || matchesAny('|', '_', '-')))
            pos++;
        
        return queue.substring(start, pos);
    }

    /**
     Consume a CSS identifier (ID or class) off the queue (letter, digit, -, _)
     http://www.w3.org/TR/CSS2/syndata.html#value-def-identifier
     @return identifier
     */
    public String consumeCssIdentifier() {
        int start = pos;
        while (!isEmpty() && (matchesWord() || matchesAny('-', '_')))
            pos++;

        return queue.substring(start, pos);
    }

    /**
     Consume an attribute key off the queue (letter, digit, -, _, :")
     @return attribute key
     */
    public String consumeAttributeKey() {
        int start = pos;
        while (!isEmpty() && (matchesWord() || matchesAny('-', '_', ':')))
            pos++;
        
        return queue.substring(start, pos);
    }

    /**
     Consume and return whatever is left on the queue.
     @return remained of queue.
     */
    public String remainder() {
        final String remainder = queue.substring(pos, queue.length());
        pos = queue.length();
        return remainder;
    }
    
    @Override
    public String toString() {
        return queue.substring(pos);
    }
}
```

