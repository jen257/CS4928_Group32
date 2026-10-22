package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertNotNull(multiKey);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }
    
    @Test
    public void testConstructorThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertNotNull(multiKey);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testConstructorFourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertNotNull(multiKey);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }
    
    @Test
    public void testConstructorFiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertNotNull(multiKey);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }
    
    @Test
    public void testConstructorArrayKeys() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertNotNull(multiKey);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }
    
    @Test
    public void testGetKeys() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertNotNull(multiKey);
        String[] retrievedKeys = multiKey.getKeys();
        assertArrayEquals(keys, retrievedKeys);
    }
    
    @Test
    public void testGetKey() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertNotNull(multiKey);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        multiKey.getKey(3);
    }
    
    @Test
    public void testSize() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertNotNull(multiKey);
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testEqualsSameObject() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(multiKey, multiKey);
    }
    
    @Test
    public void testEqualsDifferentObject() {
        String[] keys1 = {"key1", "key2", "key3"};
        String[] keys2 = {"key1", "key2", "key3"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertEquals(multiKey1, multiKey2);
    }
    
    @Test
    public void testEqualsDifferentSize() {
        String[] keys1 = {"key1", "key2", "key3"};
        String[] keys2 = {"key1", "key2"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertNotEquals(multiKey1, multiKey2);
    }
    
    @Test
    public void testEqualsDifferentKeys() {
        String[] keys1 = {"key1", "key2", "key3"};
        String[] keys2 = {"key1", "key2", "key4"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertNotEquals(multiKey1, multiKey2);
    }
    
    @Test
    public void testHashCodeSameObject() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        int hashCode = multiKey.hashCode();
        assertEquals(hashCode, multiKey.hashCode());
    }
    
    @Test
    public void testToString() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertNotNull(multiKey);
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
}