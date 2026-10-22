package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {
    
    @Test
    public void testAreEqual() {
        assertTrue("Expected areEqual(null, null) to return true", Equality.areEqual(null, null));
        
        assertFalse("Expected areEqual(null, new Object()) to return false", Equality.areEqual(null, new Object()));
        assertFalse("Expected areEqual(new Object(), null) to return false", Equality.areEqual(new Object(), null));
        
        assertTrue("Expected areEqual for identical int arrays to return true", Equality.areEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse("Expected areEqual for different int arrays to return false", Equality.areEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
        
        assertFalse("Expected areEqual between int array and Object to return false", Equality.areEqual(new int[] {1, 2, 3}, new Object()));
        assertFalse("Expected areEqual between Object and int array to return false", Equality.areEqual(new Object(), new int[] {1, 2, 3}));
        
        assertTrue("Expected areEqual for equal strings to return true", Equality.areEqual("foo", "foo"));
        assertTrue("Expected areEqual for equal integers to return true", Equality.areEqual(5, 5));
        
        assertFalse("Expected areEqual for different strings to return false", Equality.areEqual("foo", "bar"));
        assertFalse("Expected areEqual for different integers to return false", Equality.areEqual(5, 10));
        
        assertFalse("Expected areEqual(foo, null) to return false", Equality.areEqual("foo", null));
        assertFalse("Expected areEqual(null, foo) to return false", Equality.areEqual(null, "foo"));
        assertFalse("Expected areEqual(5, null) to return false", Equality.areEqual(5, null));
        assertFalse("Expected areEqual(null, 5) to return false", Equality.areEqual(null, 5));
        
        assertFalse("Expected areEqual between string and int array to return false", Equality.areEqual("foo", new int[] {1, 2, 3}));
        assertFalse("Expected areEqual between int array and string to return false", Equality.areEqual(new int[] {1, 2, 3}, "foo"));
    }
    
    @Test
    public void testAreArraysEqualWhenArrayLengthsAreEqualAndArrayElementsAreEqual() {
        assertTrue("Expected areArraysEqual for identical arrays to return true", Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse("Expected areArraysEqual for different arrays to return false", Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testAreArrayLengthsEqualWhenArrayLengthsAreEqual() {
        assertTrue("Expected areArrayLengthsEqual for arrays of same length to return true", Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse("Expected areArrayLengthsEqual for arrays of different lengths to return false", Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3, 4}));
    }
    
    @Test
    public void testAreArrayElementsEqualWhenArrayElementsAreEqual() {
        assertTrue("Expected areArrayElementsEqual for arrays with equal elements to return true", Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse("Expected areArrayElementsEqual for arrays with different elements to return false", Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testIsArray() {
        assertTrue("Expected isArray for array object to return true", Equality.isArray(new int[] {1, 2, 3}));
        assertFalse("Expected isArray for non-array object to return false", Equality.isArray("foo"));
    }
    
}
