package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tq = new TokenQueue("");
        assertTrue("Expected empty TokenQueue to return true for isEmpty()", tq.isEmpty());

        tq = new TokenQueue("abcdef");
        assertFalse("Expected non-empty TokenQueue to return false for isEmpty()", tq.isEmpty());
    }

    @Test
    public void testPeekAdvanceAndConsume() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertEquals("Expected peek() to return first character 'a'", 'a', tq.peek());

        tq = new TokenQueue("");
        assertEquals("Expected peek() on empty queue to return 0", 0, tq.peek());

        tq = new TokenQueue("abc");
        tq.advance();
        assertEquals("Expected peek() after advance() to return 'b'", 'b', tq.peek());

        tq = new TokenQueue("");
        tq.advance();
        assertEquals("Expected peek() after advance() on empty queue to return 0", 0, tq.peek());

        tq = new TokenQueue("abc");
        assertEquals("Expected consume() to return first character 'a'", 'a', tq.consume());
        assertEquals("Expected peek() after consume() to return 'b'", 'b', tq.peek());
    }

    @Test
    public void testToStringOperations() {
        TokenQueue tq = new TokenQueue("world");
        tq.addFirst("hello ");
        assertEquals("Expected queue to contain prepended string after addFirst()", "hello world", tq.toString());

        tq = new TokenQueue("abcxyz");
        assertTrue("Expected matchChomp('abc') to succeed and return true", tq.matchChomp("abc"));
        assertEquals("Expected remaining queue to be 'xyz' after matching matchChomp()", "xyz", tq.toString());

        tq = new TokenQueue("abcxyz");
        assertFalse("Expected matchChomp('xyz') at non-matching prefix to return false", tq.matchChomp("xyz"));
        assertEquals("Expected queue to remain unchanged after failed matchChomp()", "abcxyz", tq.toString());

        tq = new TokenQueue("abcxyz");
        assertEquals("Expected consumeTo('x') to return substring before 'x'", "abc", tq.consumeTo("x"));
        assertEquals("Expected remaining queue to start at 'x' after consumeTo()", "xyz", tq.toString());

        tq = new TokenQueue("abcXYZ");
        assertEquals("Expected consumeToIgnoreCase('X') to return 'abc'", "abc", tq.consumeToIgnoreCase("X"));
        assertEquals("Expected remaining queue to be 'XYZ' after consumeToIgnoreCase()", "XYZ", tq.toString());

        tq = new TokenQueue("abcxyz");
        assertEquals("Expected consumeToAny('x', '123') to return 'abc'", "abc", tq.consumeToAny("x", "123"));
        assertEquals("Expected remaining queue to be 'xyz' after consumeToAny()", "xyz", tq.toString());

        tq = new TokenQueue("abcxyz");
        assertEquals("Expected chompTo('x') to return substring before 'x'", "abc", tq.chompTo("x"));
        assertEquals("Expected remaining queue to exclude 'x' after chompTo()", "yz", tq.toString());

        tq = new TokenQueue("abcXYZ");
        assertEquals("Expected chompToIgnoreCase('X') to return 'abc'", "abc", tq.chompToIgnoreCase("X"));
        assertEquals("Expected remaining queue to exclude 'X' after chompToIgnoreCase()", "YZ", tq.toString());

        tq = new TokenQueue("(one (two) three) four");
        assertEquals("Expected chompBalanced('(', ')') to return balanced inner content", "one (two) three", tq.chompBalanced('(', ')'));
        assertEquals("Expected remaining queue after chompBalanced() to be ' four'", " four", tq.toString());

        tq = new TokenQueue("   abc");
        assertTrue("Expected consumeWhitespace() to return true when leading whitespace is present", tq.consumeWhitespace());
        assertEquals("Expected remaining queue after consumeWhitespace() to be 'abc'", "abc", tq.toString());

        tq = new TokenQueue("abc_123");
        assertEquals("Expected consumeWord() to return leading word characters 'abc'", "abc", tq.consumeWord());
        assertEquals("Expected remaining queue after consumeWord() to be '_123'", "_123", tq.toString());

        tq = new TokenQueue("div3_5");
        assertEquals("Expected consumeTagName() to consume full tag name 'div3_5'", "div3_5", tq.consumeTagName());
        assertEquals("Expected queue to be empty after consumeTagName()", "", tq.toString());

        tq = new TokenQueue("div3_5");
        assertEquals("Expected consumeElementSelector() to consume 'div3_5'", "div3_5", tq.consumeElementSelector());
        assertEquals("Expected queue to be empty after consumeElementSelector()", "", tq.toString());

        tq = new TokenQueue("abc-123");
        assertEquals("Expected consumeCssIdentifier() to consume 'abc-123'", "abc-123", tq.consumeCssIdentifier());
        assertEquals("Expected queue to be empty after consumeCssIdentifier()", "", tq.toString());

        tq = new TokenQueue("data-jsoup=\"true\"");
        assertEquals("Expected consumeAttributeKey() to return 'data-jsoup'", "data-jsoup", tq.consumeAttributeKey());
        assertEquals("Expected remaining queue after consumeAttributeKey() to be '=\"true\"'", "=\"true\"", tq.toString());

        tq = new TokenQueue("abc");
        assertEquals("Expected remainder() to return all remaining characters 'abc'", "abc", tq.remainder());
        assertEquals("Expected queue to be empty after remainder()", "", tq.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue("Expected matches('abc') to return true for matching prefix", tq.matches("abc"));
        assertFalse("Expected matches('123') to return false for non-matching prefix", tq.matches("123"));

        tq = new TokenQueue("");
        assertFalse("Expected matches('abc') on empty queue to return false", tq.matches("abc"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue("Expected case-sensitive matchesCS('abc') to return true", tq.matchesCS("abc"));
        assertFalse("Expected case-sensitive matchesCS('ABC') to return false", tq.matchesCS("ABC"));

        tq = new TokenQueue("");
        assertFalse("Expected matchesCS('abc') on empty queue to return false", tq.matchesCS("abc"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue("Expected matchesAny('abc', '123') to return true when one sequence matches", tq.matchesAny("abc", "123"));
        assertFalse("Expected matchesAny('def', '123') to return false when no sequence matches", tq.matchesAny("def", "123"));

        tq = new TokenQueue("");
        assertFalse("Expected matchesAny('abc', '123') on empty queue to return false", tq.matchesAny("abc", "123"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tq = new TokenQueue(" abc xyz");
        assertTrue("Expected matchesWhitespace() to return true for leading space", tq.matchesWhitespace());

        tq = new TokenQueue("abcxyz");
        assertFalse("Expected matchesWhitespace() to return false for leading letter", tq.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tq = new TokenQueue("abcxyz");
        assertTrue("Expected matchesWord() to return true for leading letter", tq.matchesWord());

        tq = new TokenQueue("123xyz");
        assertTrue("Expected matchesWord() to return true for leading digit", tq.matchesWord());

        tq = new TokenQueue("-xyz");
        assertFalse("Expected matchesWord() to return false for leading hyphen", tq.matchesWord());
    }

    @Test
    public void testUnescape() {
        String s = TokenQueue.unescape("hello\\\"world\\\"");
        assertEquals("Expected unescape() to strip backslash escapes from quotes", "hello\"world\"", s);
    }
}
