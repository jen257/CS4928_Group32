package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {
    
    @Test
    public void testAreEqualWhenBothObjectsAreNull() {
        assertTrue(Equality.areEqual(null, null));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsNull() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }
    
    @Test
    public void testAreEqualWhenBothObjectsAreArrays() {
        assertTrue(Equality.areEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsArray() {
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, new Object()));
        assertFalse(Equality.areEqual(new Object(), new int[] {1, 2, 3}));
    }
    
    @Test
    public void testAreEqualWhenBothObjectsAreEqual() {
        assertTrue(Equality.areEqual("foo", "foo"));
        assertTrue(Equality.areEqual(5, 5));
    }
    
    @Test
    public void testAreEqualWhenBothObjectsAreNotEqual() {
        assertFalse(Equality.areEqual("foo", "bar"));
        assertFalse(Equality.areEqual(5, 10));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsEqualAndOneObjectIsNull() {
        assertFalse(Equality.areEqual("foo", null));
        assertFalse(Equality.areEqual(null, "foo"));
        assertFalse(Equality.areEqual(5, null));
        assertFalse(Equality.areEqual(null, 5));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsEqualAndOneObjectIsArray() {
        assertFalse(Equality.areEqual("foo", new int[] {1, 2, 3}));
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, "foo"));
    }
    
    @Test
    public void testAreArraysEqualWhenArrayLengthsAreEqualAndArrayElementsAreEqual() {
        assertTrue(Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testAreArrayLengthsEqualWhenArrayLengthsAreEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3, 4}));
    }
    
    @Test
    public void testAreArrayElementsEqualWhenArrayElementsAreEqual() {
        assertTrue(Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testIsArrayWhenObjectIsArray() {
        assertTrue(Equality.isArray(new int[] {1, 2, 3}));
    }
    
    @Test
    public void testIsArrayWhenObjectIsNotArray() {
        assertFalse(Equality.isArray("foo"));
    }
    
}