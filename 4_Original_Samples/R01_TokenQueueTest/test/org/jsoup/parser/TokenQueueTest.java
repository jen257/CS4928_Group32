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